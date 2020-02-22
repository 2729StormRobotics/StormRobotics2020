/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.analog.adis16470.frc.ADIS16470_IMU;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import static frc.robot.Constants.DriveConstants.*;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;

public class Drivetrain extends SubsystemBase {

  // Creates variables for motor groups and differential drive

  private final CANSparkMax m_leftMotor1;
  private final CANSparkMax m_leftMotor2;
  private final CANSparkMax m_rightMotor1;
  private final CANSparkMax m_rightMotor2;

  private final DifferentialDrive m_drive;

  // Creates variables for encoders
  private final CANEncoder m_leftMotorEncoder1;
  private final CANEncoder m_leftMotorEncoder2;
  private final CANEncoder m_rightMotorEncoder1;
  private final CANEncoder m_rightMotorEncoder2;

  private final SimpleMotorFeedforward m_feedForward;

  //pid controller
  private final CANPIDController m_pidControllerLeft;
  private final CANPIDController m_pidControllerRight;

  // Declare the gyro.
  private final ADIS16470_IMU m_imu;

  private boolean m_highGear = false;

  private final Solenoid m_gearShift;

  // Add the Network Table for the limelight
  private final NetworkTable m_limelightTable;
  
  // Create variables for the different values given from the limelight
  private double xOffset; // Positive values mean that target is to the right of the camera; negative
                          // values mean target is to the left. Measured in degrees
  private double targetValue; // Sends 1 if a target is detected, 0 if none are present

  // Creates a new Drivetrain.
  public Drivetrain() {

    // Sets variables equal to their ports and types
    m_leftMotor1 = new CANSparkMax(kLeftDriveMotor1Port, MotorType.kBrushless);
    m_leftMotor2 = new CANSparkMax(kLeftDriveMotor2Port, MotorType.kBrushless);
    m_rightMotor1 = new CANSparkMax(kRightDriveMotor1Port, MotorType.kBrushless);
    m_rightMotor2 = new CANSparkMax(kRightDriveMotor2Port, MotorType.kBrushless);

    motorInit(m_rightMotor1, kRightSideInverted);
    motorInit(m_rightMotor2, kRightSideInverted);
    motorInit(m_leftMotor1, kLeftSideInverted);
    motorInit(m_leftMotor2, kLeftSideInverted);

    m_rightMotor2.follow(m_rightMotor1);
    m_leftMotor2.follow(m_leftMotor1);

    //PID controllers for left and right side
    m_pidControllerLeft = m_leftMotor1.getPIDController();
    m_pidControllerRight = m_rightMotor1.getPIDController();

    m_drive = new DifferentialDrive(m_leftMotor1, m_rightMotor1);
    m_drive.setRightSideInverted(false);

    m_leftMotorEncoder1 = m_leftMotor1.getEncoder();
    m_leftMotorEncoder2 = m_leftMotor2.getEncoder();
    m_rightMotorEncoder1 = m_rightMotor1.getEncoder();
    m_rightMotorEncoder2 = m_rightMotor2.getEncoder();

    m_feedForward = new SimpleMotorFeedforward(kS, kV, kA);

    // Instantiate the gyro.
    m_imu = new ADIS16470_IMU();

    m_gearShift = new Solenoid(kDriveSolenoid);

    addChild("Drivetrain", m_drive);
    addChild("Shift Gears", m_gearShift);
    addChild("Gyro", m_imu);

    SmartDashboard.putNumber("Drive P", DriveDistancePID.kP);
    SmartDashboard.putNumber("Drive I", DriveDistancePID.kI);
    SmartDashboard.putNumber("Drive D", DriveDistancePID.kD);
    //assign values with PID controllers
    m_pidControllerLeft.setP(SmartDashboard.getNumber("Drive P", 0));
    m_pidControllerRight.setP(SmartDashboard.getNumber("Drive P", 0));
    m_pidControllerLeft.setI(SmartDashboard.getNumber("Drive I", 0));
    m_pidControllerRight.setI(SmartDashboard.getNumber("Drive I", 0));
    m_pidControllerLeft.setD(SmartDashboard.getNumber("Drive D", 0));
    m_pidControllerRight.setD(SmartDashboard.getNumber("Drive D", 0));
    
    // Set a member variable for the limelight network table
    m_limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

    SmartDashboard.putNumber("Left Target", 0);
    SmartDashboard.putNumber("Right Target", 0);
  }

  /**
   * Returns a value of the offset on the x-axis of the camera to the target in
   * degrees. Negative values mean the target is to the left of the camera
   */
  public double getXOffset() {
    return xOffset;
  }

  /**
   * Returns true if a target is detected
   */
  public boolean isTargetDetected() {
    return (targetValue > 0.0);

  }

  private void motorInit(CANSparkMax motor, boolean invert) {
    motor.restoreFactoryDefaults(); // Resets settings in motor in case they are changed
    motor.setIdleMode(IdleMode.kBrake); // Sets the motors to brake mode from the beginning
    motor.setSmartCurrentLimit(kDrivetrainCurrentLimit);
    motor.setInverted(invert);

    encoderInit(motor.getEncoder()); // Initializes encoder within motor
  }

  

  public void setDriveStates(TrapezoidProfile.State left, TrapezoidProfile.State right) {
    m_pidControllerLeft.setReference(left.position, ControlType.kPosition, 0, m_feedForward.calculate(left.velocity));
    SmartDashboard.putNumber("Left Target", left.position);
    m_pidControllerRight.setReference(right.position, ControlType.kPosition, 0, m_feedForward.calculate(right.velocity));
    SmartDashboard.putNumber("Right Target", right.position);
  }



  private void encoderInit(CANEncoder encoder) {
    // Converts the input into desired value for distance and velocity
    if (m_highGear) {
      encoder.setPositionConversionFactor(kHighGearDistancePerPulse);
      encoder.setVelocityConversionFactor(kHighGearSpeedPerPulse);
    }
    else {
      encoder.setPositionConversionFactor(kLowGearDistancePerPulse);
      encoder.setVelocityConversionFactor(kLowGearSpeedPerPulse);
    }
    encoderReset(encoder);
  }

  private void encoderReset(CANEncoder encoder) {
    encoder.setPosition(0);
  }

  public void resetLeftEncoders() {
    // Resets all left-side drive encoders
    encoderReset(m_leftMotorEncoder1);
    encoderReset(m_leftMotorEncoder2);
  }

  public void resetRightEncoders() {
    // Resets all right-side drive encoders
    encoderReset(m_rightMotorEncoder1);
    encoderReset(m_rightMotorEncoder2);
  }

  // Resets all drive encoders
  public void resetAllEncoders() {
    resetLeftEncoders();
    resetRightEncoders();
  }

  /**
   * Resets the gyro to a heading of zero degrees.
   */
  public void resetGyro() {
    m_imu.reset();
  }

  // Gets left side distance by averaging left encoders
  private double getLeftEncoderAverage() {
    return (m_leftMotorEncoder1.getPosition() + m_leftMotorEncoder2.getPosition()) / 2.0;
  }

  // Gets right side distance by averaging right encoders
  private double getRightEncoderAverage() {
    return (m_rightMotorEncoder1.getPosition() + m_rightMotorEncoder2.getPosition()) / 2.0;
  }

  // Gets average distance by averaging the right and left distance
  public double getAverageDistance() {
    return (getRightEncoderAverage() + getLeftEncoderAverage()) / 2.0;
  }

  // Drives the motor using tank drive
  // Change the squareInputs if needed
  public void tankDrive(double leftPower, double rightPower, boolean squareInputs) {
    m_drive.tankDrive(leftPower, rightPower, squareInputs);
  }

  // Drives the motors using arcade drive
  public void arcadeDrive(double speed, double turn, boolean squareInputs) {
    m_drive.arcadeDrive(speed, turn, squareInputs);
  }

  // Drives the motor using trigger drive
  public void triggerDrive(double reverse, double forward, double turn, boolean squareInputs) {
    m_drive.arcadeDrive(forward - reverse, turn, squareInputs);
  }

  // sets motor values to zero to stop
  public void stopDrive() {
    m_drive.tankDrive(0, 0);
  }

  // Activate the shifting pistons to shift into low gear
  public void shiftLow() {
    m_highGear = false;
    shiftGears();
  }

  // Deactivate the shifting positions to shift into high gear
  public void shiftHigh() {
    m_highGear = true;
    shiftGears();
  }

  // Method to handle updating the pistons and encoder conversions
  private void shiftGears() {
    m_gearShift.set(m_highGear);
    encoderInit(m_leftMotorEncoder1);
    encoderInit(m_leftMotorEncoder2);
    encoderInit(m_rightMotorEncoder1);
    encoderInit(m_rightMotorEncoder2);
  }

  // gets the speed from the left motors
  private double getLeftSpeed() {
    return (m_leftMotorEncoder1.getVelocity() + m_leftMotorEncoder2.getVelocity()) / 2.0;
  }

  // gets the speed from the left motors
  private double getRightSpeed() {
    return (m_rightMotorEncoder1.getVelocity() + m_rightMotorEncoder2.getVelocity()) / 2.0;
  }

  // gets the speed by averaging the left and the right speeds
  public double getAverageSpeed() {
    return (getRightSpeed() + getLeftSpeed()) / 2.0;
  }

  /**
   * Get the orientation of the robot.
   * 
   * @return The angle that the robot is facing in degrees.
   */
  public double getRobotAngle() {
    return m_imu.getAngle();
  }

  // puts data on the SmartDashboard
  public void log() {
    SmartDashboard.putBoolean("High Gear", m_highGear);
    SmartDashboard.putNumber("Left Speed", getLeftSpeed());
    SmartDashboard.putNumber("Right Speed", getRightSpeed());
    SmartDashboard.putNumber("Left Distance", getLeftEncoderAverage());
    SmartDashboard.putNumber("Right Distance", getRightEncoderAverage());
    SmartDashboard.putNumber("Robot Angle", getRobotAngle());
    SmartDashboard.putNumber("X offset", getXOffset());

    // Updates the values of the limelight on the network table
    if (isTargetDetected()) {
      xOffset = m_limelightTable.getEntry("tx").getDouble(0.0);
    }
    else {
      xOffset = 45;
    }
    
    targetValue = m_limelightTable.getEntry("tv").getDouble(0.0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    log();
  }

}


