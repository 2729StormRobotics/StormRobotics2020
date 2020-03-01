/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Launcher;

import static frc.robot.Constants.LauncherConstants.kWallShotSpeed;;


public class LauncherWallShot extends CommandBase {
  private final Launcher m_launcher;

  /**
   * Creates a new LauncherVariableShot.
   */
  public LauncherWallShot(Launcher subsystem) {
    m_launcher = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_launcher.stopLauncher();
    m_launcher.setShortLaunchAngle();
    m_launcher.revWallShot();
  }

  @Override
  public boolean isFinished() {
    return Math.abs(m_launcher.getLauncherAvgSpeed() -  kWallShotSpeed) < .5;
  }
}
