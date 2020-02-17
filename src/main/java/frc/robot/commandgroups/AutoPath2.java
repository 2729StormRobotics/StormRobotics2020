/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commandgroups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.Constants.*;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class AutoPath2 extends SequentialCommandGroup {
  /**
   * Creates a new AutoPath2.
   */
  public AutoPath2(Drivetrain drivetrain, Launcher launcher, Intake intake, Hopper hopper) {
    // Add your commands in the super() call, e.g.
    super(
      new PointTurn(68.57, drivetrain), //turn 68.75 degrees counter clockwise
      new DriveDistance(135.69924, drivetrain),
      new PointTurn(-21.43, drivetrain), //turns 21.43 degrees clockwise
      new AutoIntake(intake, hopper), //intake first power cell
      new DriveDistance(75.789 , drivetrain), //moves 75.789 inches forward
      new AutoIntake(intake, hopper)
    );
  }
}
