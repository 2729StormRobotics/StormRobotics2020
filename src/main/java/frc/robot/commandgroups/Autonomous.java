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
import sun.tools.jconsole.VariableGridLayout;
import frc.robot.Constants.*;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class Autonomous extends SequentialCommandGroup {
  /**
   * Creates a new Autonomous.
   */
  public Autonomous(Drivetrain drivetrain, Launcher launcher, Intake intake, Hopper hopper, Limelight limelight) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    addCommands(
      new DriveDistance(3.2084, drivetrain), //moves specified distance in meters
      new PointTurn(-90, drivetrain), //turns -90 degrees counterclockwise
      new DriveDistance(.8021, drivetrain), 
      new AutoIntake(intake, hopper),
      new PointTurn(180, drivetrain),
      new DriveDistance(.8021, drivetrain),
      new PointTurn(90, drivetrain),
      new DriveDistance(3.2084, drivetrain),
      new VariableLaunch(launcher)
    );
  }
}
