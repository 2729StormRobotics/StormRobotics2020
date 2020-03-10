/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.LauncherConstants.*;

import java.util.Map;

public class Launcher extends SubsystemBase {
  private final WPI_TalonSRX m_leftMotor;
  private final WPI_TalonSRX m_rightMotor;

  private final SimpleMotorFeedforward m_feedForward;

  private final DoubleSolenoid m_launcherAnglePistons;

  private final NetworkTable m_limelight;

  private final ShuffleboardTab m_testingTab;
  private final ShuffleboardLayout m_launcherStatus;

  private final NetworkTable m_PartyTable;
  private final NetworkTableEntry m_RevStatus;
  private final NetworkTableEntry m_LongLaunchAngleStatus;
  private final NetworkTableEntry m_ShortLaunchAngleStatus;

  private String m_launchType = "Disabled";

  /**
   * Creates a new Launcher.
   */
  public Launcher() {
    // Instantiate the double solenoid for the launch pistons.
    m_launcherAnglePistons = new DoubleSolenoid(kLongAngleChannel, kShortAngleChannel);

    // Instantiate the motors.
    m_leftMotor = new WPI_TalonSRX(kLeftMotorPort);
    m_rightMotor = new WPI_TalonSRX(kRightMotorPort);

    // Initialize the the motors.
    motorInit(m_leftMotor);
    motorInit(m_rightMotor);

    // Make left motor follow the right motor and set it to inverted.
    m_leftMotor.follow(m_rightMotor);
    m_leftMotor.setInverted(InvertType.OpposeMaster);

    m_feedForward = new SimpleMotorFeedforward(kS, kV, kA);

    // Initialize pid coefficients
    pidInit();

    m_PartyTable = NetworkTableInstance.getDefault().getTable("Party Statuses");
    m_RevStatus = m_PartyTable.getEntry("Revved");
    m_LongLaunchAngleStatus = m_PartyTable.getEntry("Long Launch Angle");
    m_ShortLaunchAngleStatus = m_PartyTable.getEntry("Short Launch Angle");

    // Initialize pistons
    pistonInit();

    // Instantiate the limelight NetworkTable
    m_limelight = NetworkTableInstance.getDefault().getTable("limelight");

    m_testingTab = Shuffleboard.getTab("Testing");
    m_launcherStatus = m_testingTab.getLayout("Launcher Status", BuiltInLayouts.kList)
        .withProperties(Map.of("Label position", "TOP"));

    shuffleboardInit();
  }

  /**
   * Restore factory defaults, set idle mode to coast, and invert motor.
   * 
   * @param motor The motor to initialize
   */
  private void motorInit(WPI_TalonSRX motor) {
    motor.configFactoryDefault(); // Restores the default values in case something persists
    motor.setNeutralMode(NeutralMode.Brake); // motor mode to brake mode
    encoderInit(motor);
  }

  /**
   * Initialize an encoder.
   * 
   * @param encoder The encoder to initialize
   */
  private void encoderInit(WPI_TalonSRX encoder) {
    encoder.configSelectedFeedbackSensor(FeedbackDevice.SoftwareEmulatedSensor);
    encoder.configSelectedFeedbackCoefficient(kVelocityConversion);
    encoderReset(encoder); // Reset the encoder to 0, just in case
  }

  /**
   * Reset an encoder's position.
   * 
   * @param encoder The encoder to reset
   */
  private void encoderReset(WPI_TalonSRX encoder) {
    encoder.setSelectedSensorPosition(0);
  }

  /**
   * Set the PID coefficients for the PID Controller to use.
   */
  private void pidInit() {
    // Set the proportional constant.
    m_rightMotor.config_kP(0, LauncherPID.kP);
  }

  /**
   * Set the pistons to be retracted initially.
   */
  private void pistonInit() {
    setLongLaunchAngle();
  }

  /**
   * Set the launch pistons to long shot angle.
   */
  public void setLongLaunchAngle() {
    m_launcherAnglePistons.set(kLongLaunchValue);
    m_launchType = "Long Shot";
    m_LongLaunchAngleStatus.setBoolean(true);

  }

  /**
   * Set the launch pistons to short shot angle.
   */
  public void setShortLaunchAngle() {
    m_launcherAnglePistons.set(kShortLaunchValue);
    m_launchType = "Wall Shot";
    m_ShortLaunchAngleStatus.setBoolean(true);
  }

  /**
   * Toggles the launch pistons.
   */
  public void toggleLaunchAngle() {
    if (m_launcherAnglePistons.get() == kLongLaunchValue) {
      setShortLaunchAngle();
    } else {
      setLongLaunchAngle();
    }
  }

  /**
   * Stop the launcher motors.
   */
  public void stopLauncher() {
    m_rightMotor.set(0);
  }

  /**
   * Get the speed of the motors.
   * 
   * @return The speed of the right motor in RPM.
   */
  public double getRightLauncherSpeed() {
    return m_rightMotor.getSelectedSensorVelocity();
  }

  /**
   * Set the motor controller to start the PID controller.
   * 
   * @param speed The target angular speed in RPS
   */
  public void revToSpeed(double speed) {
    double revSpeed = speed;
    if (revSpeed > kMaxShotSpeed) {
      revSpeed = kMaxShotSpeed;
      m_RevStatus.setBoolean(true);
    }

    double feedforward = m_feedForward.calculate(revSpeed);
    m_rightMotor.set(ControlMode.Velocity, revSpeed, DemandType.ArbitraryFeedForward, feedforward / 12.0);
  }

  public void revWallShot() {
    revToSpeed(kWallShotSpeed);
    m_RevStatus.setBoolean(true);
  }

  public void revTrenchShot() {
    revToSpeed(kTrenchShotSpeed);
  }

  /**
   * Get the horizontal distance from the vision target from network tables
   * 
   * @return the distance.
   */
  private double getDistanceToTarget() {
    return m_limelight.getEntry("Target Distance").getDouble(0.0);
  }

  /**
   * Calculate the desired speed of the launch motors.
   * 
   * @param distance The horizontal distance from the vision target
   * @return The desired speed of the launch motors in RPS.
   */
  public double calculateLaunchSpeed() {
    double distance = getDistanceToTarget();
    double launchSpeed = 0.235896 * distance + 33.8493;

    if (launchSpeed < 0) {
      launchSpeed = 0;
    }

    return launchSpeed;
  }

  /**
   * Calculate the desired speed of the launch motors.
   * 
   * @param distance The horizontal distance from the vision target
   * @return The desired speed of the launch motors in RPS.
   */
  public double calculateLaunchSpeedQuad() {
    double distance = getDistanceToTarget();
    double launchSpeed = -0.00220687 * Math.pow(distance, 2) + 0.843151 * distance - 7.62407;

    if (launchSpeed < 0) {
      launchSpeed = 0;
    }

    return launchSpeed;
  }

  /**
   * Initialize the shuffleboard data
   */
  private void shuffleboardInit() {
    m_launcherStatus.addNumber("Rev Speed", () -> getRightLauncherSpeed() * 60.0);
    m_launcherStatus.addString("Launch Type", () -> m_launchType);
    m_launcherStatus.addNumber("Target Speed", () -> calculateLaunchSpeedQuad() * 60.0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
