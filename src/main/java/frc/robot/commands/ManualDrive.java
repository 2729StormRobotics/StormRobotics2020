/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class ManualDrive extends CommandBase {
  
  private final Drivetrain m_drivetrain;
  private final double m_leftSpeed;
  private final double m_rightSpeed;

  public ManualDrive(double left, double right, Drivetrain drivetrain) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    m_drivetrain = drivetrain;
    m_leftSpeed = left;
    m_rightSpeed = right;
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
    m_drivetrain.tankDrive(m_leftSpeed, m_rightSpeed, true);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean interrupted) {
    m_drivetrain.stopDrive();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  public void interrupted() {
  }
}
