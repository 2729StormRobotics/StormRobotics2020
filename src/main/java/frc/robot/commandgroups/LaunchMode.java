/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commandgroups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Cellevator;
import frc.robot.subsystems.CellevatorLoader;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Launcher;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class LaunchMode extends SequentialCommandGroup {
  /**
   * Creates a new LaunchMode.
   */
  public LaunchMode(Drivetrain m_drivetrain, Launcher m_launcher, CellevatorLoader m_loader, Cellevator m_holder, Hopper m_hopper) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    // runs the Align and Rev, Queueing the cellevator, and launching in that order 
    super(new AlignAndRevToSpeed(m_drivetrain, m_launcher), new QueueCellevator(m_loader, m_holder, m_hopper, m_launcher), new LaunchPowerCell(m_holder, m_launcher));
  }

public LaunchMode(int i, Launcher m_launcher) {
}
}
