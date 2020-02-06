/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
 
package frc.robot.subsystems;
 
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.CANPIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LauncherConstants;
 
public class Launcher extends SubsystemBase {
 
  private final CANSparkMax m_leftLauncher;
  private final CANSparkMax m_rightLauncher;
 
  private final CANEncoder m_leftEncoder;
  private final CANEncoder m_rightEncoder;

  private CANPIDController m_pidController;
  
  private double pVal;
  private double iVal;
  private double dVal;
  private double izVal;
  private double ffVal;
  private double minVal;
  private double maxVal;
 
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

    pVal = LauncherConstants.kP;
    iVal = LauncherConstants.kI;
    dVal = LauncherConstants.kD;
    izVal = LauncherConstants.kIz;
    ffVal = LauncherConstants.kFF;
    minVal = LauncherConstants.kMinOutput;
    maxVal = LauncherConstants.kMaxOutput;
   
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

  private void pidInit() {
    m_pidController = m_leftLauncher.getPIDController();

    //set the PID coefficients
    m_pidController.setP(LauncherConstants.kP);
    m_pidController.setI(LauncherConstants.kI);
    m_pidController.setD(LauncherConstants.kD);
    m_pidController.setIZone(LauncherConstants.kIz);
    m_pidController.setFF(LauncherConstants.kFF);
    m_pidController.setOutputRange(LauncherConstants.kMinOutput, LauncherConstants.kMaxOutput);

    // display PID coefficients on SmartDashboard
    SmartDashboard.putNumber("Launcher P Gain", LauncherConstants.kP);
    SmartDashboard.putNumber("Launcher I Gain", LauncherConstants.kI);
    SmartDashboard.putNumber("Launcher D Gain", LauncherConstants.kD);
    SmartDashboard.putNumber("Launcher I Zone", LauncherConstants.kIz);
    SmartDashboard.putNumber("Launcher Feed Forward", LauncherConstants.kFF);
    SmartDashboard.putNumber("Launcher Max Output", LauncherConstants.kMaxOutput);
    SmartDashboard.putNumber("Launcher Min Output", LauncherConstants.kMinOutput);
    SmartDashboard.putNumber("Target Velocity", LauncherConstants.kTargetLaunchVelociy);
    SmartDashboard.putNumber("Left Speed", m_leftEncoder.getVelocity());
    SmartDashboard.putNumber("Right Speed", m_rightEncoder.getVelocity());
    
    double p = SmartDashboard.getNumber("Launcher P Gain", 0);
    double i = SmartDashboard.getNumber("Launcher I Gain", 0);
    double d = SmartDashboard.getNumber("Launcher D Gain", 0);
    double iz = SmartDashboard.getNumber("Launcher I Zone", 0);
    double ff = SmartDashboard.getNumber("Launcher Feed Forward", 0);
    double max = SmartDashboard.getNumber("Launcher Max Output", 0);
    double min = SmartDashboard.getNumber("Launcher Min Output", 0);
    double setpoint = SmartDashboard.getNumber("Target Velocity", 0);
    
    if (p != LauncherConstants.kP) {
      m_pidController.setP(p);
      pVal = p;
    }

    if (i != LauncherConstants.kI) {
      m_pidController.setI(i);
      iVal = i;
    }

    if (d != LauncherConstants.kD) {
      m_pidController.setD(d);
      dVal = d;
    }

    if (iz != LauncherConstants.kIz) {
      m_pidController.setIZone(iz);
      izVal = iz;
    }

    if (ff != LauncherConstants.kFF) {
      m_pidController.setFF(ff);
      ffVal = ff;
    }

    if ((max != LauncherConstants.kMaxOutput) || (min != LauncherConstants.kMinOutput)) {
      m_pidController.setOutputRange(min, max);
      minVal= min;
      maxVal = max;  
    }
    m_pidController.setReference(setpoint, ControlType.kVelocity);

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
