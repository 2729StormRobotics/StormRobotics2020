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

  private String gameData;
  private String targetColor;
  private String color;
  private int count = 0;
  private String colorString;

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

  // displays the color based on RBG values
  private void detectColor() {
    Color detectedColor = m_colorSensor.getColor();
    ColorMatchResult match = m_colorMatch.matchColor(detectedColor);

    if (match.color == kRedTarget) {
      color = "Red";
      colorString = "Red";
    } else if (match.color == kYellowTarget) {
      color = "Yellow";
    } else if (match.color == kGreenTarget) {
      color = "Green";
    } else if (match.color == kBlueTarget) {
      color = "Blue";
      colorString = "Blue";
    } else {
      color = "Unknown";
    }
  }

  public int colorCount() {
    String lastColor = color;    
    //Counts how many times red and blue have passed 
    if (lastColor.equals("Red") && colorString.equals("Blue") || lastColor.equals("Blue") && colorString.equals("Red")){
      count++;
      lastColor = colorString;
      }
      else if (lastColor.equals("") && (colorString.equals("Blue") || colorString.equals("Red"))){
        lastColor = colorString; 
      }
      return count;
    }


//checks to see if wheel has passed 8 times    
public void setTargetColor(){
  //Gets data sent from the drivers station
 
  gameData = DriverStation.getInstance().getGameSpecificMessage();

  if(gameData.length() > 0)
{
  switch (gameData.charAt(0))
  {
    case 'B' :  //Blue
        targetColor = "Red";
      break;
    case 'G' :  //Green
        targetColor = "Yellow";
      break;
    case 'R' :  //Red
        targetColor = "Blue";
      break;
    case 'Y' :  //Yellow
        targetColor = "Green";
      break;
    default :
      break;
  }
} else {
  //Code for no data received yet
}

}
  
public void findTargetColor(){
  spinMotor(.18);
  if (color.equals(targetColor))
    spinMotor(0);
}

  // checks to see if wheel has passed 8 times
  public boolean isSpun() {
    Boolean spun = false;
    if (colorCount() >= 8) {
      spun = true;
    }
    return spun;
  }

  // Controls motor speed
  public void wheelMotorPower() {
    if (!isSpun()) {
      spinMotor(kMotorSpeed); 
    } else if (isSpun()) {
      findTargetColor();
    }
  }

  // stops motor after Red & Blue is passed 8 times
  public void wheelStop() {
    if (isSpun() == true && colorCount() >= 8) {
      stopSpinning();
    }
  }

  // stops the color panel motor
  public void stopSpinning() {
    spinMotor(0.0);
  }

  // displays data onto SmartDashboard
  public void updateColor() {
    detectColor();
    SmartDashboard.putString("Current Color", color);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    updateColor();
  }
}
