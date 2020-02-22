/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.ControlPanelConstants.*;
import edu.wpi.first.wpilibj.util.Color;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatch;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.DriverStation;

public class ControlPanel extends SubsystemBase {

  private final WPI_TalonSRX m_controlPanelMotor;
  private final ColorMatch m_colorMatch;
  private final ColorSensorV3 m_colorSensor;

  private String m_gameData;
  private String m_targetColor = "";
  private String m_currentColor = "";
  private int m_revColorCount = 0;
  private String m_lastColorCheck;
  private boolean m_firstColorDetected = false;
  private boolean m_readyToCount = false;
  private boolean m_readyToFindColor = false;

  /**
   * Creates a new ControlPanel.
   */
  public ControlPanel() {
    m_controlPanelMotor = new WPI_TalonSRX(kSpinnerMotorPort);
    m_controlPanelMotor.configFactoryDefault();

    m_colorSensor = new ColorSensorV3(I2C.Port.kOnboard);

    m_colorMatch = new ColorMatch();
    m_colorMatch.setConfidenceThreshold(kConfidence);

    addChild("Control Panel Motor", m_controlPanelMotor);
  }

  // Spins the control panel
  public void spinMotor(double speed) {
    m_controlPanelMotor.set(speed);
  }

  // Detects the current color from the color sensor
  // Will also update a special current color check for counting purposes
  private void detectCurrentColor() {
    Color detectedColor = m_colorSensor.getColor();
    ColorMatchResult match = m_colorMatch.matchColor(detectedColor);

    if(match ==null) {
      m_currentColor = "Unkown";
    } else {
    if (match.color == kRedTarget) {
      m_currentColor = "Red";
      m_firstColorDetected = true;
      m_lastColorCheck = m_currentColor;
    } else if (match.color == kYellowTarget) {
      m_currentColor = "Yellow";
    } else if (match.color == kGreenTarget) {
      m_currentColor = "Green";
    } else if (match.color == kBlueTarget) {
      m_currentColor = "Blue";
      m_firstColorDetected = true;
      m_lastColorCheck = m_currentColor;
    } else {
      m_currentColor = "Unknown";
    }
  }
}

  public void resetColorCount() {
    m_revColorCount = 0;
    m_firstColorDetected = false;
    m_readyToCount = true;
  }

  public void countByColor() {
    // Counts how many times red and blue have passed
    if (m_firstColorDetected) {
      if ((m_currentColor.equals("Red") && m_lastColorCheck.equals("Blue"))
          || (m_currentColor.equals("Blue") && m_lastColorCheck.equals("Red"))) {
        m_revColorCount++;
      }
    }
  }

  // Detect the target color from FMS
  public void getTargetColor() {
    // Gets data sent from the drivers station
    m_gameData = DriverStation.getInstance().getGameSpecificMessage();

    if (m_gameData.length() > 0) {
      switch (m_gameData.charAt(0)) {
      case 'B': // Blue
        m_targetColor = "Red";
        break;
      case 'G': // Green
        m_targetColor = "Yellow";
        break;
      case 'R': // Red
        m_targetColor = "Blue";
        break;
      case 'Y': // Yellow
        m_targetColor = "Green";
        break;
      default:
        break;
      }
    } else {
      // Code for no data received yet
    }

  }

  public boolean onTargetColor() {
    return m_currentColor.equals(m_targetColor);
  }

  // checks to see if wheel has passed 8 times
  public boolean isSpun() {
    return (m_revColorCount >= 8);
  }

  // Spins control panel for revolution counting
  public void spinWheelForRevolutions() {
    spinMotor(kCountRevSpeed);
    m_readyToCount = true;
  }

  // Spins control panel for targting color
  public void spinWheelForColor() {
    spinMotor(kFindColorSpeed);
    m_readyToFindColor = true;
  }

  // stops the color panel motor
  public void stopSpinning() {
    spinMotor(0.0);
    m_readyToCount = false;
    m_readyToFindColor = false;
  }

  // displays data onto SmartDashboard
  private void log() {
    SmartDashboard.putString("Current Color", m_currentColor);
    SmartDashboard.putString("Target Color", m_targetColor);
    SmartDashboard.putNumber("Spin Count", m_revColorCount);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (m_readyToCount || m_readyToFindColor) {
      detectCurrentColor();
    }
    getTargetColor();
    log();
  }
}
