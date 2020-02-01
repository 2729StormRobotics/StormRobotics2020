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
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {

  //Create variables for motor groups and differential drive

  private final CANSparkMax m_leftMotor1;
  private final CANSparkMax m_leftMotor2;
  private final CANSparkMax m_rightMotor1;
  private final CANSparkMax m_rightMotor2;

  private final SpeedControllerGroup m_leftMotors;
  private final SpeedControllerGroup m_rightMotors;

  private final DifferentialDrive m_Drive;

  //Create variables for encoders

  private final CANEncoder m_leftMotorEncoder1;
  private final CANEncoder m_leftMotorEncoder2;
  private final CANEncoder m_rightMotorEncoder1;
  private final CANEncoder m_rightMotorEncoder2;


  /**
   * Creates a new Drivetrain.
   */
  public Drivetrain() {

    // Set variables equal to their ports and types
    m_leftMotor1 = new CANSparkMax(DriveConstants.kLeftMotor1Port, MotorType.kBrushless);
    m_leftMotor2 = new CANSparkMax(DriveConstants.kLeftMotor2Port, MotorType.kBrushless);
    m_rightMotor1 = new CANSparkMax(DriveConstants.kRightMotor1Port, MotorType.kBrushless);
    m_rightMotor2 = new CANSparkMax(DriveConstants.kRightMotor2Port, MotorType.kBrushless);

    m_leftMotors = new SpeedControllerGroup(m_leftMotor1, m_leftMotor2);
    m_rightMotors = new SpeedControllerGroup(m_rightMotor1, m_rightMotor2);

    m_Drive = new DifferentialDrive(m_leftMotors, m_rightMotors);

    m_leftMotorEncoder1 = m_leftMotor1.getEncoder();
    m_leftMotorEncoder2 = m_leftMotor2.getEncoder();
    m_rightMotorEncoder1 = m_rightMotor1.getEncoder();
    m_rightMotorEncoder2 = m_rightMotor2.getEncoder();

    //
    addChild("Left Motors", m_leftMotors);
    addChild("Right Motors", m_rightMotors);
    addChild("Drivetrain", m_Drive);

  }

  private void encoderInit(CANEncoder encoder) {
    // Converts the input into desired value for distance and velocity
    encoder.setPositionConversionFactor(kEncoderDistancePerPulse); // ADD kEncoderDistancePerPulse TO CONSTANTS
    encoder.setVelocityConversionFactor(kEncoderSpeedPerPulse); // ADD kEncoderSpeedPerPulse TO CONSTANTS
    encoderReset(encoder);
  }

  private void motorInit(CANSparkMax motor, boolean invert) {
    motor.restoreFactoryDefaults(); // Reset settings in motor in case they are changed
    motor.setIdleMode(IdleMode.kBrake); // Sets the motors to brake mode from the beginning

    encoderInit(motor.getEncoder()); // Initializes encoder within motor
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
