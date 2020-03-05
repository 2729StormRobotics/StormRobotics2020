/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.subsystems.Cellevator;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.PartyConstants.*;
import edu.wpi.first.wpilibj.Timer;

public class Party extends SubsystemBase {

  // Create instances of led driver, created as motor controller because it is
  // controlled by standard PWM signal
  private final Spark m_ledDriver;
  private final NetworkTable m_PartyTable;
  private final NetworkTableEntry m_CellStatus;
  private final NetworkTableEntry m_AlignStatus;
  private final NetworkTableEntry m_RevStatus;
  private final NetworkTableEntry m_LaunchAngleStatus;
  private final NetworkTableEntry m_ClimbStatus;
  private final NetworkTableEntry m_ControlPanelColorStatus;
  private final NetworkTableEntry m_HighGearStatus;
  private final NetworkTableEntry m_LowGearStatus;

  private final Timer m_LaunchToggleTimer = new Timer();
  private final Timer m_GearshiftTimer = new Timer();
  private final Timer m_RevToSpeedTimer = new Timer();

  private boolean m_PriorHighGear = false;
  private boolean m_PriorLaunchAngle = false;
  private boolean m_PriorRevving = false;

  private int cellCount = 0;

  /**
   * Creates a new Party.
   */
  public Party() {

    m_PartyTable = NetworkTableInstance.getDefault().getTable("Party Statuses");
    m_CellStatus = m_PartyTable.getEntry("Cell Count");
    m_AlignStatus = m_PartyTable.getEntry("Aligned");
    m_RevStatus = m_PartyTable.getEntry("Revved");
    m_LaunchAngleStatus = m_PartyTable.getEntry("Launch Angle Toggled");
    m_ClimbStatus = m_PartyTable.getEntry("Climb Status");
    m_ControlPanelColorStatus = m_PartyTable.getEntry("Color Detected");
    m_HighGearStatus = m_PartyTable.getEntry("High Gear");
    m_LowGearStatus = m_PartyTable.getEntry("Low Gear");

    m_LaunchToggleTimer.start();
    m_GearshiftTimer.start();
    m_RevToSpeedTimer.start();

    // Instantiate led driver and assign to its port
    m_ledDriver = new Spark(kLedBlinkinDriverPort);
  }

  // Methods for each LED mode
  // Set LED blinkin to the spark motor double
  public void setDisabledColor() {
    // For when robot is disabled
    // Beats per minute, orange and red
    m_ledDriver.set(kDisabled);
  }

  public void setGearshiftHighColor() {
    // For when the drive gear shifts high
    // Solid White
    m_ledDriver.set(kHighGear);
  }

  public void setGearshiftLowColor() {
    // For when the drive gear shifts low
    // Solid Gold
    m_ledDriver.set(kLowGear);
  }

  public void set1PowercellColor() {
    // For when there is one powercell in cellevator
    // Color 2 Solid
    m_ledDriver.set(k1PC);
  }

  public void set2PowercellColor() {
    // For when there are two powercells in the cellevator
    // Color 2 Heartbeat Fast
    m_ledDriver.set(k2PC);
  }

  public void set3PowercellColor() {
    // For when there are three powercells in the cellevator
    // Color 2 Strobe
    m_ledDriver.set(k3PC);
  }

  public void setAlignedColor() {
    // For when the robot is aligned to the target
    // Dark Green Solid
    m_ledDriver.set(kAligned);
  }

  public void setRevtoSpeedColor() {
    // For when the launcher is revevd to speed
    // Hot Pink Solid
    m_ledDriver.set(kRevToSpeed);
  }

  public void setRevvedandAlignedColor() {
    // For when the launcher is ready to launch
    // Color 1 Solid
    m_ledDriver.set(kRevvedAndAligned);
  }

  public void setLaunchAngleColor() {
    // For when the launcher angle has been toggled
    // Blue Green
    m_ledDriver.set(kLaunchAngleToggle);
  }

  public void setControlPanelColor() {
    // For when correct color is detected on color sensor
    // Lawn Green
    m_ledDriver.set(kControlPanel);
  }

  public void setRainbowParty() {
    // For when robot is climbing
    // there is also a designated button for anytime
    // Rainbow Rainbow
    m_ledDriver.set(kDoneClimb);
  }

  public void updateNetworkTable() {
    cellCount = m_CellStatus.getNumber(0).intValue();

    boolean highGear = m_HighGearStatus.getBoolean(false);
    if (highGear != m_PriorHighGear) {
      m_GearshiftTimer.reset();
      m_GearshiftTimer.start();
    }
    m_PriorHighGear = highGear;

    boolean launchAngle = m_LaunchAngleStatus.getBoolean(false);
    if (launchAngle != m_PriorLaunchAngle) {
      m_LaunchToggleTimer.reset();
      m_LaunchToggleTimer.start();
    }
    m_PriorLaunchAngle = launchAngle;

    boolean revToSpeed = m_RevStatus.getBoolean(false);
    if (revToSpeed != m_PriorRevving) {
      m_RevToSpeedTimer.reset();
      m_RevToSpeedTimer.start();
    }
    m_PriorRevving = revToSpeed;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    updateNetworkTable();

    if (m_ClimbStatus.getBoolean(false)) {
      setRainbowParty();
    } else if (m_AlignStatus.getBoolean(false)) {
      if (m_RevStatus.getBoolean(false)) {
        setRevvedandAlignedColor();
      }
      setAlignedColor();
    } else if (m_ControlPanelColorStatus.getBoolean(false)) {
      setControlPanelColor();
    } else if (m_RevStatus.getBoolean(false) && !m_RevToSpeedTimer.hasElapsed(3)) {
      setRevtoSpeedColor();
    } else if (m_HighGearStatus.getBoolean(false) && !m_GearshiftTimer.hasElapsed(3)) {
      setGearshiftHighColor();
    } else if (m_LowGearStatus.getBoolean(false) && !m_GearshiftTimer.hasElapsed(3)) {
      setGearshiftLowColor();
    } else if (m_LaunchAngleStatus.getBoolean(false) && !m_LaunchToggleTimer.hasElapsed(3)) {
      setLaunchAngleColor();
    } else if (cellCount == 3) {
      set3PowercellColor();
    } else if (cellCount == 2) {
      set2PowercellColor();
    } else if (cellCount == 1) {
      set1PowercellColor();
    } else {
      setDisabledColor();
    }
  }
}
