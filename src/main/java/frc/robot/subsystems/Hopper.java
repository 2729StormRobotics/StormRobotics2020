/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.HopperConstants.*;

import java.util.Map;

public class Hopper extends SubsystemBase {
  private final WPI_TalonSRX m_hopperMotor;

  private final ShuffleboardTab m_hopperTab;
  private final ShuffleboardLayout m_hopperStatus;
  
  /**
   * Creates a new Hopper.
   */
  public Hopper() {
    m_hopperMotor = new WPI_TalonSRX(kHopperMotorPort);

    m_hopperMotor.configFactoryDefault();
    m_hopperMotor.setNeutralMode(NeutralMode.Coast);
    
    addChild("Hopper Motor", m_hopperMotor);

    m_hopperTab = Shuffleboard.getTab(kShuffleboardTab);
    m_hopperStatus = m_hopperTab.getLayout("Status", BuiltInLayouts.kList)
        .withProperties(Map.of("Label position", "TOP"));

    //shuffleboardInit();
  }

  /**
   * starts the hopper motor to transfer the power cells from the intake to the
   * cellevator
   */
  public void runHopper(double speed) {
    m_hopperMotor.set(speed);
  }

  /**
   * stops the hopper motor
   */
  public void stopHopper() {
    m_hopperMotor.set(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
