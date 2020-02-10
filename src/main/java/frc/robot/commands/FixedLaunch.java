/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.revrobotics.ControlType;

import com.revrobotics.CANPIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Launcher;
import jdk.vm.ci.meta.Constant;
import frc.robot.Constants;
import frc.robot.Constants.LauncherConstants;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class FixedLaunch extends CommandBase {

  private final double m_launchSpeed;
  private final Launcher m_Launcher;

  /**
   * Creates a new FixedLaunch.
   */
  public FixedLaunch(double launchSpeed, Launcher subsystem) {
    m_launchSpeed = launchSpeed;
    m_Launcher = subsystem;
    addRequirements(m_Launcher);
  }

  // Called just before this Command runs the first time.
  @Override
  public void initialize() {
    m_Launcher.revToSpeed(m_launchSpeed);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_Launcher.stopRevving();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
     return false;
  }
}
