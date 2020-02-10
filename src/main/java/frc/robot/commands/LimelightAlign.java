/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.*;

import frc.robot.subsystems.*;
import static frc.robot.Constants.LimelightConstants.*;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class LimelightAlign extends PIDCommand {
  // A fixed speed for the robot to spin if no target is detected

  /**
   * Creates a new LimelightAlign.
   */
  public LimelightAlign(Limelight limesub, Drivetrain drivesub) {
    super(
        // The controller that the command will use
        new PIDController(kLimelightAlignP, kLimelightAlignI, kLimelightAlignD),
        // This should return the measurement (Angle offset on the X-axis of the camera)
        () -> limesub.getXOffset(),
        // This should return the setpoint (can also be a constant)
        () -> 0,
        // This uses the output
        output -> {
          /**
           * If a target is detected when the command is called the robot will align
           * itself to the target using arcade drive. If no target is found it will spin
           * at a fixed speed until one comes into range
           */
          if (limesub.isTargetDetected()) {
            drivesub.arcadeDrive(0, output, true);
          } else {
            drivesub.arcadeDrive(0, steeringAdjust, true);
          }
        });

    // Sends a PID table to the SmartDashboard
        SmartDashboard.putData("AlignmentPID", getController());
    // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(limesub, drivesub);
    // Configure additional PID options by calling `getController` here.
        getController().setTolerance(kLimelightAlignTolerance);
  }

  // Returns true when the target is within the angle tolerance
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }
}
