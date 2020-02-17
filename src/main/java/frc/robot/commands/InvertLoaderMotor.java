/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.CellevatorLoader;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class InvertLoaderMotor extends InstantCommand {

  private final CellevatorLoader m_loader;
  public InvertLoaderMotor(CellevatorLoader loader) {
    m_loader = loader;
    addRequirements(loader);;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  /** 
   * inverts the loader motor so that we can unload the power cells out in case of any jams
  */
  public void initialize() {
    m_loader.invertLoader();
  }
}
