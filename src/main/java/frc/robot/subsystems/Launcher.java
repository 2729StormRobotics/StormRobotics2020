/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.LauncherConstants.*;

public class Launcher extends SubsystemBase {

  private final CANSparkMax m_leftLauncher;
  private final CANSparkMax m_rightLauncher;

  private final CANEncoder m_leftEncoder;
  private final CANEncoder m_rightEncoder;

  private final CANPIDController m_pidController;

  private final DoubleSolenoid m_pistonAdjustment;

  /**
   * Creates a new Launcher.
   */
  public Launcher() {

    //solenoid port
    m_pistonAdjustment = new DoubleSolenoid(kLeftLauncherMotorPort, kRightLauncherMotorPort);

    // Instantiate the motors.
    m_leftLauncher = new CANSparkMax(kLeftLauncherMotorPort, MotorType.kBrushless);
    m_rightLauncher = new CANSparkMax(kRightLauncherMotorPort, MotorType.kBrushless);

    // Instantiate the encoder.
    m_leftEncoder = m_leftLauncher.getEncoder();
    m_rightEncoder = m_rightLauncher.getEncoder();

    // Initialize the the motors.
    motorInit(m_leftLauncher);
    motorInit(m_rightLauncher);

    // Make right motor follow the left motor and set it to inverted.
    m_rightLauncher.follow(m_leftLauncher, true);

    // Initialize the PID controller for the motor controller.
    m_pidController = m_leftLauncher.getPIDController();

    //initialize pistons
    pistonInit();

  }

  /**
   * Restore factory defaults, set idle mode to coast, and invert motor.
   * 
   * @param motor  The motor to initialize
   * @param invert Whether motor should be inverted
   */
  private void motorInit(CANSparkMax motor) {
    motor.restoreFactoryDefaults(); // Just in case any settings persist between reboots.
    motor.setIdleMode(IdleMode.kCoast); // Set the motor to coast mode, so that we don't lose momentum when we stop
                                        // shooting.
    encoderInit(motor.getEncoder());
  }
 
  //set pistons to default retracted position
  private void pistonInit() {
    setLaunchPiston(false);
  }

  //extends pistons if true
  public void setLaunchPiston(boolean out) {
    m_pistonAdjustment.set(out ? Value.kForward : Value.kReverse);
  }


  /**
   * Initialize an encoder.
   * 
   * @param encoder The encoder to initialize
   */
  private void encoderInit(CANEncoder encoder) {
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

  // sets speed of launcher motors to 0
  public void stopMotors() {
    m_leftLauncher.set(0);
  }

  // get left launcher speed in RPM
  private double getLeftLauncherSpeed() {
    return m_leftEncoder.getVelocity();
  }

  // get right launcher speed in RPM
  private double getRightLauncherSpeed() {
    return m_rightEncoder.getVelocity();
  }

  // get average speed of left and right launchers
  public double getLauncherAvgSpeed() {
    return ((getLeftLauncherSpeed() + getRightLauncherSpeed()) / 2.0);
  }

  public void revToSpeed(double speed) {

  }

  public void stopRevving() {
    stopMotors();
  }

  // add info to the dashboard
  public void log() {
    SmartDashboard.putNumber("Left Launcher Speed(RPM)", m_leftEncoder.getVelocity());
    SmartDashboard.putNumber("Right Launcher Speed(RPM)", m_rightEncoder.getVelocity());
    SmartDashboard.putNumber("Average Launcher Speed(RPM)", getLauncherAvgSpeed());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    log();
  }
}
