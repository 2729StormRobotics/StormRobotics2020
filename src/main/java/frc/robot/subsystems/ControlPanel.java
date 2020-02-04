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
import frc.robot.Constants;
import frc.robot.Constants.ControlPanelConstants;
import edu.wpi.first.wpilibj.util.Color;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatch;
import edu.wpi.first.wpilibj.I2C;

public class ControlPanel extends SubsystemBase {

  private final WPI_TalonSRX m_controlPanelMotor = new WPI_TalonSRX(Constants.ControlPanelConstants.kSpinnerMotorPort);
  private final ColorMatch m_colorMatch = new ColorMatch();
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

     String color;

  /**
   * Creates a new ControlPanel.
   */
  public ControlPanel() {

    m_controlPanelMotor.configFactoryDefault();
    addChild("Control Panel Motor", m_controlPanelMotor);  

  }

  // starts the control panel motors
  public void startMotors(double speed) {

    m_controlPanelMotor.set(speed);

  }
 
  // sets RBG values for each color target
  // still need to add color target constants 
  public void getRBGValues() { 
    // private final Color kRedTarget = ColorMatch.makeColor(0.476, 0.376, 0.15);
    // private final Color kYellowTarget = ColorMatch.makeColor(0.381, 0.545, 0.136);
    // private final Color kGreenTarget = ColorMatch.makeColor(0.18, 0.568, 0.249);
    // private final Color kBlueTarget = ColorMatch.makeColor(0.15, 0.4467, 0.40);
  }

  // displays the color based on RBG values
  public void getColorSensorValue() {

    Color detectedColor = m_colorSensor.getColor();
    ColorMatchResult match = m_colorMatch.matchClosestColor(detectedColor);

    if (match.color == (Constants.ControlPanelConstants.kRedTarget) {
      color = "Red";
    } else if (match.color == Constants.ControlPanelConstants.kYellowTarget) {
      color = "Yellow";
    } else if (match.color == Constants.ControlPanelConstants.kGreenTarget) {
      color = "Green";
    } else if (match.color == Constants.ControlPanelConstants.kBlueTarget) {
      color = "Blue";
    } else {
      color = "Unknown";
    }

  }

  // stops the color panel motor
  public void stopSpinning() {

    m_controlPanelMotor.set(0);

  }

  // displays data onto SmartDashboard
  public void updateColor() {

    SmartDashboard.putString("Color", color);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    updateColor();
  }
}
