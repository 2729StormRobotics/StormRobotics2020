/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Drivetrain;

import static frc.robot.Constants.VisionConstants.*;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class VisionAlign extends PIDCommand {
  /**
   * Creates a new VisionAlign.
   */
  public VisionAlign(Drivetrain drivetrain) {
    super(
        // The controller that the command will use
        new PIDController(kAutoAlignP, kAutoAlignI, kAutoAlignD),
        // This should return the measurement
        () -> drivetrain.getVisionTargetXOffset(),
        // This should return the setpoint (can also be a constant)
        () -> 0,
        // This uses the output
        output -> {
          /**
           * If a target is detected when the command is called the robot will align
           * itself to the target using arcade drive.
           */
          if (drivetrain.isVisionTargetDetected()) {
            drivetrain.arcadeDrive(0, output, false);
          }
        }, drivetrain);

    getController().setTolerance(kAutoAlignTolerance, kAutoAlignSpeedTolerance);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }
}
