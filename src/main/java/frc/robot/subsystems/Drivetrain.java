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
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
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
  private final CANEncoder m_leftMotorEncoder;
  private final CANEncoder m_rightMotorEncoder;

  private final SimpleMotorFeedforward m_leftFeedforward;
  private final SimpleMotorFeedforward m_rightFeedforward;

  // pid controller
  private final CANPIDController m_pidControllerLeft;
  private final CANPIDController m_pidControllerRight;

  // Declare the gyro.
  private final ADIS16470_IMU m_imu;

  private boolean m_highGear = false;

  private final Solenoid m_gearShift;

  private final ShuffleboardTab m_drivetrainTab;
  private final ShuffleboardLayout m_drivetrainLeft;
  private final ShuffleboardLayout m_drivetrainRight;
  private final ShuffleboardLayout m_drivetrainStatus;
  private final ShuffleboardLayout m_autoTestValues;
  private final ShuffleboardLayout m_drivetrainTurn;

  private NetworkTableEntry m_testDistanceTarget;
  private NetworkTableEntry m_testDistanceLeftP;
  private NetworkTableEntry m_testDistanceLeftI;
  private NetworkTableEntry m_testDistanceLeftD;
  private NetworkTableEntry m_testDistanceRightP;
  private NetworkTableEntry m_testDistanceRightI;
  private NetworkTableEntry m_testDistanceRightD;
  private double m_leftTrapPosition = 0;
  private double m_leftTrapSpeed = 0;
  private double m_rightTrapPosition = 0;
  private double m_rightTrapSpeed = 0;

  private NetworkTableEntry m_testAngleTarget;
  private NetworkTableEntry m_testAngleP;
  private NetworkTableEntry m_testAngleI;
  private NetworkTableEntry m_testAngleD;

  // Add the Network Table for the limelight
  private final NetworkTable m_limelightTable;

  // Create variables for the different values given from the limelight
  private double m_xOffset; // Positive values mean that target is to the right of the camera; negative
  // values mean target is to the left. Measured in degrees
  private double m_targetValue; // Sends 1 if a target is detected, 0 if none are present

  /**
   * Creates a new Drivetrain.
   */
  public Drivetrain() {
    // Sets variables equal to their ports and types
    m_leftMotorLeader = new CANSparkMax(kLeftMotor1Port, MotorType.kBrushless);
    m_leftMotorFollower = new CANSparkMax(kLeftMotor2Port, MotorType.kBrushless);
    m_rightMotorLeader = new CANSparkMax(kRightMotor1Port, MotorType.kBrushless);
    m_rightMotorFollower = new CANSparkMax(kRightMotor2Port, MotorType.kBrushless);

    motorInit(m_rightMotorLeader, kRightMotorsReversed);
    motorInit(m_rightMotorFollower, kRightMotorsReversed);
    motorInit(m_leftMotorLeader, kLeftMotorsReversed);
    motorInit(m_leftMotorFollower, kLeftMotorsReversed);

    m_rightMotorFollower.follow(m_rightMotorLeader);
    m_leftMotorFollower.follow(m_leftMotorLeader);

    m_leftMotorEncoder = m_leftMotorLeader.getEncoder();
    m_rightMotorEncoder = m_rightMotorLeader.getEncoder();

    // PID controllers for left and right side
    m_pidControllerLeft = m_leftMotorLeader.getPIDController();
    m_pidControllerRight = m_rightMotorLeader.getPIDController();

    m_drive = new DifferentialDrive(m_leftMotorLeader, m_rightMotorLeader);
    m_drive.setRightSideInverted(false);

    m_gearShift = new Solenoid(kGearShiftPort);

    m_leftFeedforward = new SimpleMotorFeedforward(kLeftS, kLeftV, kLeftA);
    m_rightFeedforward = new SimpleMotorFeedforward(kRightS, kRightV, kRightA);

    // Instantiate the gyro.
    m_imu = new ADIS16470_IMU();
    resetGyro();

    // Set a member variable for the limelight network table
    m_limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

    m_drivetrainTab = Shuffleboard.getTab(kShuffleboardTab);
    m_drivetrainLeft = m_drivetrainTab.getLayout("Left", BuiltInLayouts.kList)
        .withProperties(Map.of("Label position", "TOP"));
    m_drivetrainRight = m_drivetrainTab.getLayout("Right", BuiltInLayouts.kList)
        .withProperties(Map.of("Label position", "TOP"));
    m_drivetrainStatus = m_drivetrainTab.getLayout("Status", BuiltInLayouts.kList)
        .withProperties(Map.of("Label position", "TOP"));
    m_autoTestValues = m_drivetrainTab.getLayout("Test Auto Values", BuiltInLayouts.kList)
        .withProperties(Map.of("Label position", "TOP"));
    m_drivetrainTurn = m_drivetrainTab.getLayout("Turning", BuiltInLayouts.kList)
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
      encoder.setPositionConversionFactor(kHighDriveDistancePerPulse);
      encoder.setVelocityConversionFactor(kHighDriveSpeedPerPulse);
    } else {
      encoder.setPositionConversionFactor(kLowDriveDistancePerPulse);
      encoder.setVelocityConversionFactor(kLowDriveSpeedPerPulse);
    }
    encoderReset(encoder);
  }

  private void encoderReset(CANEncoder encoder) {
    encoder.setPosition(0);
  }

  public void resetLeftEncoder() {
    // Resets all left-side drive encoders
    encoderReset(m_leftMotorEncoder);
  }

  public void resetRightEncoder() {
    // Resets all right-side drive encoders
    encoderReset(m_rightMotorEncoder);
  }

  // Resets all drive encoders
  public void resetAllEncoders() {
    resetLeftEncoder();
    resetRightEncoder();
  }

  // Gets left side distance by averaging left encoders
  private double getLeftDistance() {
    return m_leftMotorEncoder.getPosition();
  }

  // Gets right side distance by averaging right encoders
  private double getRightDistance() {
    return m_rightMotorEncoder.getPosition();
  }

  // Gets average distance by averaging the right and left distance
  public double getAverageDistance() {
    return -(getRightDistance() + getLeftDistance()) / 2.0;
  }

  // gets the speed from the left motors
  private double getLeftSpeed() {
    return m_leftMotorEncoder.getVelocity();
  }

  // gets the speed from the left motors
  private double getRightSpeed() {
    return m_rightMotorEncoder.getVelocity();
  }

  // gets the speed by averaging the left and the right speeds
  public double getAverageSpeed() {
    return -(getRightSpeed() + getLeftSpeed()) / 2.0;
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
    return (m_targetValue > 0.0);
  }

  public NetworkTableEntry getTargetDistance() {
    return m_testDistanceTarget;
  }

  private void setLeftPID() {
    m_pidControllerLeft.setP(m_testDistanceLeftP.getDouble(DriveDistance.kLeftP), DriveDistance.kProfile);
    m_pidControllerLeft.setI(m_testDistanceLeftI.getDouble(DriveDistance.kLeftI), DriveDistance.kProfile);
    m_pidControllerLeft.setD(m_testDistanceLeftD.getDouble(DriveDistance.kLeftD), DriveDistance.kProfile);
  }

  private void setRightPID() {
    m_pidControllerRight.setP(m_testDistanceRightP.getDouble(DriveDistance.kRightP), DriveDistance.kProfile);
    m_pidControllerRight.setI(m_testDistanceRightI.getDouble(DriveDistance.kRightI), DriveDistance.kProfile);
    m_pidControllerRight.setD(m_testDistanceRightD.getDouble(DriveDistance.kRightD), DriveDistance.kProfile);
  }

  public double getTurnP() {
    return m_testAngleP.getDouble(PointTurnPID.kP);
  }

  public double getTurnI() {
    return m_testAngleI.getDouble(PointTurnPID.kI);
  }

  public double getTurnD() {
    return m_testAngleD.getDouble(PointTurnPID.kD);
  }

  public double getDistanceP() {
    return m_testDistanceLeftP.getDouble(DriveDistance.kLeftP);
  }

  public double getDistanceI() {
    return m_testDistanceLeftI.getDouble(DriveDistance.kLeftI);
  }

  public double getDistanceD() {
    return m_testDistanceLeftD.getDouble(DriveDistance.kLeftD);
  }

  public NetworkTableEntry getTurnTarget() {
    return m_testAngleTarget; 
  }

  public NetworkTableEntry getDistanceTarget() {
    return m_testDistanceTarget;
  }

  public void pidAdjustDistance() {
    setLeftPID();
    setRightPID();
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
    }
  }

  // Deactivate the shifting positions to shift into high gear
  public void shiftHigh() {
    if (!m_highGear) {
      m_highGear = true;
      shiftGears();
    }
  }

  // Method to handle updating the pistons and encoder conversions
  private void shiftGears() {
    m_gearShift.set(m_highGear);
    encoderInit(m_leftMotorEncoder);
    encoderInit(m_rightMotorEncoder);

    resetAllEncoders();
  }

  public void setDriveStates(TrapezoidProfile.State left, TrapezoidProfile.State right) {
    m_leftTrapPosition = left.position;
    m_leftTrapSpeed = left.velocity;
    m_rightTrapPosition = right.velocity;
    m_rightTrapSpeed = right.velocity;
    m_pidControllerLeft.setReference(left.velocity, ControlType.kVelocity, 0,
        m_leftFeedforward.calculate(left.velocity));
    m_pidControllerRight.setReference(right.velocity, ControlType.kVelocity, 0,
        m_rightFeedforward.calculate(right.velocity));
  }

  // puts data on the SmartDashboard
  private void shuffleboardInit() {
    m_drivetrainStatus.addBoolean("High Gear?", () -> m_highGear);
    m_drivetrainLeft.addNumber("Speed", () -> getLeftSpeed());
    m_drivetrainRight.addNumber("Speed", () -> getRightSpeed());
    m_drivetrainLeft.addNumber("Position", () -> getLeftDistance());
    m_drivetrainRight.addNumber("Position", () -> getRightDistance());
    m_drivetrainTurn.addNumber("Direction", () -> getRobotAngle());
    m_drivetrainLeft.addNumber("Trap Pos", () -> m_leftTrapPosition);
    m_drivetrainRight.addNumber("Trap Pos", () -> m_rightTrapPosition);
    m_drivetrainLeft.addNumber("Trap Speed", () -> m_leftTrapSpeed);
    m_drivetrainRight.addNumber("Trap Speed", () -> m_rightTrapSpeed);

    m_testDistanceTarget = m_autoTestValues.add("Distance", 0).getEntry();

    m_testDistanceLeftP = m_drivetrainLeft.add("Left P", DriveDistance.kLeftP).getEntry();
    m_testDistanceLeftI = m_drivetrainLeft.add("Left I", DriveDistance.kLeftI).getEntry();
    m_testDistanceLeftD = m_drivetrainLeft.add("Left D", DriveDistance.kLeftD).getEntry();

    m_testDistanceRightP = m_drivetrainRight.add("Right P", DriveDistance.kRightP).getEntry();
    m_testDistanceRightI = m_drivetrainRight.add("Right I", DriveDistance.kRightI).getEntry();
    m_testDistanceRightD = m_drivetrainRight.add("Right D", DriveDistance.kRightD).getEntry();

    m_testAngleTarget = m_autoTestValues.add("Angle", 0).getEntry();
    m_testAngleP = m_drivetrainTurn.add("Turn P", PointTurnPID.kP).getEntry();
    m_testAngleI = m_drivetrainTurn.add("Turn I", PointTurnPID.kI).getEntry();
    m_testAngleD = m_drivetrainTurn.add("Turn D", PointTurnPID.kD).getEntry();
  }

  public void updateLimelight() {
    // Updates the values of the limelight from the network table
    m_xOffset = m_limelightTable.getEntry("tx").getDouble(0.0);
    m_targetValue = m_limelightTable.getEntry("tv").getDouble(0.0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    updateLimelight();
  }
}
