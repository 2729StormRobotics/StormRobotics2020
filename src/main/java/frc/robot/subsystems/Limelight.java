/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.subsystems.Drivetrain;
import static frc.robot.Constants.LimelightConstants.*; 

public class Limelight extends SubsystemBase {
  /**
   * Creates a new Limelight.
   */

   private Drivetrain m_drivetrain;  // Add a local variable for the Drivetrain

  //Create variables for the different values given from the limelight
  public double tx;
  public double ty;
  public double ta;
  public double tv;

  // Create a network table for the limelight
  private final NetworkTable m_limelightTable;

  // Returns a value of the offset on the x-axis of the camera to the target
  public double getTX() {
    return tx;
  }

  // Returns a value of 0 or 1 depending on whether or not a target is detected
  public double getTV() {
    return tv;
  }

  // Returns true if a target is detected.
  public boolean isTargetDetected() {
    return (tv > 0.0);
  }

  // Returns true if the target is within a range of the center crosshair of the camera
  public boolean isTargetCentered() {
    return ((tx > -2.0) && (tx < 2.0) && (tx != 0.0));
  }

  // Calculates the total angle by adding the mount angle with the y-axis offset angle of the limelight
  public double LimelightAngle() {
    return (kLimelightAngle + ty);
  }

  // Return the distance from the limelight to the target (hypotenuse)
  public double limelightDistance() {
    return (kPortHeight - kLimelightHeight) / Math.tan(Math.toRadians(kLimelightAngle + ty));
  }

  public Limelight() {
    // Gets the network table for the limelight
    m_limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    // Posts information from the limelight to the SmartDashboard and to the network table
    tx = m_limelightTable.getEntry("tx").getDouble(0.0);
    ty = m_limelightTable.getEntry("ty").getDouble(0.0);
    ta = m_limelightTable.getEntry("ta").getDouble(0.0);
    tv = m_limelightTable.getEntry("tv").getDouble(0.0);

    SmartDashboard.putNumber("LimelightX", tx);
    SmartDashboard.putNumber("LimelightY", ty);
    SmartDashboard.putNumber("LimelightArea", ta);
    SmartDashboard.putNumber("LimelightDetection", tv);
    SmartDashboard.putBoolean("Target Centered", isTargetCentered());
    SmartDashboard.putBoolean("Target Detected", isTargetDetected());
    SmartDashboard.putNumber("Distance (INCHES)", limelightDistance());
  }
}
