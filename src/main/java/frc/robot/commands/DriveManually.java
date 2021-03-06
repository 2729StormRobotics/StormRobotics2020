/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

import java.util.function.DoubleSupplier;

public class DriveManually extends CommandBase {
  private final Drivetrain m_drivetrain;
  private final DoubleSupplier m_leftSpeed;
  private final DoubleSupplier m_rightSpeed;
  private final DoubleSupplier m_forwardSpeed;
  private final DoubleSupplier m_reverseSpeed;
  private double m_currentSpeed = 0;

  /**
   * Creates a new DriveManually.
   * 
   * @param leftSpeed  speed of left motors
   * @param rightSpeed speed of right motors
   * @param subsystem  the subsystem being used--in this case, the drivetrain
   */
  public DriveManually(DoubleSupplier forwardSpeed, DoubleSupplier reverseSpeed, DoubleSupplier leftSpeed,
      DoubleSupplier rightSpeed, Drivetrain subsystem) {
    m_drivetrain = subsystem;
    m_leftSpeed = leftSpeed;
    m_rightSpeed = rightSpeed;
    m_forwardSpeed = forwardSpeed;
    m_reverseSpeed = reverseSpeed;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_drivetrain.stopDrive();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (Math.abs(m_forwardSpeed.getAsDouble() - m_reverseSpeed.getAsDouble()) > 0.03) {
      m_drivetrain.triggerDrive(m_forwardSpeed.getAsDouble(), m_reverseSpeed.getAsDouble(), 0, true);
    } else {
      m_drivetrain.tankDrive(m_leftSpeed.getAsDouble(), m_rightSpeed.getAsDouble(), true);
    }
    m_currentSpeed = m_drivetrain.getAverageSpeed();

    // Automatic transmission
    // if (Math.abs(m_currentSpeed) > kShiftSpeed) {
    // m_drivetrain.shiftHigh();
    // }
    // else {
    // m_drivetrain.shiftLow();
    // }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_currentSpeed = 0;
    m_drivetrain.stopDrive();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
