/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ControlPanel;

public class FindColor extends CommandBase {
  private final ControlPanel m_findColor;

  /**
   * Creates a new FindColor.
   */
  public FindColor(ControlPanel subsystem) {
    m_findColor = subsystem;
    addRequirements(m_findColor);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {    
    m_findColor.setColor();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_findColor.findTargetColor();
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_findColor.wheelStop();

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
