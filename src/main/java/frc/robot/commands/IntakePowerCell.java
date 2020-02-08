/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class IntakePowerCell extends CommandBase {
  private final Intake m_intake;
  private final double m_speed;
  
  /**
   * Creates a new IntakePowerCell.
   * 
   * @param speed
   * @param subsystem
   */
  public IntakePowerCell(double speed, Intake subsystem) {
    // Create our local instance of each variable from the parameters.
    m_speed = speed;
    m_intake = subsystem;
    
    // Attach our local instance of the subsystem to this command.
    addRequirements(m_intake);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // Once command is initialized, set the intake arm motors to run for intaking the power cells
    m_intake.startIntakeMotor(m_speed);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_intake.stopIntakeMotor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
