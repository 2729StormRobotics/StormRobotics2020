/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.PartyConstants.*;

public class Party extends SubsystemBase {
  
  // Create instances of led driver, created as motor controller because it is controlled by standard PWM signal
  private final Spark m_ledDriver;
  private final NetworkTable m_PartyTable;
  private final NetworkTableEntry m_CellStatus;
  private final NetworkTableEntry m_AlignStatus;
  private final NetworkTableEntry m_RevStatus;
  private final NetworkTableEntry m_LaunchAngleStatus;
  private final NetworkTableEntry m_FrictionBrakeStatus;
  private final NetworkTableEntry m_ControlPanelStatus;
  private final NetworkTableEntry m_HighGearStatus;
  private final NetworkTableEntry m_LowGearStatus;
  
  /**
   * Creates a new Party.
   */
  public Party() {  
    
    m_PartyTable = NetworkTableInstance.getDefault().getTable("Party Statuses");
    m_CellStatus = m_PartyTable.getEntry("Cell Count");
    m_AlignStatus = m_PartyTable.getEntry("Aligned");
    m_RevStatus = m_PartyTable.getEntry(("Revved"));
    m_LaunchAngleStatus = m_PartyTable.getEntry("Launch Angle Toggled");
    m_FrictionBrakeStatus = m_PartyTable.getEntry("Friction Brake Engaged");
    m_ControlPanelStatus = m_PartyTable.getEntry("Color Detected");
    m_HighGearStatus = m_PartyTable.getEntry("High Gear");
    m_LowGearStatus = m_PartyTable.getEntry("Low Gear");
    

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
