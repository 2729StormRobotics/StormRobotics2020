/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Cellevator;
import frc.robot.Constants.CellevatorConstants;

public class CellevatorLoader extends CommandBase {
  Cellevator m_cellevator;
  /**
   * Creates a new CellevatorLoader.
   */
  public CellevatorLoader(Cellevator cellevator) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_cellevator = cellevator;
    addRequirements(m_cellevator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  /** 
   * Every time the command is called run the loader motor if there is no power cell at the bottom of the cellevator
  */
  @Override
  public void execute() {
    if (!m_cellevator.isBottomBallPresent()) { 
      m_cellevator.runLoaderMotor(CellevatorConstants.kLoaderMotorSpeed);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_cellevator.stopLoaderMotor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_cellevator.isBottomBallPresent();
  }
}
