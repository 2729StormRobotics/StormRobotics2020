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

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.DriveConstants.*;

import java.util.Map;

public class Drivetrain extends SubsystemBase {
  // Creates variables for motor groups and differential drive

  private final CANSparkMax m_leftMotorLeader;
  private final CANSparkMax m_leftMotorFollower;
  private final CANSparkMax m_rightMotorLeader;
  private final CANSparkMax m_rightMotorFollower;

  private final DifferentialDrive m_drive;

  // Creates variables for encoders
  private final CANEncoder m_leftEncoder;
  private final CANEncoder m_rightEncoder;

  private final SimpleMotorFeedforward m_leftFeedforward;
  private final SimpleMotorFeedforward m_rightFeedforward;

  // pid controller
  private final CANPIDController m_pidControllerLeft;
  private final CANPIDController m_pidControllerRight;

  // Declare the gyro.
  private final ADIS16470_IMU m_imu;

  private boolean m_highGear = false;
  private boolean m_reverseDrive = false;

  private final Solenoid m_gearShift;

  private final ShuffleboardTab m_drivetrainTab;
  private final ShuffleboardLayout m_drivetrainStatus;

  // Add the Network Table for the limelight
  private final NetworkTable m_limelight;

  // Create variables for the different values given from the limelight
  private double m_xOffset = 0; // Positive values mean that target is to the right of the camera; negative
  // values mean target is to the left. Measured in degrees
  private boolean m_targetVisible = false;

  private final NetworkTable m_PartyTable;
  private final NetworkTableEntry m_HighGearStatus;
  private final NetworkTableEntry m_LowGearStatus;

  /**
   * Creates a new Drivetrain.
   */
  public Drivetrain() {
    // Sets variables equal to their ports and types
    m_leftMotorLeader = new CANSparkMax(kLeftMotor1Port, MotorType.kBrushless);
    m_leftMotorFollower = new CANSparkMax(kLeftMotor2Port, MotorType.kBrushless);
    m_rightMotorLeader = new CANSparkMax(kRightMotor1Port, MotorType.kBrushless);
    m_rightMotorFollower = new CANSparkMax(kRightMotor2Port, MotorType.kBrushless);

    motorInit(m_rightMotorLeader, kRightReversedDefault);
    motorInit(m_rightMotorFollower, kRightReversedDefault);
    motorInit(m_leftMotorLeader, kLeftReversedDefault);
    motorInit(m_leftMotorFollower, kLeftReversedDefault);

    m_rightMotorFollower.follow(m_rightMotorLeader);
    m_leftMotorFollower.follow(m_leftMotorLeader);

    m_leftEncoder = m_leftMotorLeader.getEncoder();
    m_rightEncoder = m_rightMotorLeader.getEncoder();

    // PID controllers for left and right side
    m_pidControllerLeft = m_leftMotorLeader.getPIDController();
    m_pidControllerRight = m_rightMotorLeader.getPIDController();

    m_drive = new DifferentialDrive(m_leftMotorLeader, m_rightMotorLeader);
    m_drive.setRightSideInverted(false);

    m_gearShift = new Solenoid(kGearShiftChannel);

    m_leftFeedforward = new SimpleMotorFeedforward(kLeftS, kLeftV, kLeftA);
    m_rightFeedforward = new SimpleMotorFeedforward(kRightS, kRightV, kRightA);

    // Instantiate the gyro.
    m_imu = new ADIS16470_IMU();
    resetGyro();

    // Set a member variable for the limelight network table
    m_limelight = NetworkTableInstance.getDefault().getTable("limelight");

    m_drivetrainTab = Shuffleboard.getTab(kShuffleboardTab);
    m_drivetrainStatus = m_drivetrainTab.getLayout("Status", BuiltInLayouts.kList)
        .withProperties(Map.of("Label position", "TOP"));

    shuffleboardInit();
  }

  private void motorInit(CANSparkMax motor, boolean invert) {
    motor.restoreFactoryDefaults(); // Resets settings in motor in case they are changed
    motor.setIdleMode(IdleMode.kBrake); // Sets the motors to brake mode from the beginning
    motor.setSmartCurrentLimit(kCurrentLimit);
    motor.setInverted(invert);
    //motor.setOpenLoopRampRate(2);

    encoderInit(motor.getEncoder()); // Initializes encoder within motor
  }

  private void encoderInit(CANEncoder encoder) {
    // Converts the input into desired value for distance and velocity
    if (m_highGear) {
      encoder.setPositionConversionFactor(kHighDistancePerPulse);
      encoder.setVelocityConversionFactor(kHighSpeedPerPulse);
    } else {
      encoder.setPositionConversionFactor(kLowDistancePerPulse);
      encoder.setVelocityConversionFactor(kLowSpeedPerPulse);
    }
    encoderReset(encoder);
  }

  private void encoderReset(CANEncoder encoder) {
    encoder.setPosition(0);
  }

  public void resetLeftEncoder() {
    // Resets all left-side drive encoders
    encoderReset(m_leftEncoder);
  }

  public void resetRightEncoder() {
    // Resets all right-side drive encoders
    encoderReset(m_rightEncoder);
  }

  // Resets all drive encoders
  public void resetAllEncoders() {
    resetLeftEncoder();
    resetRightEncoder();
  }

  // Gets left side distance by averaging left encoders
  private double getLeftDistance() {
    return m_leftEncoder.getPosition();
  }

  // Gets right side distance by averaging right encoders
  private double getRightDistance() {
    return m_rightEncoder.getPosition();
  }

  // Gets average distance by averaging the right and left distance
  public double getAverageDistance() {
    return (getRightDistance() + getLeftDistance()) / 2.0;
  }

  // gets the speed from the left motors
  private double getLeftSpeed() {
    return m_leftEncoder.getVelocity();
  }

  // gets the speed from the left motors
  private double getRightSpeed() {
    return m_rightEncoder.getVelocity();
  }

  // gets the speed by averaging the left and the right speeds
  public double getAverageSpeed() {
    return (getRightSpeed() + getLeftSpeed()) / 2.0;
  }

  /**
   * Resets the gyro to a heading of zero degrees.
   */
  public void resetGyro() {
    m_imu.reset();
  }

  /**
   * Get the orientation of the robot.
   * 
   * @return The angle that the robot is facing in degrees.
   */
  public double getRobotAngle() {
    return m_imu.getAngle();
  }

  /**
   * Returns a value of the offset on the x-axis of the camera to the target in
   * degrees. Negative values mean the target is to the left of the camera
   */
  public double getVisionTargetXOffset() {
    return m_xOffset;
  }

  /**
   * Returns true if a target is detected
   */
  public boolean isVisionTargetDetected() {
    return (m_targetVisible);
  }

  private void setLeftDistancePID() {
    m_pidControllerLeft.setP(DriveDistancePID.kLeftP, DriveDistancePID.kProfile);
    m_pidControllerLeft.setI(DriveDistancePID.kLeftI, DriveDistancePID.kProfile);
    m_pidControllerLeft.setD(DriveDistancePID.kLeftD, DriveDistancePID.kProfile);
  }

  private void setRightDistancePID() {
    m_pidControllerRight.setP(DriveDistancePID.kRightP, DriveDistancePID.kProfile);
    m_pidControllerRight.setI(DriveDistancePID.kRightI, DriveDistancePID.kProfile);
    m_pidControllerRight.setD(DriveDistancePID.kRightD, DriveDistancePID.kProfile);
  }

  public void setDistancePID() {
    setLeftDistancePID();
    setRightDistancePID();
  }

  // Drives the motor using tank drive
  // Change the squareInputs if needed
  public void tankDrive(double leftPower, double rightPower, boolean squareInputs) {
    if (m_reverseDrive) {
      m_drive.tankDrive(rightPower, leftPower, squareInputs);
    }
    else {
      m_drive.tankDrive(leftPower, rightPower, squareInputs);
    }
  }

  // Drives the motors using arcade drive
  public void arcadeDrive(double speed, double turn, boolean squareInputs) {
    m_drive.arcadeDrive(speed, turn, squareInputs);
  }

  // Drives the motor using trigger drive
  public void triggerDrive(double forward, double reverse, double turn, boolean squareInputs) {
    m_drive.arcadeDrive(forward - reverse, turn, squareInputs);
  }

  // sets motor values to zero to stop
  public void stopDrive() {
    m_drive.tankDrive(0, 0);
  }

  // Activate the shifting pistons to shift into low gear
  public void shiftLow() {
    if (m_highGear) {
      m_highGear = false;
      shiftGears();
      m_LowGearStatus.setBoolean(true);
    }
  }

  // Deactivate the shifting positions to shift into high gear
  public void shiftHigh() {
    if (!m_highGear) {
      m_highGear = true;
      shiftGears();
      m_HighGearStatus.setBoolean(true);
    }
  }

  // Method to handle updating the pistons and encoder conversions
  private void shiftGears() {
    m_gearShift.set(m_highGear);
    encoderInit(m_leftEncoder);
    encoderInit(m_rightEncoder);

    resetAllEncoders();
  }

  // Invert a motor
  private void invertMotor(CANSparkMax motor) {
    motor.setInverted(!motor.getInverted());
  }

  // Reverse Controls
  public void reverseControls() {
    m_reverseDrive = !m_reverseDrive;
    invertMotor(m_leftMotorLeader);
    invertMotor(m_leftMotorFollower);
    invertMotor(m_rightMotorLeader);
    invertMotor(m_rightMotorFollower);

    resetAllEncoders();
  }


  public void setDriveStates(TrapezoidProfile.State left, TrapezoidProfile.State right) {
    m_pidControllerLeft.setReference(left.velocity, ControlType.kVelocity, 0,
        m_leftFeedforward.calculate(left.velocity));
    m_pidControllerRight.setReference(right.velocity, ControlType.kVelocity, 0,
        m_rightFeedforward.calculate(right.velocity));
  }

  // puts data on the SmartDashboard
  private void shuffleboardInit() {
    m_drivetrainStatus.addBoolean("High Gear", () -> m_highGear);
    m_drivetrainStatus.addNumber("Left Speed", () -> getLeftSpeed());
    m_drivetrainStatus.addNumber("Right Speed", () -> getRightSpeed());
    m_drivetrainStatus.addNumber("Left Position", () -> getLeftDistance());
    m_drivetrainStatus.addNumber("Right Position", () -> getRightDistance());
    m_drivetrainStatus.addNumber("Angle", () -> getRobotAngle());
    m_drivetrainStatus.addBoolean("Reversed?", () -> m_reverseDrive);
  }

  public void updateLimelight() {
    // Updates the values of the limelight from the network table
    m_xOffset = m_limelight.getEntry("Target Offset").getDouble(0.0);
    m_targetVisible = m_limelight.getEntry("Target Detection").getBoolean(false);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    updateLimelight();
  }
}
