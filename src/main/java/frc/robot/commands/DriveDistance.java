/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.TrapezoidProfileCommand;

import static frc.robot.Constants.DriveConstants.*;

import java.util.function.DoubleSupplier;
import frc.robot.subsystems.Drivetrain;

public class DriveDistance extends TrapezoidProfileCommand {

  /**
   * Drive straight a specified distance.
   * 
   * @param distance Target distance in inches.
   * @param drivetrain The Drivetrain passed through to run DriveDistance.
   */
  public DriveDistance(DoubleSupplier distance, Drivetrain drivetrain) {
    super(
        // The motion profile to be executed
        new TrapezoidProfile(
            // Limit the max speed and acceleration
            new TrapezoidProfile.Constraints(kMaxSpeed, kMaxAcceleration),
            
            // End desired distance at targetTravelDistance
            new TrapezoidProfile.State(distance.getAsDouble(), 0)), // Velocity ends at 0.
            
            // Send the profile state to the drivetrain
            state -> drivetrain.setDriveStates(state, state), 
            drivetrain);
        
      drivetrain.resetAllEncoders();
  }
}
