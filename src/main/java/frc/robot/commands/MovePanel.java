/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ControlPanel;

public class MovePanel extends CommandBase {
  private final ControlPanel m_MovePanel;
  
  public MovePanel(ControlPanel subsystem) {
    m_MovePanel = subsystem;
    addRequirements(m_MovePanel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  m_MovePanel.colorCount();
  }
 
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  m_MovePanel.wheelMotorPower();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_MovePanel.stopSpinning();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
