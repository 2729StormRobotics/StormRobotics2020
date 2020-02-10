/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climbers;

public class MoveClimber extends CommandBase {
  private final Climbers m_climbers;  //declares climber
  private final double m_speed;     //declares a speed variable

  public MoveClimber(double speed, Climbers climbers) {
    m_speed = speed;            //initializing speed
    m_climbers = climbers;     //initailizing climber
    addRequirements(m_climbers);      //it requires the climber subsystem to run the command
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_climbers.climb(m_speed);  //moves the climber using the speed

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    
    m_climbers.stopClimb(); //when the command is finished the motors stop
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;   //false because it is a default command
  }
}
