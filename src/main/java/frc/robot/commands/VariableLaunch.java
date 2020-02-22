/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Launcher;

public class VariableLaunch extends CommandBase {
  private final Launcher m_launcher;

  /**
   * Variable launch with calculated speed based on distance from vision target.
   * 
   * @param launcher The Launcher subsystem to pass in.
   */
  public VariableLaunch(Launcher launcher) {
    m_launcher = launcher;
    addRequirements(m_launcher);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    double speed = m_launcher.calculateLaunchSpeed();
    m_launcher.revToSpeed(speed);
  }

  @Override
  public void end(boolean interrupted) {
    m_launcher.stopRevving();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
     return false;
  }
}
