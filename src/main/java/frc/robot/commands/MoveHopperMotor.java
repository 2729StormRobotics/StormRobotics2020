/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hopper;
import frc.robot.Constants.HopperConstants;

public class MoveHopperMotor extends CommandBase {
  private final Hopper m_Hopper;
  /**
   * Creates a new MoveHopperMotor.
   */
  public MoveHopperMotor(Hopper subsystems) {
    // Use addRequirements() here to declare subsystem dependencies.
    
    // Creates our local instances of each variable from the parameters
    m_Hopper = subsystems;

    // Attaches our local instance of the subsystem to this command
    addRequirements(m_Hopper);
    

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // sets the hopper motor to run when command starts
    m_Hopper.startHopperMotor(HopperConstants.kHopperMotorSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // stops the hopper motor if command is interupped
    m_Hopper.stopHopperMotor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
