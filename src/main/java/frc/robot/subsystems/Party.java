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
  private final Spark m_ledDriver;
  
  /**
   * Creates a new Party.
   */
  public Party() {    
    // Instantiate led driver and assign to its port
    m_ledDriver = new Spark(kLedBlinkinDriverPort);
  }

  // Method for setting LED mode to normal: Red and Orange Color Waves
  public void setToNormal() {
    m_ledDriver.set(kNormal);
  }

  // Methods for each LED mode
  // Set LED blinkin to the spark motor double
  public void turnWhite() {
    // For when there is 1 power cell in cellevator
    m_ledDriver.set(kWhite);
  }

  public void turnGray() {
    // For when when there is 2 power cells in cellevator
    m_ledDriver.set(kGray);
  }

  public void turnBlack() {
    // For when there are 3 power cells in cellevator
    m_ledDriver.set(kBlack);
  }

  public void turnGreen() {
    // For when aligned to target
    m_ledDriver.set(kGreen);
  }

  public void turnYellow() {
    // For when aligning to target but not aligned yet
    m_ledDriver.set(kYellow);
  }

  public void turnHeartbeatBlue() {
    // For when launcher is at desired speed
    m_ledDriver.set(kHeartbeatBlue);
  }

  public void turnPink() {
    // For when launcher is at higher angle
    m_ledDriver.set(kPink);
  }

  public void turnViolet() {
    // For when launcher is at lower angle
    m_ledDriver.set(kViolet);
  }

  public void turnStrobeGold() {
    // For when climber is going up
    m_ledDriver.set(kStrobeGold);
  }

  public void turnStrobeWhite() {
    // For when climber is going down
    m_ledDriver.set(kStrobeWhite);
  }

  public void turnRed() {
    // For when climber hooks have reached max height
    m_ledDriver.set(kRed);
  }

  public void turnRainbowParty() {
    // For when a successful climb has been completed
    // Also has a designated button on controller for any other time
    m_ledDriver.set(kRainbowParty);
  }

  public void turnOrange() {
    // for anything extra
    m_ledDriver.set(kOrange);
  }
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
