/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.ClimberConstants;

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




}
