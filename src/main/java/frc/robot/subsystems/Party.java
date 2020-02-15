/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.PartyConstants.*;

public class Party extends SubsystemBase {
  
  // Create instances of led driver, created as motor controller because it is controlled by standard PWM signal
  private final Spark m_ledDriver1;
  private final Spark m_ledDriver2;
  
  /**
   * Creates a new Party.
   */
  public Party() {    
    // Instantiate led driver and assign to its port
    m_ledDriver1 = new Spark(kLedBlinkinDriver1Port);
    m_ledDriver2 = new Spark(kLedBlinkinDriver2Port);
  }

  // Method for setting LED mode to normal: Red and Orange Color Waves
  public void setToNormal() {
    m_ledDriver1.set(kNormal);
    m_ledDriver2.set(kNormal);
  }

  // Methods for each LED mode
  public void turnGreen() {
    // Set LED blinkin to the spark motor double
    m_ledDriver1.set(kGreen);
    m_ledDriver2.set(kGreen);
  }

  

  public void intakeSuccessful() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
