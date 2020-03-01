/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

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
  private final CANSparkMax m_leftMotor;
  private final CANSparkMax m_rightMotor;

  private final CANEncoder m_leftEncoder;
  private final CANEncoder m_rightEncoder;

  private final SimpleMotorFeedforward m_feedForward;
  private final CANPIDController m_pidController;

  private final DoubleSolenoid m_launcherAnglePistons;

  private final NetworkTable m_limelight;

  private final ShuffleboardTab m_launcherTab;
  private final ShuffleboardLayout m_launcherStatus;

  private final NetworkTable m_PartyTable;
  private final NetworkTableEntry m_RevStatus;
  private final NetworkTableEntry m_LaunchAngleStatus;

  private String m_launchType = "Disabled";

  /**
   * Creates a new Launcher.
   */
  public Launcher() {
    // Instantiate the double solenoid for the launch pistons.
    m_launcherAnglePistons = new DoubleSolenoid(kLongAngleChannel, kShortAngleChannel);

    // Instantiate the motors.
    m_leftMotor = new CANSparkMax(kLeftMotorPort, MotorType.kBrushless);
    m_rightMotor = new CANSparkMax(kRightMotorPort, MotorType.kBrushless);

    // Instantiate the encoder.
    m_leftEncoder = m_leftMotor.getEncoder();
    m_rightEncoder = m_rightMotor.getEncoder();

    // Initialize the the motors.
    motorInit(m_leftMotor);
    motorInit(m_rightMotor);

    // Make right motor follow the left motor and set it to inverted.
    m_leftMotor.follow(m_rightMotor, true);

    m_feedForward = new SimpleMotorFeedforward(kS, kV, kA);

    // Initialize the PID controller for the motor controller.
    m_pidController = m_rightMotor.getPIDController();

    // Initialize pid coefficients
    pidInit();

    // Initialize pistons
    pistonInit();

    // Instantiate the limelight NetworkTable
    m_limelight = NetworkTableInstance.getDefault().getTable("limelight");

    m_launcherTab = Shuffleboard.getTab(kShuffleboardTab);
    m_launcherStatus = m_launcherTab.getLayout("Launcher Status", BuiltInLayouts.kList)
        .withProperties(Map.of("Label position", "TOP"));

    shuffleboardInit();

    m_PartyTable = NetworkTableInstance.getDefault().getTable("Party Statuses");
    m_RevStatus = m_PartyTable.getEntry("Revved");
    m_LaunchAngleStatus = m_PartyTable.getEntry("Launch Angle Toggled");



  }

  /**
   * Restore factory defaults, set idle mode to coast, and invert motor.
   * 
   * @param motor The motor to initialize
   */
  private void motorInit(CANSparkMax motor) {
    motor.restoreFactoryDefaults(); // Just in case any settings persist between reboots.
    motor.setIdleMode(IdleMode.kCoast); // Set the motor to coast mode, so that we don't lose momentum when we stop
    encoderInit(motor.getEncoder());
  }

  /**
   * Initialize an encoder.
   * 
   * @param encoder The encoder to initialize
   */
  private void encoderInit(CANEncoder encoder) {
    encoder.setVelocityConversionFactor(kVelocityConversion);
    encoderReset(encoder); // Reset the encoder to 0, just in case
  }

  /**
   * Reset an encoder's position.
   * 
   * @param encoder The encoder to reset
   */
  private void encoderReset(CANEncoder encoder) {
    encoder.setPosition(0);
  }

  /**
   * Set the PID coefficients for the PID Controller to use.
   */
  private void pidInit() {
    // Set the proportional constant.
    m_pidController.setP(LauncherPID.kP);
    // Set the integral constant.
    m_pidController.setI(LauncherPID.kI);
    // Set the derivative constant.
    m_pidController.setD(LauncherPID.kD);
    // Set the integral zone. This value is the maximum |error| for the integral
    // gain to take effect.
    m_pidController.setIZone(LauncherPID.kIz);
    // Set the feed-forward constant.
    m_pidController.setFF(LauncherPID.kF);
    // Set the output range.
    m_pidController.setOutputRange(LauncherPID.kMinOutput, LauncherPID.kMaxOutput);
  }

  public void pidAdjust() {
    m_pidController.setP(LauncherPID.kP);
    m_pidController.setI(LauncherPID.kI);
    m_pidController.setD(LauncherPID.kD);
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
    // m_LaunchAngleStatus.setBoolean(true);
  }

  /**
   * Set the launch pistons to short shot angle.
   */
  public void setShortLaunchAngle() {
    m_launcherAnglePistons.set(kShortLaunchValue);
    m_launchType = "Wall Shot";
    // m_LaunchAngleStatus.setBoolean(true);
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
   * Get the speed of the left motor.
   * 
   * @return The speed of the left motor in RPM.
   */
  public double getLeftLauncherSpeed() {
    return m_leftEncoder.getVelocity() / kVelocityConversion;
  }

  /**
   * Get the speed of the right motor.
   * 
   * @return The speed of the right motor in RPM.
   */
  public double getRightLauncherSpeed() {
    return m_rightEncoder.getVelocity() / kVelocityConversion;
  }

  /**
   * Get the average speed of the launcher motors.
   * 
   * @return The average speed of both launcher motors in RPM.
   */
  public double getLauncherAvgSpeed() {
    return ((getLeftLauncherSpeed() + getRightLauncherSpeed()) / 2.0);
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
    m_pidController.setReference(revSpeed, ControlType.kVelocity, 0, feedforward);
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
    double launchSpeed = -0.00220687 * Math.pow(distance, 2) + 0.843151 * distance -7.62407;

    if (launchSpeed < 0) {
      launchSpeed = 0;
    }

    return launchSpeed;
  }

  /**
   * Initialize the shuffleboard data
   */
  private void shuffleboardInit() {
    m_launcherStatus.addNumber("Rev Speed", () -> getLauncherAvgSpeed());
    m_launcherStatus.addString("Launch Type", () -> m_launchType);
    m_launcherStatus.addNumber("Target Speed", () -> calculateLaunchSpeed());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
