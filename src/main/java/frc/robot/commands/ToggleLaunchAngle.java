/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Launcher;

public class ToggleLaunchAngle extends InstantCommand {
  private final Launcher m_launcher;

  /**
   * Toggle the launch pistons.
   * 
   * @param launcher The Launcher subsystem to pass in.
   */
  public ToggleLaunchAngle(Launcher launcher) {
    m_launcher = launcher;
    addRequirements(m_launcher);
  }

  /**
   * Runs when the command is first started.
   */
  @Override
  public void initialize() {
    m_launcher.toggleLaunchPiston();
  }
}
