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
  private boolean runHolderMotor;

  /**
   * This command requires the cellevator subsystem. By default, don't run the
   * motor.
   */
  public CellevatorHolder(Cellevator cellevator) {
    m_cellevator = cellevator;
    runHolderMotor = false;
    addRequirements(m_cellevator);
  }

  private boolean isSafeToLoad() {
    boolean topClearAndMiddleOccupied = !m_cellevator.isTopBallPresent() && !m_cellevator.isMiddleGapClear();
    boolean onlyBottomOccupied = m_cellevator.isBottomBallPresent() && !m_cellevator.isTopBallPresent() && m_cellevator.isMiddleGapClear() && m_cellevator.getBeamBreakMiddlePrevious();

    return topClearAndMiddleOccupied || onlyBottomOccupied;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    runHolderMotor = isSafeToLoad();
  }

  /**
   * if intialized results in running the motor, then run the motor
   */
  @Override
  public void execute() {
    // if the boolean is true run the holder motor
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
    return !isSafeToLoad();
  }
}
