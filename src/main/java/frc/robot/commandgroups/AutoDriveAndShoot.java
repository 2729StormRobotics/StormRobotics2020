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
public class AutoDriveAndShoot extends SequentialCommandGroup {
private final Drivetrain m_drivetrain;
  /**
   * Creates a new AutoPowerMove.
   */
  public AutoDriveAndShoot(Drivetrain drivetrain, Launcher launcher, Intake intake, Hopper hopper,
      Cellevator cellevator) {

    // Drive straight for 130 inches, then turn 90 degrees, then enter launcher mode
    // for 8 seconds
    super(new IntakeLower(intake),
        new DriveShiftLow(drivetrain),
        new DriveDistance(12, drivetrain).withTimeout(3),
        new VisionAlign(drivetrain).withTimeout(1.5),
        new LauncherMode(launcher, intake, hopper, cellevator).withTimeout(8));

    m_drivetrain = drivetrain;
  }

  @Override
  public void end(boolean interrupted) {
    m_drivetrain.shiftHigh();
  }
}
