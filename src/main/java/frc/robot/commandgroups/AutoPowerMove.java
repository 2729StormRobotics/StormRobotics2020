/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commandgroups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.DriveDistance;
import frc.robot.commands.DrivePointTurn;
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
public class AutoPowerMove extends SequentialCommandGroup {

  /**
   * Creates a new AutoPowerMove.
   */
  public AutoPowerMove(Drivetrain drivetrain, Launcher launcher, Intake intake, Hopper hopper, Cellevator cellevator) {

    // Drive straight for 130 inches, then turn 90 degrees, then enter launcher mode
    // for 8 seconds
    super(new IntakeLower(intake),
        new DriveShiftLow(drivetrain),
        new DriveDistance(130, drivetrain).withTimeout(3),
        new DrivePointTurn(45, drivetrain).withTimeout(1.5),
        new DriveDistance(150, drivetrain).withTimeout(5),
        new DrivePointTurn(-20, drivetrain),
        new VisionAlign(drivetrain).withTimeout(1.5),
        new LauncherMode(launcher, intake, hopper, cellevator).withTimeout(8));
  }
}
