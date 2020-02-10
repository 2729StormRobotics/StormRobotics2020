/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.TrapezoidProfileCommand;

import frc.robot.Constants.*;
import frc.robot.subsystems.*;


// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class DriveDistance extends TrapezoidProfileCommand {
  /**
   * Creates a new DriveDistance.
   */
  public DriveDistance(double distanceMeters, Drivetrain drive) {
    super(
        // The motion profile to be executed
        new TrapezoidProfile(
            // The motion profile constraints(limit max vel and acc)
            new TrapezoidProfile.Constraints(, 0),
            // Goal state
            new TrapezoidProfile.State(),
            // Initial state
            new TrapezoidProfile.State()),
        state -> {
          // Use current trajectory state here
        });
  }
}
