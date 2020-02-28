/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commandgroups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.HopperAgitate;
import frc.robot.commands.IntakeLower;
import frc.robot.commands.IntakeRun;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class IntakeAndAgitate extends SequentialCommandGroup {

  /**
   * Creates a new IntakeAndCellevate.
   */
  public IntakeAndAgitate(Intake intake, Hopper hopper) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());super();
    super(new IntakeLower(intake), new IntakeRun(intake), new HopperAgitate(hopper));
  }
}
