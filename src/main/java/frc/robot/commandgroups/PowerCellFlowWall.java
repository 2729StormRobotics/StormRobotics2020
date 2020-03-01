/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commandgroups;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.CellevateForLaunch;
import frc.robot.commands.HopperAgitate;
import frc.robot.commands.IntakeRun;
import frc.robot.commands.LauncherWallShot;
import frc.robot.subsystems.Cellevator;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Launcher;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class PowerCellFlowWall extends ParallelCommandGroup {
  /**
   * Creates a new PowerCellFlow.
   */
  public PowerCellFlowWall(Launcher launcher, Intake intake, Hopper hopper, Cellevator cellevator) {
    // Add your commands in the super() call, e.g.
    super(new LauncherWallShot(launcher), new IntakeRun(intake), new HopperAgitate(hopper), new CellevateForLaunch(cellevator));
  }
}
