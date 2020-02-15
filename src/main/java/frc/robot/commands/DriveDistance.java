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
import frc.robot.subsystems.Drivetrain;


// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class DriveDistance extends TrapezoidProfileCommand {


  /**
   * Creates a new DriveDistance.
   */
  public DriveDistance(double targetTravelDistance, Drivetrain drive) {
    super(
        // The motion profile to be executed
        new TrapezoidProfile(
            // Limit the max speed and acceleration
            new TrapezoidProfile.Constraints(kMaxSpeed, kMaxAcceleration),
            
            // End desired distance at targetTravelDistance
            new TrapezoidProfile.State(targetTravelDistance, 0)), //implicitely starts at 0
            
            // Send the profile state to the drivetrain
            state -> drive.setDriveStates(state, state), 
            drive);
        
      drive.resetAllEncoders();
  }

  

}
