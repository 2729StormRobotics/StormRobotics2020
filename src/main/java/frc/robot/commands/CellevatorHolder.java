/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
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
    
    if (m_cellevator.isBottomBallPresent() && m_cellevator.isMiddleBallPresent() && m_cellevator.isTopBallPresent()) {
      // if the cellevator is empty, then do not move the motor
      runHolderMotor = false;
    }
    else if (m_cellevator.isBottomBallPresent() && !m_cellevator.isMiddleBallPresent() && !m_cellevator.isTopBallPresent()) {
      // if there is a power cell in the bottom and nowhere else, move it to the middle
      runHolderMotor = true;
      runUntil = true;
    }
    else if (!m_cellevator.isBottomBallPresent() && m_cellevator.isMiddleBallPresent() && !m_cellevator.isTopBallPresent()) {
      // if there is a power cell in the middle and nowhere else, do not move the motor
      runHolderMotor = false;
    }
    else if(m_cellevator.isBottomBallPresent() && m_cellevator.isMiddleBallPresent() && !m_cellevator.isTopBallPresent()) {
      // if there is a power cell in the bottom and the middle, move them both to the top
      runHolderMotor = true;
      runUntil = false;
    }
    else if (!m_cellevator.isBottomBallPresent() && m_cellevator.isMiddleBallPresent() && m_cellevator.isTopBallPresent()) {
      // if there is a power cell in the middle and the top, do not move the motor
      runHolderMotor = false;
    }
    else if (m_cellevator.isBottomBallPresent() && m_cellevator.isMiddleBallPresent() && m_cellevator.isTopBallPresent()) {
      // if the cellevator is full, then do not run the motor
      runHolderMotor = false;
    }
    else if (!m_cellevator.isBottomBallPresent() && !m_cellevator.isMiddleBallPresent() && m_cellevator.isTopBallPresent()) {
      // if there is only a power cell at the top, then do not move the motor
      // this will probably never happen but may occur if launching stops in the middle of the cycle
      runHolderMotor = false;
    }
    else if (m_cellevator.isBottomBallPresent() && !m_cellevator.isMiddleBallPresent() && m_cellevator.isTopBallPresent()) {
      // if there is a power cell at the top and the bottom, then do not run the motor
      // this should never happen during normal operation, it is only for safety
      runHolderMotor = false;
    }
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
