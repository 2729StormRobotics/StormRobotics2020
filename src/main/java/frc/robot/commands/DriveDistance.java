/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.TrapezoidProfileCommand;

import edu.wpi.first.wpilibj.controller.PIDController;
import frc.robot.Constants;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.Drivetrain;


// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class DriveDistance extends TrapezoidProfileCommand {
  /**
   * Creates a new DriveDistance.
   */
  public DriveDistance(double distanceMeters, Drivetrain drivetrain) {
    super(
        // The motion profile to be executed
        new TrapezoidProfile(
            //sets mac velociy and acceleration
            new TrapezoidProfile.Constraints(DriveConstants.kMaxSpeed, DriveConstants.kMaxAcceleration),
            //Goal state(ends at distanceMeters, velocity starts at 0)
            new TrapezoidProfile.State(distanceMeters, 0)),
        state -> drivetrain.setDriveStates(state,state), 
        //drivetrain);
        //set drive states
  }
}
