/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HopperConstants;

public class Hopper extends SubsystemBase {

  private final TalonSRX m_hopperMotor;

  /**
   * Creates a new Hopper.
   */
  public Hopper() {
    m_hopperMotor = new TalonSRX(HopperConstants.kHopperMotorPort);
  
    motorInit(m_hopperMotor);
    
  }

  public void motorInit(TalonSRX motor) {
    // configures the factory defaults in case and changes were previously made to the motor
    motor.configFactoryDefault();   
  }

  /**
   * starts the hopper motor to transfer the power cells from the intake to the cellavator
   */ 
  public void startHopperMotor(double speed) {
    m_hopperMotor.set(ControlMode.PercentOutput, speed);;
  }

  /**
  *stops the hopper motor
  */
  public void stopHopperMotor() {
    m_hopperMotor.set(ControlMode.PercentOutput, 0);\

  }

  public void log() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    log();
  }
}
