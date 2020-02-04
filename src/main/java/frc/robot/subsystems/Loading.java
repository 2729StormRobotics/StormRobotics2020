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
import frc.robot.Constants.LoadingConstants;

public class Loading extends SubsystemBase {
  /**
   * Creates a new Loading.
   */
  private final CANSparkMax m_holderMotor;
  private final CANSparkMax m_loaderMotor;
  private final DigitalInput m_beamBreakHolder;
  private final DigitalInput m_beamBreakLoader;


  public Loading() {

    m_holderMotor = new CANSparkMax(LoadingConstants.kLowCellevatorMotorPort, MotorType.kBrushed);
    m_loaderMotor = new CANSparkMax(LoadingConstants.kHighCellevatorMotor2Port, MotorType.kBrushed);
    m_beamBreakHolder = new DigitalInput(LoadingConstants.kBeamBreakOutput1Port);
    m_beamBreakLoader = new DigitalInput(LoadingConstants.kBeamBreakOutput2Port);

    //intializes the motors 
    motorInit(m_loaderMotor);
    motorInit(m_holderMotor);
    
  }

  private void motorInit(CANSparkMax motor) {
    motor.restoreFactoryDefaults(); // Reset settings in motor in case they are changed
    motor.setIdleMode(IdleMode.kBrake); // Sets the motors to brake mode from the beginning
  }

  // starts the loading motors
  public void startLoaderMotor (double speed) {
    m_loaderMotor.set(speed);
  }

  public void startHolderMotor (double speed) {
    m_holderMotor.set(speed);
  }

  // gets the beam break value to see if there is a power cell present at the top of the cellavator
  public void getHolderBeamBreakValue () {
    m_beamBreakHolder.get();
    }

  // gets the beam break value to see if there is a power cell present at bottom at the cellavator where the loader motor is

  public void getLoaderBeamBreakValue() {
    m_beamBreakLoader.get();
  }

  // stops the loading motor and the holder motor
  public void stopAllMotors() {
    m_holderMotor.set(0);
    m_loaderMotor.set(0);
  }

  // only stops the loader motor
  public void stopLoaderMotor() {
    m_loaderMotor.set(0);
  }

  // only stops the holder motor
  public void stopHolderMotor() {
    m_holderMotor.set(0);
  }

  // displays data onto SmartDashboard
  public void log() {
    SmartDashboard.putData("Holder Beam Value", m_beamBreakHolder);
    SmartDashboard.putData("Loader Bean Value", m_beamBreakLoader);
    SmartDashboard.putNumber("Holder Motor Speed", m_holderMotor.get());
    SmartDashboard.putNumber("Loader Motor Speed", m_loaderMotor.get());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    log();
  }
}
