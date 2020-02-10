/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.CellevatorConstants;
import frc.robot.subsystems.Cellevator;

public class CellevatorHolder extends CommandBase {

  private final Cellevator m_cellevator;
  private boolean runHolderMotor = false;
  
  /**
   * true = run until beam break middle 
  * false = run until beam break holder
  */

  private boolean runUntil; 
  /**
   * Creates a new CellevatorHolder.
   */
  public CellevatorHolder(Cellevator cellevator) {
      m_cellevator = cellevator;
      addRequirements(m_cellevator);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
  }

/**
 * if intialized results in running the motor, then run the motor
 */
  @Override
  public void execute() {
    if (runHolderMotor) {
      m_cellevator.runHolderMotor(CellevatorConstants.kHolderMotorSpeed);
    }


  }

/**
 * stop the motors at the end of the command
 */
  @Override
  public void end(boolean interrupted) {
    m_cellevator.stopHolderMotor();
  }

  /**
   * end the command if we have reached the target or if are not running the motor
   */
  @Override
  public boolean isFinished() {
    if (!runHolderMotor) {
      // if the motor is not running, the command will finish
      return true;
    }
    else if (runUntil) {
      // running the motor until the middle beam break
      return m_cellevator.isMiddleBallPresent();
    }
    else {
      // running the motor until the top beam break
      return m_cellevator.isTopBallPresent();
    }
  }
}
