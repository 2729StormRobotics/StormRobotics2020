/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class ManualDrive extends CommandBase {
  private final Drivetrain m_drivetrain;
  private final DoubleSupplier m_reverseSpeed;
  private final DoubleSupplier m_forwardSpeed;
  private final DoubleSupplier m_turnSpeed;

  /**
   * @param reverse     The backwards speed of the robot
   * @param forward     The forwards speed of the robot
   * @param turn        The turning speed of the robot
   * @param subsystem   The subsystem being used by the command--in this case, the drivetrain
   */

  public ManualDrive(DoubleSupplier reverse, DoubleSupplier forward, DoubleSupplier turn, Drivetrain subsystem) {
    // These are the local instances of the parameter variables for ManualDrive.
    m_drivetrain = subsystem;
    m_reverseSpeed = reverse;
    m_forwardSpeed = forward;
    m_turnSpeed = turn;

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
    m_drivetrain.triggerDrive(m_reverseSpeed.getAsDouble(), m_forwardSpeed.getAsDouble(), m_turnSpeed.getAsDouble(), true);
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
