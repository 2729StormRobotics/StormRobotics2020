/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ControlPanel;

public class MovePanelForRevs extends CommandBase {
  private final ControlPanel m_controlPanel;

  public MovePanelForRevs(ControlPanel subsystem) {
    m_controlPanel = subsystem;
    addRequirements(m_controlPanel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_controlPanel.resetColorCount();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_controlPanel.spinWheelForRevolutions(); // Sets power
    m_controlPanel.countByColor(); // updates color count
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_controlPanel.stopSpinning();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (m_controlPanel.isSpun()); // Ends when we've detected that it's been spun at least 8 times
  }
}
