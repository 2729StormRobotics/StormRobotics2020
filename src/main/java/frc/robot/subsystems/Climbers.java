/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.ClimberConstants;
import frc.robot.Constants.DriveConstants;

public class Climbers extends SubsystemBase {
  
  private final CANSparkMax m_leftClimber;
  private final CANSparkMax m_rightClimber;
  

  /**
   * Creates a new Climbers.
   */
  public Climbers() {

    m_leftClimber = new CANSparkMax(ClimberConstants.kLeftClimberMotorPort, MotorType.kBrushless);
    m_rightClimber = new CANSparkMax(ClimberConstants.kRightClimberMotorPort, MotorType.kBrushless);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
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
    encoder.setPositionConversionFactor(DriveConstants.kEncoderDistancePerPulse);
    encoder.setVelocityConversionFactor(DriveConstants.kEncoderSpeedPerPulse); 
    encoderReset(encoder); //Calls the encoderReset method

  }

  private void encoderReset(CANEncoder encoder) {
    //Resets encoder value to 0.
    encoder.setPosition(0);
  }

  


}
