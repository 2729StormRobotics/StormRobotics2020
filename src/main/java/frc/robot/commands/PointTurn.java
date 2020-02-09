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

public class PointTurn extends PIDCommand {

  // Declare the drivetrain member variable
  private final Drivetrain m_drivetrain;

  // Declare the variable for the initial angle.
  private static double m_initialAngle;

  /**
   * Rotates the robot a certain amount of degrees using PID.
   * 
   * @param angle The angle in degrees by which the robot will rotate. Must be between -180 and 180 degrees.
   * @param drivetrain Pass in the drivetrain for the command to use.
   * 
   */
  public PointTurn(double angle, Drivetrain drivetrain) {
    super(
        // The controller that the command will use
        new PIDController(PointTurnPID.kP, PointTurnPID.kI, PointTurnPID.kD),
        // This returns the measurement (current angle of the robot relative to initial angle)
        () -> drivetrain.getRobotAngle() - m_initialAngle,
        // This returns the setpoint (target angle relative to initial angle)
        () -> angle,
        // This uses the output (rotates the robot using arcadeDrive)
        output -> {
          // Note: uses negative output because arcadeDrive considers clockwise angles to be positive,
          // but the gyro considers counter-clockwise angles to be positive.
          drivetrain.arcadeDrive(0, -output, false);
        });
    // Assign drivetrain member variable to drivetrain passed in constructor.
    m_drivetrain = drivetrain;
    // Add Drivetrain as a required subsystem.
    addRequirements(m_drivetrain);
    // Set the position and velocity tolerances.
    getController().setTolerance(PointTurnPID.kPositionTolerance, PointTurnPID.kVelocityTolerance);

    // Set the wrap-around point for the PIDController to move in the shortest route to the setpoint.
    // Note: this clamps the setpoint between -180 and 180, meaning that the setpoint should never be outside that range.
    getController().enableContinuousInput(-180, 180);
  }

  // Runs when the command is scheduled.
  @Override
  public void initialize() {
    // Save the angle of the gyro at the beginning of PointTurn.
    m_initialAngle = m_drivetrain.getRobotAngle();
    super.initialize();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // Return whether or not the angle and angular velocity are within their respective tolerances.
    return getController().atSetpoint();
  }
}
