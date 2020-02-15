/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Drivetrain;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class LowGearDrive extends InstantCommand {
  // this command uses the drivetrain subsystem
  public final Drivetrain m_drivetrain;

  public LowGearDrive(final Drivetrain subsystem) {

    m_drivetrain = subsystem;
    addRequirements(subsystem); // adds drivetrain subsystem as a requiremnt for this command
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled
  // After initalization this command uses the low gear method
  @Override
  public void initialize() {
	m_drivetrain.shiftLow(true);
}
}
