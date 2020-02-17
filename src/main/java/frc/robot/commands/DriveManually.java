/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class DriveManually extends CommandBase {
  private final Drivetrain m_drivetrain;
  private final DoubleSupplier m_leftSpeed;
  private final DoubleSupplier m_rightSpeed;

/**
 * @param leftSpeed   speed of left motors
 * @param rightSpeed  speed of right motors
 * @param subsystem   the subsystem being used--in this case, the drivetrain
 */
  public DriveManually(DoubleSupplier leftSpeed, DoubleSupplier rightSpeed, Drivetrain subsystem) {
    // These are the local instances of the parameter variables for ManualDrive.
    m_drivetrain = subsystem;
    m_leftSpeed = leftSpeed;
    m_rightSpeed = rightSpeed;

    // Attach the local instance of the subsystem--in this case, drivetrain--to the command.
    addRequirements(m_drivetrain);
  }

  // Called just before this Command runs the first time.
  @Override
  public void initialize() {
    m_drivetrain.stopDrive();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Repeatedly passes the parameters--left speed, right speed, and if inputs are
    //squared, which increases fine control at lower speeds--to the tankDrive
    m_drivetrain.tankDrive(m_leftSpeed.getAsDouble(), m_rightSpeed.getAsDouble(), true);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // Stop the robot if this command ends naturally or is interrupted.
    // It will not end naturally.
    m_drivetrain.stopDrive();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
     // Since ManualDrive is the default, it should never stop running
     return false;
  }
}
