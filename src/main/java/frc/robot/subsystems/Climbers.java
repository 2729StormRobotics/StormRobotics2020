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

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ClimberConstants;

public class Climbers extends SubsystemBase {
  
  private final CANSparkMax m_leftClimber;
  private final CANSparkMax m_rightClimber;

  private final CANEncoder m_leftEncoder;
  private final CANEncoder m_rightEncoder;
  
  private final Solenoid m_frictionBrake;

  /**
   * Creates a new Climbers.
   */
  public Climbers() {

    m_leftClimber = new CANSparkMax(ClimberConstants.kLeftClimberMotorPort, MotorType.kBrushless);
    m_rightClimber = new CANSparkMax(ClimberConstants.kRightClimberMotorPort, MotorType.kBrushless);

    m_leftEncoder = m_leftClimber.getEncoder();
    m_rightEncoder = m_rightClimber.getEncoder();

    m_frictionBrake = new Solenoid(ClimberConstants.kFrictionSolenoidPort);

    motorInit(m_leftClimber, false);
    motorInit(m_rightClimber, false);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    log(); //Log on periodic
  }

  private void motorInit(CANSparkMax motor, boolean invert) {
    motor.restoreFactoryDefaults(); //Restores the default values in case something stayed from a previous reboot.
    motor.setIdleMode(IdleMode.kBrake); //Set motor mode to brake mode
    motor.setInverted(invert); //Invert the motor if needed.
    encoderInit(motor.getEncoder()); //Initialize the encoder.
  }

  private void encoderInit(CANEncoder encoder) {
    /* Sets the conversion factor for the encoder. This allows the
     * encoder to output the specified unit.
     */
    encoder.setPositionConversionFactor(ClimberConstants.kEncoderDistancePerPulse);
    encoder.setVelocityConversionFactor(ClimberConstants.kEncoderSpeedPerPulse); 
    encoderReset(encoder); //Calls the encoderReset method
  }

  private void encoderReset(CANEncoder encoder) {
    //Resets encoder value to 0.
    encoder.setPosition(0);
  }

  public void stopMotor(CANSparkMax motor) {
    //Sets the speed of the motor to 0.
    motor.set(0.0);
  }

  private void climb(CANSparkMax motor, double speed) {
    motor.set(speed); //The motor goes at a speed given to it. Not a specific speed
  }

  public void climberUp(CANSparkMax motorOne, CANSparkMax motorTwo) {
    climb(motorOne, 0.5); //FIXME 0.5 is a random value. This value needs to be tested.
    climb(motorTwo, 0.5); //FIXME 0.5 is a random value. This value needs to be tested.
  }

  public void climberDown(CANSparkMax motorOne, CANSparkMax motorTwo) {

    climb(motorOne, -0.5); //FIXME 0.5 is a random value. This value needs to be tested.
    climb(motorTwo, -0.5); //FIXME 0.5 is a random value. This value needs to be tested.
  }

  private double getRightEncoderValue() {
   return m_rightEncoder.getPosition(); //Get the position of the right encoder.
  }

  private double getLeftEncoderValue() {
   return m_leftEncoder.getPosition(); //Get the position of the left encoder.
  }

  public double getEncoderValue() {
    return ((getRightEncoderValue() + getLeftEncoderValue()) / 2); //Finds the average of the encoders.
  }

  public void frictionBrakerOn(boolean on) {
    m_frictionBrake.set(on); //Sets frictionBreak to the value inputted.
  }  
  
  public void log() {
    //Puts the climber data on the SmartDashboard.
    SmartDashboard.putNumber("Climber", getEncoderValue());
  }


}
