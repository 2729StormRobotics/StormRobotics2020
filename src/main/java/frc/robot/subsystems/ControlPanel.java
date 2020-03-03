/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import static frc.robot.Constants.ControlPanelConstants.*;

import java.util.Map;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.SuppliedValueWidget;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ControlPanel extends SubsystemBase {
  private final WPI_TalonSRX m_spinnerMotor;
  private final ColorMatch m_colorMatch;
  private final ColorSensorV3 m_colorSensor;

  private String m_gameData = "Unknown";
  private String m_targetColorString = "BLACK";
  private String m_currentColorString = "BLACK";
  private int m_revColorCount = 0;
  private boolean m_readyToCount = false;
  private boolean m_readyToFindColor = false;
  private boolean m_targetColorAvailable = false;
  private boolean m_currentColorAvailable = false;
  private int m_colorCheck = 0;
  private Color m_initialColor = kNoColor;
  private Color m_previousColor = kNoColor;
  private Color m_previousColorCheck = kNoColor;
  private Color m_currentColor = kNoColor;
  private Color m_targetColor = kUnknownColor;

  private final ShuffleboardTab m_testingTab;
  private final ShuffleboardLayout m_controlPanelStatus;
  private SuppliedValueWidget<Boolean> m_targetColorWidget;
  private SuppliedValueWidget<Boolean> m_currentColorWidget;

  private final NetworkTable m_PartyTable;
  private final NetworkTableEntry m_ControlPanelColorStatus;

  /**
   * Creates a new ControlPanel.
   */
  public ControlPanel() {
    m_spinnerMotor = new WPI_TalonSRX(kSpinnerMotorPort);
    m_spinnerMotor.configFactoryDefault();
    m_spinnerMotor.configSelectedFeedbackSensor(FeedbackDevice.SoftwareEmulatedSensor);
    m_spinnerMotor.configSelectedFeedbackCoefficient(kRevConversion);

    m_colorSensor = new ColorSensorV3(Port.kOnboard);

    m_colorMatch = new ColorMatch();
    m_colorMatch.addColorMatch(kRedTarget);
    m_colorMatch.addColorMatch(kBlueTarget);
    m_colorMatch.addColorMatch(kGreenTarget);
    m_colorMatch.addColorMatch(kYellowTarget);

    m_testingTab = Shuffleboard.getTab("Testing");
    m_controlPanelStatus = m_testingTab.getLayout("Control Panel", BuiltInLayouts.kList)
        .withProperties(Map.of("Label position", "TOP"));

    shuffleboardInit();

    m_PartyTable = NetworkTableInstance.getDefault().getTable("Party Statuses");
    m_ControlPanelColorStatus = m_PartyTable.getEntry("Color Detected");
  }

  // Spins the control panel
  public void spinMotor(double speed) {
    m_spinnerMotor.set(speed);
  }

  public void detectInitialColor() {
    Color detectedColor = m_colorSensor.getColor();
    ColorMatchResult matchResult = m_colorMatch.matchClosestColor(detectedColor);

    m_initialColor = matchResult.color;
    m_previousColorCheck = m_initialColor;
    m_previousColor = m_initialColor;
  }

  // Detects the current color from the color sensor
  // Will also update a special current color check for counting purposes
  public void detectCurrentColor() {
    Color detectedColor = m_colorSensor.getColor();
    ColorMatchResult matchResult = m_colorMatch.matchClosestColor(detectedColor);

    if (matchResult.color == m_previousColorCheck) {
      m_colorCheck++;
    } else {
      m_colorCheck = 0;
    }

    if (m_colorCheck > 5) {
      m_currentColorAvailable = true;
      m_currentColor = matchResult.color;
    }

    if (m_currentColor == kRedTarget) {
      m_currentColorString = "BLUE";
    } else if (m_currentColor == kYellowTarget) {
      m_currentColorString = "GREEN";
    } else if (m_currentColor == kGreenTarget) {
      m_currentColorString = "YELLOW";
    } else if (m_currentColor == kBlueTarget) {
      m_currentColorString = "RED";
    } else {
      m_currentColorAvailable = false;
      m_currentColorString = "BLACK";
    }
  }

  public void resetColorCount() {
    m_revColorCount = 0;
  }

  public void countByColor() {
    if (m_currentColor != m_previousColor) {
      m_revColorCount++;
      m_previousColor = m_currentColor;
    }
  }

  public double revCountByColor() {
    return (double) m_revColorCount / 8.0;
  }

  // Detect the target color from FMS
  public void getTargetColor() {
    // Gets data sent from the drivers station
    m_gameData = DriverStation.getInstance().getGameSpecificMessage();

    if (m_gameData.length() > 0) {
      m_targetColorAvailable = true;

      switch (m_gameData.charAt(0)) {
      case 'B': // Blue
        m_targetColor = kBlueTarget;
        m_targetColorString = "BLUE";
        break;
      case 'G': // Green
        m_targetColor = kGreenTarget;
        m_targetColorString = "GREEN";
        break;
      case 'R': // Red
        m_targetColor = kRedTarget;
        m_targetColorString = "RED";
        break;
      case 'Y': // Yellow
        m_targetColor = kYellowTarget;
        m_targetColorString = "YELLOW";
        break;
      default:
        m_targetColorString = "BLACK";
        break;
      }
    } else {
      m_targetColorAvailable = false;
    }
  }

  public boolean onTargetColor() {
    boolean onTargetColor = m_currentColor.equals(m_targetColor);
    m_ControlPanelColorStatus.setBoolean(onTargetColor);
    return onTargetColor;
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

  private void shuffleboardInit() {
    m_targetColorWidget = m_controlPanelStatus.addBoolean("Target Color", () -> m_targetColorAvailable)
        .withWidget(BuiltInWidgets.kBooleanBox);
    m_targetColorWidget.withProperties(Map.of("colorWhenFalse", "BLACK"));
    m_currentColorWidget = m_controlPanelStatus.addBoolean("Current Color", () -> m_currentColorAvailable)
        .withWidget(BuiltInWidgets.kBooleanBox);
    m_currentColorWidget.withProperties(Map.of("colorWhenFalse", "BLACK"));

    m_controlPanelStatus.addBoolean("On Target", () -> onTargetColor());
    m_controlPanelStatus.addBoolean("Counting by Color", () -> m_readyToCount);
    m_controlPanelStatus.addBoolean("Seeking Color", () -> m_readyToFindColor);
    m_controlPanelStatus.addNumber("Revolutions", () -> revCountByColor());
    m_controlPanelStatus.addNumber("Color Check", () -> m_colorCheck);
    m_controlPanelStatus.addNumber("Colors Counted", () -> m_revColorCount);
    m_controlPanelStatus.addString("Current Color Text", () -> m_currentColorString);

  }

  @Override
  public void periodic() {
    m_targetColorWidget.withWidget(BuiltInWidgets.kBooleanBox)
        .withProperties(Map.of("colorWhenTrue", m_targetColorString));
    m_currentColorWidget.withWidget(BuiltInWidgets.kBooleanBox)
        .withProperties(Map.of("colorWhenTrue", m_currentColorString));
  }
}
