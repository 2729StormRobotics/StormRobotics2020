/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.*;
import frc.robot.commandgroups.*;
import frc.robot.subsystems.*;

import static frc.robot.Constants.OIConstants.*;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final Drivetrain m_drivetrain;
  private final Vision m_vision;
  private final Intake m_intake;
  private final Hopper m_hopper;
  private final Launcher m_launcher;
  private final Climber m_climber;
  private final Cellevator m_cellevator;
  private final ControlPanel m_controlPanel;

  private final XboxController m_driver = new XboxController(kDriverControllerPort);
  private final XboxController m_weapons = new XboxController(kWeaponsControllerPort);

  private final SendableChooser<Command> m_autoChooser;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    CameraServer.getInstance().startAutomaticCapture();

    m_drivetrain = new Drivetrain();
    m_vision = new Vision();
    m_intake = new Intake();
    m_hopper = new Hopper();
    m_launcher = new Launcher();
    m_climber = new Climber();
    m_cellevator = new Cellevator();
    m_controlPanel = new ControlPanel();

    m_autoChooser = new SendableChooser<>();
    SmartDashboard.putData("Autonomous Selection", m_autoChooser);
    m_autoChooser.setDefaultOption("Do Nothing", new DoNothingAuto());
    m_autoChooser.addOption("Power Move",
        new AutoPowerMove(m_drivetrain, m_launcher, m_intake, m_hopper, m_cellevator));
    m_autoChooser.addOption("Drive then Shoot",
        new AutoDriveThenShoot(m_drivetrain, m_launcher, m_intake, m_hopper, m_cellevator));
    m_autoChooser.addOption("Shoot then Drive",
        new AutoDriveThenShoot(m_drivetrain, m_launcher, m_intake, m_hopper, m_cellevator));
    m_autoChooser.addOption("Just Drive", new DriveDistance(36, m_drivetrain));

    m_drivetrain.setDefaultCommand(
        new DriveManually(() -> m_driver.getY(Hand.kLeft), () -> m_driver.getY(Hand.kRight), m_drivetrain));

    m_climber.setDefaultCommand(new ClimbManually(() -> m_weapons.getY(Hand.kLeft), m_climber));
    m_cellevator.setDefaultCommand(new Cellevate(m_cellevator));

    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // Driver controls
    new JoystickButton(m_driver, Button.kX.value).whileHeld(new VisionAlign(m_drivetrain));
    new JoystickButton(m_driver, Button.kY.value).whenPressed(new DriveShiftHigh(m_drivetrain));
    new JoystickButton(m_driver, Button.kB.value).whenPressed(new DriveShiftLow(m_drivetrain));
    new JoystickButton(m_driver, Button.kA.value).whenPressed(new DriveReverse(m_drivetrain));

    // Operator controls
    // new JoystickButton(m_weapons, Button.kBumperRight.value).whenPressed(new
    // PowerCellFlow(m_launcher, m_intake, m_hopper, m_cellevator));
    // new JoystickButton(m_weapons, Button.kBumperRight.value).whenReleased(new
    // PowerCellStop(m_launcher, m_intake, m_hopper, m_cellevator));
    // new JoystickButton(m_weapons, Button.kBumperLeft.value).whenPressed(new
    // IntakeAndAgitate(m_intake, m_hopper));
    // new JoystickButton(m_weapons, Button.kBumperLeft.value).whenReleased(new
    // IntakeAndAgitateStop(m_intake, m_hopper));
    new JoystickButton(m_weapons, Button.kBumperRight.value).whenPressed(new IntakeAndAgitate(m_intake, m_hopper));
    new JoystickButton(m_weapons, Button.kBumperRight.value).whenReleased(new IntakeAndAgitateStop(m_intake, m_hopper));
    new JoystickButton(m_weapons, Button.kBumperLeft.value).whenPressed(new IntakeEject(m_intake));
    new JoystickButton(m_weapons, Button.kBumperLeft.value).whenReleased(new IntakeStop(m_intake));

    new JoystickButton(m_weapons, Button.kX.value).whenPressed(new ControlPanelSpinForRevs(m_controlPanel));
    new JoystickButton(m_weapons, Button.kX.value).whenReleased(new ControlPanelStop(m_controlPanel));
    // new JoystickButton(m_weapons, Button.kY.value).whenPressed(new
    // ControlPanelSpinForColor(m_controlPanel));
    // new JoystickButton(m_weapons, Button.kY.value).whenReleased(new
    // ControlPanelStop(m_controlPanel));

    new JoystickButton(m_weapons, Button.kA.value)
        .whenPressed(new LauncherMode(m_launcher, m_intake, m_hopper, m_cellevator));
    new JoystickButton(m_weapons, Button.kA.value)
        .whenReleased(new LauncherModeStop(m_launcher, m_intake, m_hopper, m_cellevator));
    new JoystickButton(m_weapons, Button.kB.value).whenPressed(new IntakeToggle(m_intake));
    new JoystickButton(m_weapons, Button.kStart.value)
        .whenPressed(new LauncherModeWall(m_launcher, m_intake, m_hopper, m_cellevator));
    new JoystickButton(m_weapons, Button.kStart.value)
        .whenReleased(new LauncherModeStop(m_launcher, m_intake, m_hopper, m_cellevator));
    new JoystickButton(m_weapons, Button.kY.value)
        .whenPressed(new LauncherModeTrench(m_launcher, m_intake, m_hopper, m_cellevator));
    new JoystickButton(m_weapons, Button.kY.value)
        .whenReleased(new LauncherModeStop(m_launcher, m_intake, m_hopper, m_cellevator));

    new JoystickButton(m_weapons, Button.kBack.value).whenPressed(new PowerCellEject(m_intake, m_hopper, m_cellevator));
    new JoystickButton(m_weapons, Button.kBack.value)
        .whenReleased(new PowerCellStop(m_launcher, m_intake, m_hopper, m_cellevator));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return (m_autoChooser.getSelected());
  }
}
