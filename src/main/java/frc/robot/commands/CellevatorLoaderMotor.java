/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.CellevatorLoader;
import frc.robot.Constants.CellevatorConstants;

public class CellevatorLoaderMotor extends CommandBase {
  
  private final CellevatorLoader m_cellevatorLoader;
  /**
   * Creates a new CellevatorLoader.
   */
  public CellevatorLoaderMotor(CellevatorLoader cellevatorLoader) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_cellevatorLoader = cellevatorLoader;
    addRequirements(m_cellevatorLoader);
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
    if (!m_cellevatorLoader.isBottomBallPresent()) { 
      m_cellevatorLoader.runLoaderMotor(CellevatorConstants.kLoaderMotorSpeed);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_cellevatorLoader.stopLoaderMotor();

    
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (m_cellevatorLoader.isBottomBallPresent()) {
      return true;
  }
  else {
    return false;
  }
  }
}
