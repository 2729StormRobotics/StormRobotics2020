/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commandgroups;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.IntakePowerCell;
import frc.robot.commands.MoveIntakePistons;
import frc.robot.commands.MoveHopperMotor;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Hopper;
import frc.robot.Constants.IntakeConstants;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class AutoIntake extends ParallelCommandGroup {
  /**
   * Creates a new AutoIntake.
   */
  public AutoIntake(Intake m_intake, Hopper m_hopper) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());super();
    //running these intake and hopper commands in parallel
    super(new IntakePowerCell(IntakeConstants.kIntakeMotorSpeed, m_intake), new MoveIntakePistons(m_intake), new MoveHopperMotor(m_hopper));
    // to toggle pistons so intake goes back up will be done in RobotContainer
  }
}
