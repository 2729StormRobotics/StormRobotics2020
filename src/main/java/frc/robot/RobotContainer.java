/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.commands.*;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import static edu.wpi.first.wpilibj.GenericHID.Hand;
import static edu.wpi.first.wpilibj.XboxController.Button.*;
import static frc.robot.Constants.*;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final Cellevator m_cellevator = new Cellevator();
  private final CellevatorLoader m_cellevatorLoader = new CellevatorLoader();
  private final Climbers m_climbers = new Climbers();
  private final ControlPanel m_controlPanel = new ControlPanel();
  private final Drivetrain m_drivetrain = new Drivetrain();
  private final Hopper m_hopper = new Hopper();
  private final Intake m_intake = new Intake();
  private final Launcher m_launcher = new Launcher();
  private final Limelight m_limelight = new Limelight();
  private final Party m_party = new Party();

  private final XboxController m_driverController = new XboxController(ControllerConstants.kDriverControlPort);
  private final XboxController m_weaponsController = new XboxController(ControllerConstants.kWeaponsControlPort);

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Set default commands once button bindings are finalized
    SmartDashboard.putData("Cellevator Subsystem", m_cellevator);
    SmartDashboard.putData("Cellevator Loader Subsystem", m_cellevatorLoader);
    SmartDashboard.putData("Climbers Subsystem", m_climbers);
    SmartDashboard.putData("Control Panel Subsystem", m_controlPanel);
    SmartDashboard.putData("Drivetrain Subsystem", m_drivetrain);
    SmartDashboard.putData("Hopper Subsystem", m_hopper);
    SmartDashboard.putData("Intake Subsystem", m_intake);
    SmartDashboard.putData("Launcher Subsystem", m_launcher);
    SmartDashboard.putData("Limelight Subsystem", m_limelight);
    SmartDashboard.putData("Party Subsystem", m_party);

    // Show what command each subsystem is running on the SmartDashboard

    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // Set button bindings here once they are decided upon and finalized.
  }

public void updateDashboard() {
  m_cellevator.log();
  m_cellevatorLoader.log();
  m_climbers.log();
  // Control Panel log does not exist, but it may not be necessary to have.
  m_drivetrain.log();
  m_hopper.log();
  m_intake.log();
  m_launcher.log();
  m_limelight.log();
  // Party log does not exist, but it may not be necessary to have.
}

  //commented out because no auto yet
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  /*public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_autoCommand;
  }*/
}
