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
  private final Double m_leftSpeed;
  private final Double m_rightSpeed;

/**
 * @param leftSpeed   speed of left motors
 * @param rightSpeed  speed of right motors
 * @param subsystem   the subsystem being used--in this case, the drivetrain
 */
  public ManualDrive(Double leftSpeed, Double rightSpeed, Drivetrain subsystem) {
    // These are the local instances of the parameter variables for ManualDrive.
    m_drivetrain = subsystem;
    m_leftSpeed = leftSpeed;
    m_rightSpeed = rightSpeed;

    // Attach the local instane of the subsystem--in this case, drivetrain--to the command.
    addRequirements(m_drivetrain);
  }

  // Called just before this Command runs the first time.
  @Override
  public void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
    // Repeatedly passes the parameters--reverse speed, forward speed, turn speed,
    // and if inputs are squared, which increases fine control at lower speeds--
    //to the triggerDrive
    m_drivetrain.tankDrive(m_leftSpeed, m_rightSpeed, true);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    // Since ManualDrive is the default, it should never stop running
    return false;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean interrupted) {
    // Stop the robot if this command ends naturally or is interrupted.
    // It will not end naturally.
    m_drivetrain.stopDrive();
  }

  // // Called when another command which requires one or more of the same
  // // subsystems is scheduled to run
  // @Override
  // public void interrupted() {
  // }
}
