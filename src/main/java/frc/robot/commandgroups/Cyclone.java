/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commandgroups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.*;
import frc.robot.subsystems.Cellevator;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Launcher;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class Cyclone extends SequentialCommandGroup {
  public final Drivetrain m_drivetrain;
  public final Intake m_intake;
  public final Cellevator m_cellevator;
  public final Launcher m_launcher;
  public final Hopper m_hopper;

  /**
   * Creates a new Cyclone.
   */
  public Cyclone(Drivetrain drivetrain, Launcher launcher, Intake intake, Hopper hopper,
  Cellevator cellevator) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(
    new IntakeLower(intake),
    new DriveShiftLow(drivetrain),
    new VisionAlign(drivetrain).withTimeout(3),
    new ConditionalCommand(
        new LauncherMode(launcher, intake, hopper, cellevator).withTimeout(8),
        new DoNothingAuto(),
        () -> drivetrain.isTargetCentered()
    ),
    new DrivePointTurn(() -> 45, drivetrain).withTimeout(3),
    new DriveDistance(192, drivetrain).withTimeout(3),
    new IntakeToggle(intake).withTimeout(3),
    new ConditionalCommand(
        new LauncherMode(launcher, intake, hopper, cellevator).withTimeout(8),
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
  @Override
    public void end(boolean interrupted) {
      m_drivetrain.shiftHigh();
    m_launcher.stopLauncher();
    m_intake.stopIntake();
    m_hopper.stopHopper();
    m_cellevator.stopCellevator();
    m_cellevator.stopLoader();
    }
}
