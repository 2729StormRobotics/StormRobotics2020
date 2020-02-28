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

import static frc.robot.Constants.DriveConstants.*;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class DriveDistancePID extends PIDCommand {
  /**
   * Creates a new PointTurn.
   */
  public DriveDistancePID(Drivetrain drivetrain) {
    super(
        // The controller that the command will use
        new PIDController(drivetrain.getDistanceP(), drivetrain.getDistanceI(), drivetrain.getDistanceD()),
        // This should return the measurement
        () -> drivetrain.getAverageDistance(),
        // This should return the setpoint (can also be a constant)
        () -> drivetrain.getDistanceTarget().getDouble(0),
        // This uses the output
        output -> {
          // Use the output here
          drivetrain.arcadeDrive(-output, 0, false);
        });
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
    // Configure additional PID options by calling `getController` here.
    getController().setTolerance(1, 5);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }
}
