/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

import java.util.function.DoubleSupplier;

public class DriveManually extends CommandBase {
  private final Drivetrain m_drivetrain;
  private final DoubleSupplier m_leftTank;
  private final DoubleSupplier m_rightTank;
  private final DoubleSupplier m_forwardTrigger;
  private final DoubleSupplier m_reverseTrigger;
  private final DoubleSupplier m_turn;
  private final SendableChooser<String> m_driveChooser;
  private String m_driveType = "Tank";
  private double m_currentSpeed = 0;

  /**
   * Creates a new DriveManually.
   * 
   * @param leftSpeed  speed of left motors
   * @param rightSpeed speed of right motors
   * @param subsystem  the subsystem being used--in this case, the drivetrain
   */
  public DriveManually(SendableChooser<String> type, DoubleSupplier rightTrigger, DoubleSupplier leftTrigger,
      DoubleSupplier leftY, DoubleSupplier rightY, DoubleSupplier rightX, DoubleSupplier leftX, Drivetrain subsystem) {
    m_drivetrain = subsystem;
    m_leftTank = leftY;
    m_rightTank = rightY;
    m_forwardTrigger = rightTrigger;
    m_reverseTrigger = leftTrigger;
    m_turn = leftX;

    m_driveChooser = type;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_drivetrain.stopDrive();
    m_driveType = m_driveChooser.getSelected();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_driveType = m_driveChooser.getSelected();

    if (m_driveType.equals("Arcade")) {
      m_drivetrain.arcadeDrive(m_rightTank.getAsDouble(), m_turn.getAsDouble(), true);
    } else if (m_driveType.equals("Trigger")) {
      m_drivetrain.triggerDrive(m_forwardTrigger.getAsDouble(), m_reverseTrigger.getAsDouble(), m_turn.getAsDouble(),
          true);
    } else {
      m_drivetrain.tankDrive(m_leftTank.getAsDouble(), m_rightTank.getAsDouble(), true);
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
