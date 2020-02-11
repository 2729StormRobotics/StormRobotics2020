/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Launcher;

public class ToggleLaunchAngle extends CommandBase {
  private final Launcher m_launchAngle;
  private final boolean m_isExtended;
  
  /**
   * Creates a new ToggleLaunchAngle.
   */
  public ToggleLaunchAngle(Boolean extended, Launcher launcher) {
    // Set launcher subsystem equal to command parameter
    m_launchAngle = launcher;

    // Set m_isExtended to the boolean parameter
    m_isExtended = extended;
    
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_launchAngle);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_launchAngle.setLaunchPiston(m_isExtended);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
