/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.ControlPanel;

public class ControlPanelStop extends InstantCommand {
  private final ControlPanel m_controlPanel;

  /**
   * Creates a new ControlPanelStop.
   */
  public ControlPanelStop(ControlPanel subsystem) {
    m_controlPanel = subsystem;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_controlPanel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_controlPanel.stopSpinning();
  }
}