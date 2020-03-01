/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commandgroups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.LauncherWallShot;
import frc.robot.subsystems.Cellevator;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Launcher;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class LauncherModeWall extends SequentialCommandGroup {
  /**
   * Creates a new LauncherMode.
   */
  public LauncherModeWall(Launcher launcher, Intake intake, Hopper hopper, Cellevator cellevator) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(new LauncherWallShot(launcher).withTimeout(2),
        new PowerCellFlowWall(launcher, intake, hopper, cellevator));
  }
}
