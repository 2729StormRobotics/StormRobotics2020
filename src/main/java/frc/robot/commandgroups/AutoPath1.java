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

public class AutoPath1 extends SequentialCommandGroup {
  /** 
   * Creates a new AutoPath1.
   */
  public AutoPath1(Drivetrain drivetrain, Launcher launcher, Intake intake, Hopper hopper) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    addCommands(
      new DriveDistance(3.2084, drivetrain), //moves forward 3.208 meters.
      new PointTurn(-90, drivetrain),        //turns -90 degrees counterclockwise
      new DriveDistance(.8021, drivetrain), //moves toward power cells
      new AutoIntake(intake, hopper),       //intakes power cells
      new PointTurn(180, drivetrain),      //turns 180 degrees around
      new DriveDistance(.8021, drivetrain), 
      new PointTurn(90, drivetrain),      //turns 90 degrees counterclockwise 
      new DriveDistance(3.2084, drivetrain), //drives back to the line
      new VariableLaunch(launcher)         //launces the power cells
    );
  }
}
