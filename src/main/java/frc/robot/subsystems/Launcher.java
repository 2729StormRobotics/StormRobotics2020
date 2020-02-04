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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
 
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.LauncherConstants;
 
public class Launcher extends SubsystemBase {
 
  private final CANSparkMax m_leftLauncher;
  private final CANSparkMax m_rightLauncher;
 
  private final CANEncoder m_leftEncoder;
  private final CANEncoder m_rightEncoder;
 
  /**
   * Creates a new Launcher.
   */
  public Launcher() {
    // Instantiate the motors.
    m_leftLauncher = new CANSparkMax(LauncherConstants.kLeftLauncherMotorPort, MotorType.kBrushless);
    m_rightLauncher = new CANSparkMax(LauncherConstants.kRightLauncherMotorPort, MotorType.kBrushless);
 
    // Instantiate the encoder.
    m_leftEncoder = m_leftLauncher.getEncoder();
    m_rightEncoder = m_rightLauncher.getEncoder();
 
    // Initialize the the motors.
    motorInit(m_leftLauncher, false);
    motorInit(m_rightLauncher, true);
  }
 
  /**
   * Restore factory defaults, set idle mode to coast, and invert motor.
   * 
   * @param motor The motor to initialize
   * @param invert Whether motor should be inverted
   */
  private void motorInit(CANSparkMax motor, boolean invert) {
    motor.restoreFactoryDefaults(); // Just in case any settings persist between reboots.
    motor.setIdleMode(IdleMode.kCoast); // Set the motor to coast mode, so that we don't lose momentum when we stop shooting.
    motor.setInverted(invert); // Whether or not to invert motor.
    encoderInit(motor.getEncoder());
  }
 
  /**
   * Initialize an encoder.
   * 
   * @param encoder The encoder to initialize
   */
  private void encoderInit(CANEncoder encoder) {
    // Set the automatic conversion factor for the encoder. This allows
    // the encoder to output data in a specified unit.
    encoder.setVelocityConversionFactor(LauncherConstants.kEncoderSpeedPerPulse);
    encoderReset(encoder); // Reset the encoder to 0, just in case
  }
 
  /**
   * Reset an encoder's position.
   * 
   * @param encoder The encoder to reset
   */
  private void encoderReset(CANEncoder encoder) {
    encoder.setPosition(0);
  }
 
  //sets speed of launcher motors to 0
  private void stopMotors(CANSparkMax leftLauncher, CANSparkMax rightLauncher) {
    leftLauncher.set(0);
    rightLauncher.set(0);
  }
 
  //get left speed in RPM
  private double getLeftSpeed() {
    return m_leftEncoder.getVelocity();
  }
 
  //get right speed in RPM
  private double getRightSpeed() {
    return m_rightEncoder.getVelocity();
  }
 
  //get average speed
  private double getSpeed() {
    return ((getLeftSpeed() + getRightSpeed()) / 2.0);
  }
 
  //add info to the dashboard
  public void log() {
    SmartDashboard.putNumber("Left Launcher Speed(RPM)", m_leftEncoder.getVelocity());
    SmartDashboard.putNumber("Right Launcher Speed(RPM)", m_rightEncoder.getVelocity());
    SmartDashboard.putNumber("Average Launcher Speed(RPM)", getSpeed());
  }
 
  
 
 
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
