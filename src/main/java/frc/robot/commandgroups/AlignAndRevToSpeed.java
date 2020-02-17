/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commandgroups;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.LimelightAlign;
import frc.robot.commands.VariableLaunch;
import frc.robot.subsystems.Launcher;
import frc.robot.subsystems.Drivetrain;


// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class AlignAndRevToSpeed extends ParallelCommandGroup {
  /**
   * Creates a new AlignAndRevToSpeed.
   */
  public AlignAndRevToSpeed(Drivetrain m_drivetrain, Launcher m_launcher) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());super();
    // running the align and rev to speed commands in parallel
    super(new LimelightAlign(m_drivetrain), new VariableLaunch(m_launcher));
  }
}
