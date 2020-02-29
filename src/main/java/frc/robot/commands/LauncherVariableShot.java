/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Launcher;

public class LauncherVariableShot extends CommandBase {
  private final Launcher m_launcher;
  private double previousSpeed = 0;

  /**
   * Creates a new LauncherVariableShot.
   */
  public LauncherVariableShot(Launcher subsystem) {
    m_launcher = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_launcher.stopLauncher();
    m_launcher.setLongLaunchAngle();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double speed = m_launcher.calculateLaunchSpeed();
    if (Math.abs(speed - previousSpeed) > 1) {
      m_launcher.revToSpeed(speed);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_launcher.stopLauncher();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
