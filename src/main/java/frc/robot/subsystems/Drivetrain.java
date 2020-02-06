/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.Solenoid;

public class Drivetrain extends SubsystemBase {

  // Creates variables for motor groups and differential drive

  private final CANSparkMax m_leftMotor1;
  private final CANSparkMax m_leftMotor2;
  private final CANSparkMax m_rightMotor1;
  private final CANSparkMax m_rightMotor2;

  private final SpeedControllerGroup m_leftMotors;
  private final SpeedControllerGroup m_rightMotors;

  private final DifferentialDrive m_Drive;

  // Creates variables for encoders

  private final CANEncoder m_leftMotorEncoder1;
  private final CANEncoder m_leftMotorEncoder2;
  private final CANEncoder m_rightMotorEncoder1;
  private final CANEncoder m_rightMotorEncoder2;

  private boolean m_lowGear = false;

  private String m_driveType = "Tank"; // adds a String object to store the drive type in
  SendableChooser<String> m_chooser = new SendableChooser<>();

  private final Solenoid gearShift;

  // Creates a new Drivetrain.
  public Drivetrain() {

    // Sets variables equal to their ports and types
    m_leftMotor1 = new CANSparkMax(DriveConstants.kLeftDriveMotor1Port, MotorType.kBrushless);
    m_leftMotor2 = new CANSparkMax(DriveConstants.kLeftDriveMotor2Port, MotorType.kBrushless);
    m_rightMotor1 = new CANSparkMax(DriveConstants.kRightDriveMotor1Port, MotorType.kBrushless);
    m_rightMotor2 = new CANSparkMax(DriveConstants.kRightDriveMotor2Port, MotorType.kBrushless);

    motorInit(m_rightMotor1, DriveConstants.kRightEncoderInverted);
    motorInit(m_rightMotor2, DriveConstants.kRightEncoderInverted);
    motorInit(m_leftMotor1, DriveConstants.kLeftEncoderInverted);
    motorInit(m_leftMotor2, DriveConstants.kLeftEncoderInverted);
    m_leftMotors = new SpeedControllerGroup(m_leftMotor1, m_leftMotor2);
    m_rightMotors = new SpeedControllerGroup(m_rightMotor1, m_rightMotor2);

    m_Drive = new DifferentialDrive(m_leftMotors, m_rightMotors);

    // adds options to the drive type
    m_chooser.addOption("Tank Drive", "Tank");
    m_chooser.addOption("Arcade Drive", "Arcade");
    m_chooser.addOption("Trigger Drive", "Trigger");

    m_leftMotorEncoder1 = m_leftMotor1.getEncoder();
    m_leftMotorEncoder2 = m_leftMotor2.getEncoder();
    m_rightMotorEncoder1 = m_rightMotor1.getEncoder();
    m_rightMotorEncoder2 = m_rightMotor2.getEncoder();

    gearShift = new Solenoid(DriveConstants.kDriveSolenoid);

    addChild("Left Motors", m_leftMotors);
    addChild("Right Motors", m_rightMotors);
    addChild("Drivetrain", m_Drive);
    addChild("Shift Gears", gearShift);

  }

  private void encoderInit(CANEncoder encoder) {
    // Converts the input into desired value for distance and velocity
    encoder.setPositionConversionFactor(DriveConstants.kEncoderDistancePerPulse);
    encoder.setVelocityConversionFactor(DriveConstants.kEncoderSpeedPerPulse);
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

  private void motorInit(CANSparkMax motor, boolean invert) {
    motor.restoreFactoryDefaults(); // Resets settings in motor in case they are changed
    motor.setIdleMode(IdleMode.kBrake); // Sets the motors to brake mode from the beginning
    motor.setSmartCurrentLimit(DriveConstants.kDrivetrainCurrentLimit);
    motor.setInverted(invert);

    encoderInit(motor.getEncoder()); // Initializes encoder within motor
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
    m_Drive.tankDrive(leftPower, rightPower);
  }

  // Drives the motors using arcade drive
  public void arcadeDrive(double speed, double turn, boolean squareInputs) {
    m_Drive.arcadeDrive(speed, turn, true);
  }

  // Drives the motor using trigger drive
  public void triggerDrive(double reverse, double forward, double turn, boolean squareInputs) {
    m_Drive.arcadeDrive(forward - reverse, turn);
  }

  // sets motor values to zero to stop
  public void stopDrive() {
    m_Drive.tankDrive(0, 0);
  }

  // all motors go at half speed when shiftLow is activated
  public void shiftLow() {
    m_Drive.setMaxOutput(0.5);
    m_lowGear = true;
  }

  // all motors go at full speed when shiftHigh is activated
  public void shiftHigh() {
    m_Drive.setMaxOutput(1.0);
    m_lowGear = false;
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

  // changes the drive type using the SmartDashboard
  private void updateDriveType() {
    m_driveType = m_chooser.getSelected();
  }

  // gets the drive type on the SmartDashboard
  public String getDriveType() {
    return m_driveType;
  }

  // puts data on the SmartDashboard
  public void log() {
    SmartDashboard.putBoolean("Low Gear", m_lowGear);
    SmartDashboard.putNumber("Left Speed", getLeftSpeed());
    SmartDashboard.putNumber("Right Speed", getRightSpeed());
    SmartDashboard.putNumber("Left Distance", getLeftEncoderAverage());
    SmartDashboard.putNumber("Right Distance", getRightEncoderAverage());
    SmartDashboard.putData("Drive Control Type", m_chooser);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    log();
    updateDriveType();
  }
}
