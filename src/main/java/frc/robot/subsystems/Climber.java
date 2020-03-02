/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import static frc.robot.Constants.ClimberConstants.*;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Climber extends SubsystemBase {
  private final WPI_TalonSRX m_climberMotor;

  private final Solenoid m_frictionBrake;

  private final ShuffleboardTab m_testingTab;
  private final ShuffleboardLayout m_climberStatus;

  private final NetworkTable m_PartyTable;
  private final NetworkTableEntry m_ClimbPartyStatus;


  /**
   * Creates a new Climber subsystem.
   */
  public Climber() {
    m_climberMotor = new WPI_TalonSRX(kClimberMotorPort);

    m_frictionBrake = new Solenoid(kFrictionBrakeChannel);

    motorInit(m_climberMotor, kMotorInverted);

    m_frictionBrake.set(kFrictionBrakeEnabled);

    m_testingTab = Shuffleboard.getTab(kShuffleboardTab);
    m_climberStatus = m_testingTab.getLayout("Climber Status", BuiltInLayouts.kList);

    shuffleboardInit();

    m_PartyTable = NetworkTableInstance.getDefault().getTable("Party Statuses");
    m_ClimbPartyStatus = m_PartyTable.getEntry("Climb Status");

  }

  /**
   * Initialize our motor.
   * 
   * @param motor  The SparkMAX motor to initialize
   * @param invert Whether the motor should be inverted
   */
  private void motorInit(WPI_TalonSRX motor, boolean invert) {
    motor.configFactoryDefault(); // Restores the default values in case something persists
    motor.setNeutralMode(NeutralMode.Brake); //motor mode to brake mode
    motor.setInverted(invert); // Invert the motor if needed.
    encoderInit(motor); // Initialize the encoder.
  }

  /**
   * Initialize our encoder.
   * 
   * @param encoder The SparkMAX encoder to initialize
   */
  private void encoderInit(WPI_TalonSRX motor) {
    motor.configSelectedFeedbackSensor(FeedbackDevice.SoftwareEmulatedSensor);

    // Sets the conversion factor for the encoder
    motor.configSelectedFeedbackCoefficient(kDistancePerPulse);
    encoderReset(motor);
  }

  /**
   * Set encoder position to 0.
   * 
   * @param encoder The SparkMAX encoder to reset
   */
  private void encoderReset(WPI_TalonSRX motor) {
    motor.setSelectedSensorPosition(0);
  }

  /**
   * Standard method to drive motor
   * 
   * @param speed The motor speed to drive the climber
   */
  public void climb(double speed) {
    m_climberMotor.set(-speed);
    if (Math.abs(speed) >= 0.05) {
      m_ClimbPartyStatus.setBoolean(true);
    } else {
      m_ClimbPartyStatus.setBoolean(false);
    }
  }

  /**
   * Runs the motor at a specified "up" speed
   */
  public void climbUp() {
    climb(kClimbUpSpeed);
  }

  /**
   * Runs the motor at a specified "down" speed
   */
  public void climbDown() {
    climb(kClimbDownSpeed);
  }

  /**
   * Stop the motor
   */
  public void stopClimb() {
    // Sets the speed of the motor to 0.
    climb(0.0);
  }

  /**
   * Get the current height
   * 
   * @return The current height, in inches
   */
  private double getHeight() {
    return m_climberMotor.getSelectedSensorPosition();
  }

  /**
   * Get the current speed
   * 
   * @return The current speed, in inches per second
   */
  private double getSpeed() {
    return m_climberMotor.getSelectedSensorVelocity();
  }

  /**
   * Determine if the climber is above its max height
   * 
   * @return true if the current height is above its maximum
   */
  public boolean atMaxHeight() {
    return getHeight() > kMaxHeight;
  }

  /**
   * Determine if the climber is below its min height
   * 
   * @return true if the current height is below its minimum
   */
  public boolean atMinHeight() {
    return getHeight() < 0;
  }

  /**
   * Engage the friction brake
   */
  public void engageFrictionBrake() {
    m_frictionBrake.set(kFrictionBrakeEnabled);
  }

  /**
   * Release the friction brake
   */
  public void releaseFrictionBrake() {
    m_frictionBrake.set(kFrictionBrakeDisabled);
  }

  private void shuffleboardInit() {
    // Adds the climber data to the Shuffleboard in its own tab.
    m_climberStatus.addNumber("Height", () -> getHeight()).withWidget(BuiltInWidgets.kNumberBar)
        .withProperties(Map.of("Min", -1)).withProperties(Map.of("Max", 23));
    m_climberStatus.addNumber("Speed", () -> getSpeed());

    m_climberStatus.add("Friction Brake", m_frictionBrake);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
