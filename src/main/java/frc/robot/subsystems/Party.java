/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.PartyConstants;

public class Party extends SubsystemBase {
  
  // Create instance of led driver, created as motor controller because it is controlled by standard PWM signal
  private final Spark m_ledDriver;
  
  /**
   * Creates a new Party.
   */
  public Party() {    
    // Instantiate led driver and assign to its port
    m_ledDriver = new Spark(PartyConstants.kLedBlinkinDriverPort);
  }

  // Methods for each LED mode
  public void danceParty() {
    m_ledDriver.set(PartyConstants.kDancePartySparkValue);
  }
  public void intakeSuccessful() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
