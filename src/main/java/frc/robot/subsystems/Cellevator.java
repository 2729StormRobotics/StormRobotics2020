/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CelevatorConstants;

public class Cellevator extends SubsystemBase {
  /**
   * Creates a new Loading.
   */
  private final CANSparkMax m_holderMotor;
  private final CANSparkMax m_loaderMotor;
  private final DigitalInput m_beamBreakHolder;
  private final DigitalInput m_beamBreakLoader;
  private final DigitalInput m_beamBreakMiddle;


  public Cellevator() {

    
    m_holderMotor = new CANSparkMax(CelevatorConstants.kHolderMotorPort, MotorType.kBrushed);
    m_loaderMotor = new CANSparkMax(CelevatorConstants.kLoaderMotorPort, MotorType.kBrushed);
    m_beamBreakHolder = new DigitalInput(CelevatorConstants.kBeamBreakHolderPort);
    m_beamBreakLoader = new DigitalInput(CelevatorConstants.kBeamBreakLoaderPort);
    m_beamBreakMiddle = new DigitalInput(CelevatorConstants.kBeamBreakMiddlePort);

    // intializes the motors 
    motorInit(m_holderMotor, CelevatorConstants.kHolderMotorInverted);
    motorInit(m_loaderMotor, CelevatorConstants.kLoaderMotorInverted);
    
  }

  private void motorInit(CANSparkMax motor, boolean invert) {
    motor.restoreFactoryDefaults(); // Reset settings in motor in case they are changed
    motor.setIdleMode(IdleMode.kBrake); // Sets the motors to brake mode from the beginning
    motor.setInverted(invert); // Inverts the motor if needed
    motor.setSmartCurrentLimit(CelevatorConstants.kCelevatorCurrentLimit);
  }

  /** 
   * starts the loader motors
  */ 
  public void runLoaderMotor (double speed) {
    m_loaderMotor.set(speed);
  }

  /** 
   * starts the holder motors
  */ 
  public void runHolderMotor (double speed) {
    m_holderMotor.set(speed);
  }

  /** 
   * gets the beam break value to see if there is a power cell present at the top of the cellavator
   */ 
  public boolean isTopBallPresent() {
    return m_beamBreakHolder.get();
    }

  /** 
   * gets the beam break value to see if there is a power cell present at the middle of the cellavator
   */ 
  public boolean isMiddleBallPresent() {
    return m_beamBreakMiddle.get();
  }

  /** 
   * gets the beam break value to see if there is a power cell present at the bottom of the cellavator
   */ 
  public boolean isBottomBallPresent() {
    return m_beamBreakLoader.get();
  }

  /** 
   * stops both the loader motor and holder motor
   */ 
  public void stopAllMotors() {
    m_holderMotor.set(0);
    m_loaderMotor.set(0);
  }

  /** 
   * only stops the loader motor 
   */ 
  public void stopLoaderMotor() {
    runLoaderMotor(0);
  }

  /** 
   * only stops the holder motor
   */ 
  public void stopHolderMotor() {
    runHolderMotor(0);
  }

  /** 
   * displays data onto SmartDashboard
  */ 
  public void log() {
    SmartDashboard.putBoolean("Holder Beam Value", m_beamBreakHolder.get()); 
    SmartDashboard.putBoolean("Loader Beam Value", m_beamBreakLoader.get());
    SmartDashboard.putBoolean("Middle Beam Value", m_beamBreakMiddle.get());
    SmartDashboard.putNumber("Holder Motor Speed", m_holderMotor.get());
    SmartDashboard.putNumber("Loader Motor Speed", m_loaderMotor.get());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    log();
  }
}
