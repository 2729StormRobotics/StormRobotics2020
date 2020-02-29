/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class ClimbManually extends CommandBase {
  private final Climber m_climber;
  private final DoubleSupplier m_speed;

  /**
   * Creates a new ClimbManually.
   */
  public ClimbManually(DoubleSupplier speed, Climber subsystem) {
    m_climber = subsystem;
    m_speed = speed;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_climber.stopClimb();
    m_climber.releaseFrictionBrake();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_climber.atMaxHeight() && m_speed.getAsDouble() > 0) {
      m_climber.stopClimb();
    }
    else if (m_climber.atMinHeight() && m_speed.getAsDouble() < 0) {
      m_climber.stopClimb();
    }
    else {
      m_climber.climb(m_speed.getAsDouble());
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_climber.stopClimb();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
