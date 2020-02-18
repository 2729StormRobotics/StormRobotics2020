/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ControlPanel;

public class MovePanelForColor extends CommandBase {
  private final ControlPanel m_controlPanel;

  public MovePanelForColor(ControlPanel subsystem) {
    m_controlPanel = subsystem;

    addRequirements(m_controlPanel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_controlPanel.spinWheelForColor(); // sets power

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_controlPanel.stopSpinning();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (m_controlPanel.onTargetColor() && m_controlPanel.isSpun()); // ends when both condidtions are true
  }
}
