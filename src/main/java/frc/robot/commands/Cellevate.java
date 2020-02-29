/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Cellevator;

public class Cellevate extends CommandBase {
  private final Cellevator m_cellevator;

  /**
   * Creates a new Cellevate.
   */
  public Cellevate(Cellevator subsystem) {
    m_cellevator = subsystem;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_cellevator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_cellevator.safeToCellelevate()) {
      m_cellevator.cellevate();
    } else {
      m_cellevator.stopCellevator();
    }

    if (m_cellevator.readyToLoad()) {
      m_cellevator.load();
    } else {
      m_cellevator.stopLoader();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_cellevator.stopCellevator();
    m_cellevator.stopLoader();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}