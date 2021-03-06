/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Cellevator;

public class CellevateContinuous extends CommandBase {
  private final Cellevator m_cellevator;

  /**
   * Creates a new CellevateContinuous.
   */
  public CellevateContinuous(Cellevator subsystem) {
    m_cellevator = subsystem;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_cellevator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_cellevator.load();
    m_cellevator.cellevate();
  }

  @Override
  public void execute() {
  }

  @Override
  public void end(boolean interrupted) {
    m_cellevator.stopCellevator();
    m_cellevator.stopLoader();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
