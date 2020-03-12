/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import static frc.robot.Constants.VisionConstants.*;

public class Vision extends SubsystemBase {
  // Create variables for the different values given from the limelight

  // Offset along x axis in degrees.
  private double m_xOffset; // Positive values mean that target is to the right of the camera

  // Offset along y axis in degrees
  private double m_yOffset; // Positive values mean that target is above the camera

  // Percentage of the image the target takes
  private double m_targetArea;

  // Is target detected
  private double m_targetValue;

  private double[] m_targetCoordinatesRaw;

  private double m_topLeftX;
  private double m_topLeftY;
  private double m_topRightX;
  private double m_topRightY;

  // Create a network table for the limelight
  private final NetworkTable m_limelightTable;

  // Create new network table entries for target detection
  private final NetworkTableEntry m_targetDistance;
  private final NetworkTableEntry m_targetDetection;
  private final NetworkTableEntry m_targetOffset;
  private final NetworkTableEntry m_targetCorners;

  /**
   * Creates a new Vision.
   */
  public Vision() {
    // Gets the network table for the limelight
    m_limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

    // Reset the default settings and pipelines to the Limelight
    setPipeline(kDefaultPipeline);

    // Initialize the network table entries for distance and target detection to
    // default values
    m_targetDistance = m_limelightTable.getEntry("Target Distance");
    m_targetDetection = m_limelightTable.getEntry("Target Detection");
    m_targetOffset = m_limelightTable.getEntry("Target Offset");
    m_targetCorners = m_limelightTable.getEntry("Target Corners");
  }

  public void disableLED() {
    m_limelightTable.getEntry("ledMode").setNumber(1);
  }

  public void enableLED() {
    m_limelightTable.getEntry("ledMode").setNumber(3);
  }

  /**
   * 
   * Returns a value of the offset on the x-axis of the camera to the target in
   * degrees. Negative values mean the target is to the left of the camera
   */
  public double getXOffset() {
    return m_xOffset;
  }

  /**
   * Returns true if a target is detected
   */
  public boolean isTargetDetected() {
    return (m_targetValue > 0.0);
  }

  /**
   * Returns true if the target is within a range of the center crosshair of the
   * camera
   */
  public boolean isTargetCentered() {
    return (isTargetDetected() && (getXOffset() > -1.5) && (getXOffset() < 1.5));
  }

  /**
   * Calculates the total angle by adding the mounting angle with the y-axis
   * offset angle of the limelight in degrees
   */
  public double getTargetAngle() {
    return (kLimelightAngle + m_yOffset);
  }

  public double getTargetArea() {
    return m_targetArea;
  }

  /**
   * Return the distance from the limelight to the target in inches (floor
   * distance)
   */
  public double getTargetDistance() {
    return (kPortHeight - kLimelightHeight) / Math.tan(Math.toRadians(getTargetAngle()));
  }

  private double[] getTargetCornersRaw() {
    return m_targetCoordinatesRaw;
  }

  private void getTopLeftCorners() {
    double[] target = getTargetCornersRaw();
    int min = 0;
    for (int i = 2; i < target.length; i += 2) {
      if (target[i] < target[min]) {
        min = i;
      }
    }
    m_topLeftX = getTargetAngleX(target[min]);
    m_topLeftY = getTargetAngleY(target[min+1]);
  }

  private void getTopRightCorners() {
    double[] target = getTargetCornersRaw();
    int max = 0;
    for (int i = 2; i < target.length; i += 2) {
      if (target[i] > target[max]) {
        max = i;
      }
    }
    m_topRightX = getTargetAngleX(target[max]);
    m_topRightY = getTargetAngleY(target[max+1]);
  }

  private void updateTopCorners() {
    getTopLeftCorners();
    getTopRightCorners();
  }

  private double getMidPoint(double x1, double x2) {
    double midpoint = 0.5 * (x2 - x1) + x1;
    return midpoint;
  }

  private void updateMidpointX() {
    m_xOffset = getMidPoint(m_topLeftX, m_topRightX);
  }

  private void updateMidpointY() {
    m_yOffset = getMidPoint(m_topLeftY, m_topRightY);
  }

  private void updateMidpoint() {
    updateMidpointX();
    updateMidpointY();
  }

  private double getNormalizedX(double x) {
    return (1 / 160) * (x - 160);
  }

  private double getNormalizedY(double y) {
    return (1 / 120) * (120 - y);
  }

  private double getViewplaneX(double x) {
    return Math.tan(Math.toRadians(29.8)) * getNormalizedX(x);
  }

  private double getViewplaneY(double y) {
    return Math.tan(Math.toRadians(24.85)) * getNormalizedY(y);
  }

  public double getTargetAngleX(double x) {
    return Math.toDegrees(Math.atan2(getViewplaneX(x), 1));
  }

  public double getTargetAngleY(double y) {
    return Math.toDegrees(Math.atan2(getViewplaneY(y), 1));
  }

  /**
   * Chooses which pipeline to use on the limelight and prevents invalid values
   * from being sent
   * 
   * @param pipeline Which pipeline to use on the limelight (0-9)
   */
  public void setPipeline(int pipeline) {
    if (pipeline < 0) {
      pipeline = 0;
    } else if (pipeline > 9) {
      pipeline = 9;
    }

    m_limelightTable.getEntry("pipeline").setNumber(pipeline);
  }

  /**
   * Returns the value of the pipeline from the network table
   * 
   * @return pipelineValue
   */
  public double getPipeline() {
    double pipeline = m_limelightTable.getEntry("pipeline").getDouble(0.0);
    return pipeline;
  }

  public void updateLimelight() {
    // Updates the values of the limelight on the network table
    m_targetArea = m_limelightTable.getEntry("ta").getDouble(0.0);
    m_targetValue = m_limelightTable.getEntry("tv").getDouble(0.0);
    m_targetCoordinatesRaw = m_limelightTable.getEntry("tcornxy").getDoubleArray(new double[0]);

    // Updates the values of the math for target distance and value to the network
    // table
    m_targetDistance.setDouble(getTargetDistance());
    m_targetDetection.setBoolean(isTargetDetected());
    m_targetOffset.setDouble(getXOffset());
    m_targetCorners.setDoubleArray(getTargetCornersRaw());

    updateTopCorners();
    updateMidpoint();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    updateLimelight();
  }
}
