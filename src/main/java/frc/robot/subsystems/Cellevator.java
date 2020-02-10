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
import static frc.robot.Constants.CellevatorConstants.*;

public class Cellevator extends SubsystemBase {
  private final CANSparkMax m_holderMotor;
  private final CANSparkMax m_loaderMotor;
  private final DigitalInput m_beamBreakHolder;
  private final DigitalInput m_beamBreakLoader;
  private final DigitalInput m_beamBreakMiddle;

  /**
   * Creates a new Cellevator subsystem
   */
  public Cellevator() {

    m_holderMotor = new CANSparkMax(kHolderMotorPort, MotorType.kBrushed);
    m_loaderMotor = new CANSparkMax(kLoaderMotorPort, MotorType.kBrushed);
    m_beamBreakHolder = new DigitalInput(kBeamBreakHolderPort);
    m_beamBreakLoader = new DigitalInput(kBeamBreakLoaderPort);
    m_beamBreakMiddle = new DigitalInput(kBeamBreakMiddlePort);

    // intializes the motors
    motorInit(m_holderMotor, kHolderMotorInverted);
    motorInit(m_loaderMotor, kLoaderMotorInverted);
  }

  private void motorInit(CANSparkMax motor, boolean invert) {
    motor.restoreFactoryDefaults(); // Reset settings in motor in case they are changed
    motor.setIdleMode(IdleMode.kBrake); // Sets the motors to brake mode from the beginning
    motor.setInverted(invert); // Inverts the motor if needed
    motor.setSmartCurrentLimit(kCellevatorCurrentLimit);
  }

  /**
   * Runs the loader motors
   * 
   * @param speed The speed to run the Loader motors
   */
  public void runLoaderMotor(double speed) {
    m_loaderMotor.set(speed);
  }

  /**
   * Runs the holder motors
   * 
   * @param speed The speed to run the Holder motors
   */
  public void runHolderMotor(double speed) {
    m_holderMotor.set(speed);
  }

  /**
   * Gets the beam break value to see if there is a power cell present at the top
   * of the cellavator
   */
  public boolean isTopBallPresent() {
    return m_beamBreakHolder.get();
  }

  /**
   * Gets the beam break value to see if there is a power cell present at the
   * middle of the cellavator
   */
  public boolean isMiddleBallPresent() {
    return m_beamBreakMiddle.get();
  }

  /**
   * Gets the beam break value to see if there is a power cell present at the
   * bottom of the cellavator
   */
  public boolean isBottomBallPresent() {
    return m_beamBreakLoader.get();
  }

  /**
   * Stops the loader motor
   */
  public void stopLoaderMotor() {
    runLoaderMotor(0);
  }

  /**
   * Stops the holder motor
   */
  public void stopHolderMotor() {
    runHolderMotor(0);
  }

  /**
   * Stops both the loader motor and holder motor
   */
  public void stopAllMotors() {
    stopLoaderMotor();
    stopHolderMotor();
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
