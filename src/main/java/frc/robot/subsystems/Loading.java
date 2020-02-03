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

  private final CANSparkMax m_loadingMotor;
  private final CANSparkMax m_cellevatorMotor1;
  private final CANSparkMax m_cellevatorMotor2;
  private final DigitalInput m_beamTop;
  private final DigitalInput m_beamBottom;


  public Loading() {

    m_loadingMotor = new CANSparkMax(LoadingConstants.kLoadingMotorPort, MotorType.kBrushed);
    m_cellevatorMotor1 = new CANSparkMax(LoadingConstants.kLowCellevatorMotorPort, MotorType.kBrushed);
    m_cellevatorMotor2 = new CANSparkMax(LoadingConstants.kHighCellevatorMotor2Port, MotorType.kBrushed);
    m_beamTop = new DigitalInput(LoadingConstants.kBeamBreakOutput1Port);
    m_beamBottom = new DigitalInput(LoadingConstants.kBeamBreakOutput2Port);

    motorInit(m_loadingMotor, false);
    motorInit(m_cellevatorMotor1, false);
    motorInit(m_cellevatorMotor2, false);
    
  }

  private void motorInit(CANSparkMax motor, boolean invert) {
    motor.restoreFactoryDefaults(); // Reset settings in motor in case they are changed
    motor.setIdleMode(IdleMode.kBrake); // Sets the motors to brake mode from the beginning
  }

  // starts the loading motors
  public void startMotors (double speed) {
    m_loadingMotor.set(speed);
    m_cellevatorMotor1.set(speed);
    m_cellevatorMotor2.set(speed);
  }

  public void getBeamBreakValues () {
    m_beamTop.get();
    m_beamBottom.get();
  }

  // stops the loading motors
  public void stopMotors() {
    m_loadingMotor.set(0);
    m_cellevatorMotor1.set(0);
    m_cellevatorMotor2.set(0);
  }

  // displays data onto SmartDashboard
  public void log() {
    SmartDashboard.putData("Top Beam Value", m_beamTop);
    SmartDashboard.putData("Bottom Bean Value", m_beamBottom);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
