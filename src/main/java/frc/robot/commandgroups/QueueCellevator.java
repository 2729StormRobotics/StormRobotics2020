/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commandgroups;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.CellevatorLoaderMotor;
import frc.robot.commands.MoveHopperMotor;
import frc.robot.commands.CellevatorHolder;
import frc.robot.commands.VariableLaunch;
import frc.robot.subsystems.CellevatorLoader;
import frc.robot.subsystems.Cellevator;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Launcher;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class QueueCellevator extends ParallelCommandGroup {
  /**
   * Creates a new QueueCellevator.
   */
  public QueueCellevator(CellevatorLoader m_loader, Cellevator m_holder, Hopper m_hopper, Launcher m_launcher) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());super();
    // run loader, hopper, cellevator, and rev to speed parallel
    // rev to speed is to maintain the speed of the launcher while the power cells are queueing
    super(new CellevatorLoaderMotor(m_loader), new MoveHopperMotor(m_hopper), new CellevatorHolder(m_holder), new VariableLaunch(m_launcher));
  }
}
