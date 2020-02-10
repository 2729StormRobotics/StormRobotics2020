/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Climbers;

<<<<<<< HEAD


=======
>>>>>>> 2ff813a9f863b0d4c97a8c3ea607076c4fcf04c1
// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class FrictionBrakeRelease extends InstantCommand {
<<<<<<< HEAD
  Climbers m_climbers;   

  // public FrictionBrakeRelease() {
  //   addRequirements(m_climbers);
    // Use addRequirements() here to declare subsystem dependencies.
  

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    addRequirements(m_climbers);
    m_climbers.frictionBrakerOn(false);
    // before command starts, friction brake is off
=======
  private final Climbers m_climbers;

  public FrictionBrakeRelease(Climbers subsystem) {
    m_climbers = subsystem;

    addRequirements(m_climbers);
  }


  @Override
  public void initialize() {
    m_climbers.engageFrictionBrake(true);
>>>>>>> 2ff813a9f863b0d4c97a8c3ea607076c4fcf04c1
  }
}
