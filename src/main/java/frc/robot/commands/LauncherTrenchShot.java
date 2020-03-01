/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Launcher;

public class LauncherTrenchShot extends InstantCommand {
  private final Launcher m_launcher;

  /**
   * Creates a new LauncherVariableShot.
   */
  public LauncherTrenchShot(Launcher subsystem) {
    m_launcher = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_launcher);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_launcher.setLongLaunchAngle();
    m_launcher.revTrenchShot();
  }
}
