/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ControlPanel;

public class ControlPanelSpinForColor extends CommandBase {
  private final ControlPanel m_controlPanel;

  /**
   * Creates a new ControlPanelSpinForRevs.
   */
  public ControlPanelSpinForColor(ControlPanel subsystem) {
    m_controlPanel = subsystem;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_controlPanel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_controlPanel.stopSpinning();
    m_controlPanel.spinWheelForRevolutions();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_controlPanel.detectCurrentColor();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_controlPanel.stopSpinning();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_controlPanel.onTargetColor();
  }
}
