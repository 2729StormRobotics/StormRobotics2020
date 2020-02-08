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
import frc.robot.Constants.ControlPanelConstants;
import edu.wpi.first.wpilibj.util.Color;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatch;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.DriverStation;

public class ControlPanel extends SubsystemBase {

  private final WPI_TalonSRX m_controlPanelMotor = new WPI_TalonSRX(ControlPanelConstants.kSpinnerMotorPort);
  private final ColorMatch m_colorMatch = new ColorMatch();
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

     String color;
     int count = 0;
     String colorString;
     String gameData;

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
 
  // displays the color based on RBG values
  public void getColorSensorValue() {

    Color detectedColor = m_colorSensor.getColor();
    ColorMatchResult match = m_colorMatch.matchClosestColor(detectedColor);

    if (match.color == ControlPanelConstants.kRedTarget) {
      color = "Red";
      colorString = "Red";
    } else if (match.color == ControlPanelConstants.kYellowTarget) {
      color = "Yellow";
    } else if (match.color == ControlPanelConstants.kGreenTarget) {
      color = "Green";
    } else if (match.color == ControlPanelConstants.kBlueTarget) {
      color = "Blue";
      colorString = "Blue";
    } else {
      color = "Unknown";
    }

  }
  public int colorCount(){
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
public void setColor(){
  //Gets data sent from the drivers station
  gameData = DriverStation.getInstance().getGameSpecificMessage();

  if(gameData.length() > 0)
{
  switch (gameData.charAt(0))
  {
    case 'B' :  //Blue
        findBlue();
      break;
    case 'G' :  //Green
        findGreen();
      break;
    case 'R' :  //Red
        findRed();
      break;
    case 'Y' :  //Yellow
        findYellow();
      break;
    default :

      break;
  }
} else {
  //Code for no data received yet
}

}
public void findBlue(){
  startMotors(.18);
  if (color == "Red")
    startMotors(0);
}
public void findRed(){
  startMotors(.18);
  if (color == "Blue")
    startMotors(0);
}
public void findGreen(){
  startMotors(.18);
  if (color == "Yellow")
    startMotors(0);
}
public void findYellow(){
  startMotors(.18);
  if (color == "Green")
    startMotors(0);
}

public boolean isSpun(){
  Boolean spun= false;
  if (colorCount() >= 8){
    spun = true;
  }
  return spun;
}

//Controls motor speed 
public void wheelMotorPower(){
  if (isSpun() == false){
    startMotors(.4);
  }
  else if (isSpun() == true){
    startMotors(0);
  }

}
//stops motor after Red & Blue is passed 8 times
public void wheelStop(){
  if (isSpun() == true && colorCount() >=8){
    stopSpinning();
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
