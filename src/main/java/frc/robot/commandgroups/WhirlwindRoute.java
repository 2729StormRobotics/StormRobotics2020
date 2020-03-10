/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commandgroups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Launcher;
import frc.robot.subsystems.Cellevator;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.commands.DriveDistance;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class WhirlwindRoute extends SequentialCommandGroup {
  /**
   * Creates a new WhirlwindRoute.
   */
  private final Drivetrain m_drivetrain;
private final Launcher m_launcher;
private final Intake m_intake;
private final Hopper m_hopper;
private final Cellevator m_cellevator;

  public WhirlwindRoute(Drivetrain m_drivetrain,  Launcher m_launcher){
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super( new DriveDistance(36, drivetrain).withTimeout(3),




    );
  m_drivetrain = drivetrain;
    m_launcher = launcher;
    m_intake = intake;
    m_hopper = hopper;
    m_cellevator = cellevator;
  }  
}
