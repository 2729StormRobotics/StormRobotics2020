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
    m_ledDriver1 = new Spark(kLedBlinkinDriverPort);
    m_ledDriver2 = new Spark(kLedBlinkinDriverPort);
  }

  // Methods for each LED mode
  // Set LED blinkin to the spark motor double  
  public void setDisabledColor() {
    // For when robot is disabled
    // Beats per minute, orange and red
    m_ledDriver1.set(kDisabled);
    m_ledDriver2.set(kDisabled);
  }

  public void set1PowercellColor() {
    // For when there is one powercell in cellevator
    // Color 2 Solid
    m_ledDriver1.set(k1PC);
    m_ledDriver2.set(k1PC);
  }

  public void set2PowercellColor() {
    // For when there are two powercells in the cellevator
    // Color 2 Heartbeat Fast
    m_ledDriver1.set(k2PC);
    m_ledDriver2.set(k2PC);
  }

  public void set3PowercellColor() {
    // For when there are three powercells in the cellevator
    // Color 2 Strobe
    m_ledDriver1.set(k3PC);
    m_ledDriver2.set(k3PC);
  }

  public void setAlignedColor() {
    // For when the robot is aligned to the target
    // Dark Green Solid
    m_ledDriver1.set(kAligned);
    m_ledDriver2.set(kAligned);
  }

  public void setRevtoSpeedColor() {
    // For when the launcher is revved to speed
    // Blue Green Solid
    m_ledDriver1.set(kRevingToSpeed);
    m_ledDriver2.set(kRevingToSpeed);
  }

  public void setRevvedandAlignedColor() {
    // For when the launcher is ready to launch, aligned and revved
    // Forest Color Wave
    m_ledDriver1.set(kRevFullSpeed);
    m_ledDriver2.set(kRevFullSpeed);
  }

  public void setHighAngleColor() {
    // For when the launcher is at the high angle
    // Color 1 Strobe
    m_ledDriver1.set(kHighAngleShoot);
    m_ledDriver2.set(kHighAngleShoot);
  }

  public void setLowAngleColor() {
    // For when the launcher is at the low angle
    // Color 1 Solid
    m_ledDriver1.set(kLowAngleShoot);
    m_ledDriver2.set(kLowAngleShoot);
  }

  public void setClimbMaxColor() {
    // For when the climbing hook is at max height
    // Hot Pink Solid
    m_ledDriver1.set(kClimbMaxHeight);
    m_ledDriver2.set(kClimbMaxHeight);
  }

  public void setClimbingColor() {
    // For when the robot is climbing
    // Rainbow Lava
    m_ledDriver1.set(kClimbing);
    m_ledDriver2.set(kClimbMaxHeight);
  }

  public void setRainbowParty() {
    // For when a successful climb has been done
    // there is also a designated button for anytime
    // Rainbow Rainbow
    m_ledDriver1.set(kDoneClimb);
    m_ledDriver2.set(kDoneClimb);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
