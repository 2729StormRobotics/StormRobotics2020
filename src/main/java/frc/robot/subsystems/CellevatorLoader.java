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

public class CellevatorLoader extends SubsystemBase {

  private final DigitalInput m_beamBreakBottom;
  private final CANSparkMax m_loaderMotor;
  private final DigitalInput m_beamBreakMiddle;
  private boolean previousBBMiddle;

  /**
   * Creates a new CellevatorLoader.
   */
  public CellevatorLoader() {

    m_loaderMotor = new CANSparkMax(kLoaderMotorPort, MotorType.kBrushed);
    m_beamBreakBottom = new DigitalInput(kBeamBreakLoaderPort);
    m_beamBreakMiddle = new DigitalInput(kBeamBreakMiddlePort);
    previousBBMiddle = false;

    // intializes the motor
    motorInit(m_loaderMotor, kLoaderMotorInverted);
  }

  private void motorInit(CANSparkMax motor, boolean invert) {
    motor.restoreFactoryDefaults(); // Reset settings in motor in case they are changed
    motor.setIdleMode(IdleMode.kBrake); // Sets the motors to brake mode from the beginning
    motor.setInverted(invert); // Inverts the motor if needed
    motor.setSmartCurrentLimit(kCellevatorCurrentLimit);
  }

  /**
   * Runs the loader motor
   * 
   * @param speed The speed to run the loader motor
   */
  public void runLoaderMotor(double speed) {
    m_loaderMotor.set(speed);
  }

  /** 
   * inverts the motor by setting it to the opposite of te state in constants
  */
  public void invertLoader() {
    m_loaderMotor.setInverted(!m_loaderMotor.getInverted());
  }

    /**
   * Gets the beam break value to see if there is a power cell present at the
   * bottom of the cellevator
   */
  public boolean isBottomBallPresent() {
    return !m_beamBreakBottom.get();
  }

  /**
   * Stops the loader motor
   */
  public void stopLoaderMotor() {
    runLoaderMotor(0);
  }

 /**
   * Gets the beam break value to see if there is a power cell present at the
   * middle of the cellevator
   */
  public boolean isMiddleGapClear() {
    return m_beamBreakMiddle.get();
    }

  /**
   * returns the value of the beam break middle value before the current value
   * @return
   */
  public boolean getBeamBreakMiddlePrevious() {
    return previousBBMiddle;
  }

  /**
   * displays data onto SmartDashboard
   */
  public void log() {
    SmartDashboard.putNumber("Loader Motor Speed", m_loaderMotor.get());

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    log();
  }
}
