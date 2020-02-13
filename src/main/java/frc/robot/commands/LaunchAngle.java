/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Launcher;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class LaunchAngle extends InstantCommand {
  private final Launcher m_launch;
  private final boolean m_isExtended;
  
  /**
   * Creates a new ToggleLaunchAngle.
   */
  public LaunchAngle(Boolean extended, Launcher launcher) {
    // Set launcher subsystem equal to command parameter
    m_launch = launcher;

    // Set m_isExtended to the boolean parameter
    m_isExtended = extended;
    
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_launch);
  }

  /**
   * This command changes the angle of the power cell launcher
   */
  @Override
  public void initialize() {
    m_launch.setLaunchPiston(m_isExtended);
  }
}
