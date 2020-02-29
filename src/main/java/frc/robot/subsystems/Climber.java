/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import static frc.robot.Constants.ClimberConstants.*;

import java.util.Map;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

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
  private final CANSparkMax m_climberMotor;

  private final CANEncoder m_encoder;

  private final Solenoid m_frictionBrake;
  private boolean m_retracted = true;

  private final ShuffleboardTab m_climberTab;
  private final ShuffleboardLayout m_climberStatus;

  private final NetworkTable m_PartyTable;
  private final NetworkTableEntry m_FrictionBrakeStatus;


  /**
   * Creates a new Climber subsystem.
   */
  public Climber() {
    m_climberMotor = new CANSparkMax(kClimberMotorPort, MotorType.kBrushless);

    m_frictionBrake = new Solenoid(kFrictionBrakeChannel);

    motorInit(m_climberMotor, kMotorInverted);

    m_encoder = m_climberMotor.getEncoder();

    m_frictionBrake.set(kFrictionBrakeDisabled);

    m_climberTab = Shuffleboard.getTab(kShuffleboardTab);
    m_climberStatus = m_climberTab.getLayout("Climber Status", BuiltInLayouts.kList);

    shuffleboardInit();

    m_PartyTable = NetworkTableInstance.getDefault().getTable("Party Statuses");
    m_FrictionBrakeStatus = m_PartyTable.getEntry("Friction Brake Engaged");

  }

  /**
   * Initialize our motor.
   * 
   * @param motor  The SparkMAX motor to initialize
   * @param invert Whether the motor should be inverted
   */
  private void motorInit(CANSparkMax motor, boolean invert) {
    motor.restoreFactoryDefaults(); // Restores the default values in case something persists
    motor.setIdleMode(IdleMode.kBrake); // Set motor mode to brake mode
    motor.setInverted(invert); // Invert the motor if needed.
    encoderInit(motor.getEncoder()); // Initialize the encoder.
  }

  /**
   * Initialize our encoder.
   * 
   * @param encoder The SparkMAX encoder to initialize
   */
  private void encoderInit(CANEncoder encoder) {
    // Sets the conversion factor for the encoder
    encoder.setPositionConversionFactor(kDistancePerPulse);
    encoder.setVelocityConversionFactor(kSpeedPerPulse);
    encoderReset(encoder);
  }

  /**
   * Set encoder position to 0.
   * 
   * @param encoder The SparkMAX encoder to reset
   */
  private void encoderReset(CANEncoder encoder) {
    encoder.setPosition(0);
  }

  /**
   * Standard method to drive motor
   * 
   * @param speed The motor speed to drive the climber
   */
  public void climb(double speed) {
    m_climberMotor.set(-speed);
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
    return m_encoder.getPosition();
  }

  /**
   * Get the current speed
   * 
   * @return The current speed, in inches per second
   */
  private double getSpeed() {
    return m_encoder.getVelocity();
  }

  /**
   * Determine if the climber is above its max height
   * 
   * @return true if the current height is above its maximum
   */
  public boolean atMaxHeight() {
    return m_encoder.getPosition() > kMaxHeight;
  }

  /**
   * Determine if the climber is below its min height
   * 
   * @return true if the current height is below its minimum
   */
  public boolean atMinHeight() {
    return m_encoder.getPosition() < 0;
  }

  /**
   * Engage the friction brake
   */
  public void engageFrictionBrake() {
    m_frictionBrake.set(kFrictionBrakeEnabled);
    m_FrictionBrakeStatus.setBoolean(true);
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
    m_climberStatus.addBoolean("Fully Retracted", () -> m_retracted);

    m_climberStatus.add("Friction Brake", m_frictionBrake);
  }

  @Override
  public void periodic() {
    if (getHeight() > 1) {
      m_retracted = false;
    } else {
      m_retracted = true;
    }
    // This method will be called once per scheduler run
  }
}
