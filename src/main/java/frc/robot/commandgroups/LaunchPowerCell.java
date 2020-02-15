/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commandgroups;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.HolderOutake;
import frc.robot.commands.VariableLaunch;
import frc.robot.subsystems.Cellevator;
import frc.robot.subsystems.Launcher;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class LaunchPowerCell extends ParallelCommandGroup {
  /**
   * Creates a new LaunchPowerCell.
   */
  public LaunchPowerCell(Cellevator m_holder, Launcher m_launcher) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());super();
    // run the holder outake to push the power cells into the launcher to launch running parallel
    // runs the rev to speed command to maintain the right speed when launching
    super(new HolderOutake(m_holder), new VariableLaunch(m_launcher));
  }
}
