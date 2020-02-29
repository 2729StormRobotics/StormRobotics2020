/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commandgroups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.DriveDistance;
import frc.robot.commands.DriveShiftLow;
import frc.robot.commands.IntakeLower;
import frc.robot.commands.VisionAlign;
import frc.robot.subsystems.Cellevator;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Launcher;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class AutoShootThenDrive extends SequentialCommandGroup {
private final Drivetrain m_drivetrain;
private final Launcher m_launcher;
private final Intake m_intake;
private final Hopper m_hopper;
private final Cellevator m_cellevator;

  /**
   * Creates a new AutoPowerMove.
   */
  public AutoShootThenDrive(Drivetrain drivetrain, Launcher launcher, Intake intake, Hopper hopper,
      Cellevator cellevator) {


    // Drive straight for 130 inches, then turn 90 degrees, then enter launcher mode
    // for 8 seconds
    super(new IntakeLower(intake),
        new VisionAlign(drivetrain).withTimeout(3),
        new LauncherMode(launcher, intake, hopper, cellevator).withTimeout(6),
        new DriveShiftLow(drivetrain),
        new DriveDistance(24, drivetrain).withTimeout(3)
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
