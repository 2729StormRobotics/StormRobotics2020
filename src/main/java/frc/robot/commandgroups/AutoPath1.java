/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commandgroups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import frc.robot.commands.DrivePointTurn;
import frc.robot.commands.DoNothingAuto;
import frc.robot.commands.DriveDistance;
import frc.robot.commands.DriveShiftLow;
import frc.robot.commands.IntakeLower;
import frc.robot.commands.IntakeRun;
import frc.robot.commands.VisionAlign;
import frc.robot.subsystems.Cellevator;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Launcher;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class AutoPath1 extends SequentialCommandGroup {
private final Drivetrain m_drivetrain;
private final Launcher m_launcher;
private final Intake m_intake;
private final Hopper m_hopper;
private final Cellevator m_cellevator;

  /**
   * Creates a new AutoPath1.
   */
  public AutoPath1(Drivetrain drivetrain, Launcher launcher, Intake intake, Hopper hopper, Cellevator cellevator) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(
      new IntakeLower(intake), 
      new DriveShiftLow(drivetrain),
      new VisionAlign(drivetrain),
      new ConditionalCommand(
          new LauncherMode(launcher, intake, hopper, cellevator).withTimeout(8),
          new DoNothingAuto(),
          () -> drivetrain.isTargetCentered()
        ),
      new DrivePointTurn(135, drivetrain),
      new DriveDistance(24, drivetrain).withTimeout(3),
      new IntakeRun(intake),
      new DrivePointTurn(180, drivetrain),
      new DriveDistance(24, drivetrain).withTimeout(3),
      new DrivePointTurn(180, drivetrain),
      new VisionAlign(drivetrain),
      new ConditionalCommand(
          new LauncherMode(launcher, intake, hopper, cellevator).withTimeout(3),
          new DoNothingAuto(),
          () -> drivetrain.isTargetCentered()
        )
      );

    m_drivetrain = drivetrain;
    m_launcher = launcher;
    m_intake = intake;
    m_hopper = hopper;
    m_cellevator = cellevator;
  }
}
