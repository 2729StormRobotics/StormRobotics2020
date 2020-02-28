/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.TrapezoidProfileCommand;
import frc.robot.subsystems.Drivetrain;

import static frc.robot.Constants.DriveConstants.DriveDistance.*;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class DriveDistanceTest extends TrapezoidProfileCommand {
  /**
   * Creates a new DriveDistance.
   */
  public DriveDistanceTest(Drivetrain drive) {
    super(
        // The motion profile to be executed
        new TrapezoidProfile(
            // The motion profile constraints
            new TrapezoidProfile.Constraints(kMaxVelocity, kMaxAcceleration),
            // Goal state
            new TrapezoidProfile.State(drive.getTargetDistance().getDouble(0), 0),
            // Initial state
            new TrapezoidProfile.State(0, 0)),
        state -> {
          // Use current trajectory state here
          drive.setDriveStates(state, state);
        }, drive);

    drive.pidAdjustDistance();
    drive.stopDrive();
    drive.shiftLow();
    drive.resetAllEncoders();
  }
}
