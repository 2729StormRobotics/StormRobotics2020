/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Climbers;


public class FrictionBrakeRelease extends InstantCommand {
  //This command uses the Climbers subsystem
  private final Climbers m_climbers;

  public FrictionBrakeRelease(Climbers subsystem) {
    m_climbers = subsystem; //the parqameter is equal to the climbers subsystem

    addRequirements(m_climbers); //adds the subsystem as a requirement for the command to run
  }


  @Override
  public void initialize() {
    m_climbers.engageFrictionBrake(true);

  }
}
