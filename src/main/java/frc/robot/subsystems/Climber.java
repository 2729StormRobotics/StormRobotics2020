/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import static frc.robot.Constants.ClimberConstants.*;

import java.util.Map;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Climber extends SubsystemBase {

  //creates
  private final Solenoid m_extendClimber;
  private boolean m_extend = true;

  private final Solenoid m_retractClimber;
  private boolean m_retract = true;

  private final ShuffleboardTab m_climberTab;
  private final ShuffleboardLayout m_climberStatus;

  private final NetworkTable m_PartyTable;
  private final NetworkTableEntry m_ClimbStatus;


  /**
   * Creates a new Climber subsystem.
   */
  public Climber(){
    //creates solenoid
    m_extendClimber = new Solenoid(0);
    m_retractClimber = new Solenoid(0);

    m_climberTab = Shuffleboard.getTab(kShuffleboardTab);
    m_climberStatus = m_climberTab.getLayout("Climber Status", BuiltInLayouts.kList).withProperties(Map.of("Label Position", "TOP"));

    shuffleboardInit();

    m_PartyTable = NetworkTableInstance.getDefault().getTable("Party Statuses");
    m_ClimbStatus = m_PartyTable.getEntry("Climb Status");

  }



  public void extendClimber() {
    m_extendClimber.set(kExtendClimber);
    // m_FrictionBrakeStatus.setBoolean(true);
  }

  public void retractClimber() {
    m_retractClimber.set(kRetractClimber);
    // m_FrictionBrakeStatus.setBoolean(true);
  }

 
  private void shuffleboardInit() {
    // Adds the climber data to the Shuffleboard in its own tab.
  }

  @Override
  public void periodic() {

    // This method will be called once per scheduler run
  }
}
