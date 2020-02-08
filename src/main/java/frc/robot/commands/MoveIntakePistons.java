/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants;
import frc.robot.subsystems.Intake;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class MoveIntakePistons extends InstantCommand {

  // uses the intake subsystem for the command to run
  private final Intake m_intake;

  public MoveIntakePistons(Intake intake) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_intake = intake; // sets the command parameter equal to the intake subsystem
    addRequirements(m_intake); // adds the intake subsystem as a requirement for this command to run
  }

  // Called when the command is initially scheduled.
  // when it is initailized it will run the method that toggles between raising and lowering the intake
  @Override
  public void initialize() {
    m_intake.toggleIntakePistons();
  }
}
