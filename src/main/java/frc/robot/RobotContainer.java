/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import static edu.wpi.first.wpilibj.XboxController.Button.kA;
import static edu.wpi.first.wpilibj.XboxController.Button.kB;
import static edu.wpi.first.wpilibj.XboxController.Button.kBack;
import static edu.wpi.first.wpilibj.XboxController.Button.kBumperLeft;
import static edu.wpi.first.wpilibj.XboxController.Button.kBumperRight;
import static edu.wpi.first.wpilibj.XboxController.Button.kStart;
import static edu.wpi.first.wpilibj.XboxController.Button.kStickRight;
import static edu.wpi.first.wpilibj.XboxController.Button.kY;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.Constants.ControllerConstants;
import frc.robot.commands.CellevatorHolder;
import frc.robot.commands.DriveDistance;
import frc.robot.commands.DriveManually;
import frc.robot.commands.FixedLaunch;
import frc.robot.commands.FrictionBrakeRelease;
import frc.robot.commands.HolderMotorManual;
import frc.robot.commands.HighGearDrive;
import frc.robot.commands.IntakePowerCell;
import frc.robot.commands.InvertHolderMotor;
import frc.robot.commands.InvertLoaderMotor;
import frc.robot.commands.LimelightAlign;
import frc.robot.commands.LowGearDrive;
import frc.robot.commands.MoveClimber;
import frc.robot.commands.MoveHopperMotor;
import frc.robot.commands.MoveIntakePistons;
import frc.robot.commands.PointTurn;
import frc.robot.commands.ToggleLaunchAngle;
import frc.robot.commandgroups.*;
import frc.robot.subsystems.Cellevator;
import frc.robot.subsystems.CellevatorLoader;
import frc.robot.subsystems.Climbers;
import frc.robot.subsystems.ControlPanel;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Launcher;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Party;

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
    m_drivetrain.setDefaultCommand(new DriveManually(() -> m_driverController.getY(Hand.kLeft), () -> m_driverController.getY(Hand.kRight), m_drivetrain));
    // Set default commands once button bindings are finalized

    m_climbers.setDefaultCommand(new MoveClimber(() -> m_weaponsController.getY(Hand.kLeft), m_climbers));

    m_cellevator.setDefaultCommand(new CellevatorHolder(m_cellevator));


    // Add data of each subsystem to the SmartDashboard
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

    // Add Point Turn button and input angle.
    SmartDashboard.putNumber("Target Angle", 0.0);
    SmartDashboard.putData("Point Turn", new PointTurn(m_drivetrain));

    // Add DriveDistance button and input distance.
    SmartDashboard.putNumber("Target Distance", 0.0);
    SmartDashboard.putData("Drive Distance", new DriveDistance(m_drivetrain));

    // Add CellevatorLoaderMotor button.
    SmartDashboard.putData("Cellevator Loader Command", new CellevatorLoader());

    // Add FixedLaunch button and input speed.
    SmartDashboard.putNumber("Target Launch Speed", 0.0);
    SmartDashboard.putData("FixedLaunch", new FixedLaunch(m_launcher));

    // Add LimelightAlign button.
    SmartDashboard.putData("Limelight Align", new LimelightAlign(m_drivetrain));

    // Reverse the loader motors.
    SmartDashboard.putData("Reverse Loader Motor", new InvertLoaderMotor(m_cellevatorLoader));

    // Reverse the holder motors.
    SmartDashboard.putData("Reverse Holder Motor", new InvertHolderMotor(m_cellevator));

    // Auto path 1
    //SmartDashboard.putData("Auto Path 1", new AutoPath1(m_drivetrain, m_launcher, m_intake, m_hopper));

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
    
    new JoystickButton(m_driverController, kA.value).whileHeld(new LimelightAlign(m_drivetrain)); // Limelight align
    
    new JoystickButton(m_driverController, kB.value).whenPressed(new HighGearDrive(m_drivetrain));

    new JoystickButton(m_driverController, kY.value).whenPressed(new LowGearDrive(m_drivetrain));
    
    //new JoystickButton(m_party, kX.value).whenPressed(new )

    new POVButton(m_weaponsController, 0).whenPressed(new ToggleLaunchAngle(m_launcher));

    new JoystickButton(m_weaponsController, kBumperLeft.value).whileHeld(new IntakePowerCell(true, m_intake));   //powercell intake

    new JoystickButton(m_weaponsController, kBumperRight.value).whileHeld(new IntakePowerCell(false, m_intake));   //powercell outtake

    new JoystickButton(m_weaponsController, kA.value).whileHeld(new FrictionBrakeRelease(m_climbers)); //friction break

    new JoystickButton(m_weaponsController, kB.value).whileHeld(new LaunchMode(m_drivetrain, m_launcher, m_cellevatorLoader, m_cellevator, m_hopper));

    new JoystickButton(m_weaponsController, kStart.value).whileHeld(new HolderMotorManual(m_cellevator));

    new JoystickButton(m_weaponsController, kBack.value).whileHeld(new MoveHopperMotor(m_hopper));

    new JoystickButton(m_weaponsController, kStickRight.value).whenPressed(new MoveIntakePistons(m_intake));

   
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
