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

  /**
   * This command requires the cellevator subsystem. By default, don't run the
   * motor.
   */
  public CellevatorHolder(Cellevator cellevator) {
    m_cellevator = cellevator;
    runHolderMotor = false;
    addRequirements(m_cellevator);
  }

  /** 
   * creates a boolean with a value that represents which condition is happening 
  * if either of the two conditions are true, then the holder motor will run
  */
  private boolean isSafeToRunHolder() {
    boolean topClearAndMiddleOccupied = !m_cellevator.isTopBallPresent() && !m_cellevator.isMiddleGapClear();
    boolean onlyBottomOccupied = m_cellevator.isBottomBallPresent() && !m_cellevator.isTopBallPresent() && m_cellevator.isMiddleGapClear() && m_cellevator.getBeamBreakMiddlePrevious();

    return topClearAndMiddleOccupied || onlyBottomOccupied;
  }

  /** 
   * sets a boolean to true if the loader motor can run 
  */
  private boolean isSafeToLoad() {
    //if there is no power cell at the bottom of the cellevator and the gap is clear in the middle then the loader motor can run
    boolean middleIsClear = !m_cellevator.isBottomBallPresent() && m_cellevator.isMiddleGapClear();
    
    return middleIsClear;
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
    // if the boolean is true run the holder motor
    if (isSafeToRunHolder()) {
      m_cellevator.runHolderMotor(CellevatorConstants.kHolderMotorSpeed);
    } else {
      m_cellevator.stopHolderMotor();
    }

    // if the bottom of the cellevator is empty and the subsystem has been notified that intake is occuring then run the motor
    if (isSafeToLoad() && m_cellevator.getShouldRunLoader()) {
        m_cellevator.runLoaderMotor(CellevatorConstants.kLoaderMotorSpeed);
    }
    // this should be here instead of end so that the loader is independent of the holder
    else {
      m_cellevator.stopLoaderMotor();
    }
  }

  /**
   * stop the motors at the end of the command
   */
  @Override
  public void end(boolean interrupted) {
    // this only runs if interrupted, as this default command does not end naturally
    m_cellevator.stopHolderMotor();
    m_cellevator.stopLoaderMotor();
  }

  /**
   * end the command if we have reached the target or if are not running the motor
   */
  @Override
  public boolean isFinished() {
    return false;
  }
}
