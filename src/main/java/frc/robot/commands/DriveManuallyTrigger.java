/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import static frc.robot.Constants.DriveConstants.*;

import java.util.function.DoubleSupplier;

public class DriveManuallyTrigger extends CommandBase {
  private final Drivetrain m_drivetrain;
  private final DoubleSupplier m_forward;
  private final DoubleSupplier m_reverse;
  private final DoubleSupplier m_turn;
  private double m_currentSpeed = 0;

  /**
   * Creates a new DriveManually.
   * 
   * @param leftSpeed  speed of left motors
   * @param rightSpeed speed of right motors
   * @param subsystem  the subsystem being used--in this case, the drivetrain
   */
  public DriveManuallyTrigger(DoubleSupplier forward, DoubleSupplier reverse, DoubleSupplier turn, Drivetrain subsystem) {
    m_drivetrain = subsystem;
    m_reverse = reverse;
    m_forward = forward;
    m_turn = turn;

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
    m_drivetrain.triggerDrive(m_forward.getAsDouble(), m_reverse.getAsDouble(), m_turn.getAsDouble(), true);
    m_currentSpeed = m_drivetrain.getAverageSpeed();

    // Shift gears if we're at our shifting speed
    if (Math.abs(m_currentSpeed) > kShiftSpeed) {
      m_drivetrain.shiftHigh();
    }
    else {
      m_drivetrain.shiftLow();
    }
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
