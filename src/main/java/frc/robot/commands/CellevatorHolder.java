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
   * This command requres the cellevator subsystem. By default, don't run the motor.
   */
  public CellevatorHolder(Cellevator cellevator) {
    m_cellevator = cellevator;
    runHolderMotor = false;
    addRequirements(m_cellevator); 
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (m_cellevator.isBottomBallPresent() && !m_cellevator.getBeamBreakMiddlePrevious()
        && !m_cellevator.isMiddleBallPresent() && !m_cellevator.isTopBallPresent()
        && m_cellevator.getPowerCellCount() == 1 || m_cellevator.getPowerCellCount() == 2) {
      // if the bottom beam break detects a powercell, the top doesn't, and there are 1 or 2 power cells in the cellevator, 
      // run the holder motor until a power cell breaks the middle beam breaker and then leaves, preserving the gap
      // run the motor - there is one at the bottom
      runHolderMotor = true;
    } else {
      // if any other condition occurs, do not run the holder motor
      runHolderMotor = false;
    }

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
    if (!runHolderMotor) {
      // if the motor is not running, the command will finish
      return true;
    } else if (m_cellevator.getBeamBreakMiddlePrevious() != m_cellevator.isMiddleBallPresent()) {
      // if the previous middle beam break value is not equal to the current value,
      // set the previous value eqaual to the current oolam value
      m_cellevator.setBeamBreakMiddlePrevious(m_cellevator.isMiddleBallPresent());
      return (!m_cellevator.isMiddleBallPresent() && !m_cellevator.getBeamBreakMiddlePrevious());
    } else {
      // if any other condition occurs, keep running the command
      return false;
    }
  }
}
