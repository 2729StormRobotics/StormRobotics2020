/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import static frc.robot.Constants.ClimberConstants.*;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
  private final CANSparkMax m_leftClimber;
  private final CANSparkMax m_rightClimber;

  private final CANEncoder m_leftEncoder;
  private final CANEncoder m_rightEncoder;

  private final Solenoid m_frictionBrake;

  private final SpeedControllerGroup m_climberMotors;

  private final ShuffleboardTab m_climberTab;
  private final ShuffleboardLayout m_climberHeights;
  private final ShuffleboardLayout m_climberSpeeds;
  private final ShuffleboardLayout m_climberStatus;

  /**
   * Creates a new Climber.
   */
  public Climber() {
    m_leftClimber = new CANSparkMax(kLeftClimberMotorPort, MotorType.kBrushless);
    m_rightClimber = new CANSparkMax(kRightClimberMotorPort, MotorType.kBrushless);

    m_frictionBrake = new Solenoid(kFrictionSolenoidPort);

    motorInit(m_leftClimber, kLeftClimberMotorInverted);
    motorInit(m_rightClimber, kRightClimberMotorInverted);

    m_climberMotors = new SpeedControllerGroup(m_leftClimber, m_rightClimber);

    m_leftEncoder = m_leftClimber.getEncoder();
    m_rightEncoder = m_rightClimber.getEncoder();

    m_frictionBrake.set(kFrictionBrakeDisabled);

    m_climberTab = Shuffleboard.getTab(kShuffleboardTab);
    m_climberHeights = m_climberTab.getLayout("Climber Heights", BuiltInLayouts.kList).withSize(2, 3)
        .withPosition(2, 0);
    m_climberSpeeds = m_climberTab.getLayout("Climber Speeds", BuiltInLayouts.kList).withSize(2, 3).withPosition(4,
        0);
    m_climberStatus = m_climberTab.getLayout("Climber Status", BuiltInLayouts.kList).withSize(2, 3).withPosition(0,
        0);

    shuffleboardInit();
  }

  private void motorInit(CANSparkMax motor, boolean invert) {
    motor.restoreFactoryDefaults(); // Restores the default values in case something stayed from a previous reboot.
    motor.setIdleMode(IdleMode.kBrake); // Set motor mode to brake mode
    motor.setInverted(invert); // Invert the motor if needed.
    encoderInit(motor.getEncoder()); // Initialize the encoder.
  }

  private void encoderInit(CANEncoder encoder) {
    /*
     * Sets the conversion factor for the encoder. This allows the encoder to output
     * the specified unit.
     */
    encoder.setPositionConversionFactor(kEncoderDistancePerPulse);
    encoder.setVelocityConversionFactor(kEncoderSpeedPerPulse);
    encoderReset(encoder); // Calls the encoderReset method
  }

  private void encoderReset(CANEncoder encoder) {
    // Resets encoder value to 0.
    encoder.setPosition(0);
  }

  public void climb(double speed) {
    m_leftClimber.set(speed); // The motor goes at a speed given to it. Not a specific speed
  }

  public void climbUp() {
    climb(kClimbUpSpeed);
  }

  public void climbDown() {
    climb(kClimbDownSpeed);
  }

  public void stopClimb() {
    // Sets the speed of the motor to 0.
    climb(0.0);
  }

  private double getRightDistance() {
    return m_rightEncoder.getPosition(); // Get the position of the right encoder.
  }

  private double getLeftDistance() {
    return m_leftEncoder.getPosition(); // Get the position of the left encoder.
  }

  public double getAverageDistance() {
    return ((getRightDistance() + getLeftDistance()) / 2); // Finds the average of the encoders.
  }

  public void engageFrictionBrake() {
    m_frictionBrake.set(kFrictionBrakeEnabled); // Sets frictionBreak to the value inputted.
  }

  public void disengageFrictionBrake() {
    m_frictionBrake.set(kFrictionBrakeDisabled);
  }

  private void shuffleboardInit() {
    // Puts the climber data on the SmartDashboard.
    m_climberHeights.addNumber("Left", () -> getLeftDistance());
    m_climberHeights.addNumber("Right", () -> getRightDistance());
    m_climberHeights.addNumber("Average", () -> getAverageDistance());

    m_climberSpeeds.addNumber("Left Speed", () -> getLeftDistance());
    m_climberSpeeds.addNumber("Right Speed", () -> getRightDistance());
    m_climberSpeeds.addNumber("Average Speed", () -> getAverageDistance());

    m_climberStatus.add("Friction Brake", m_frictionBrake).withWidget(BuiltInWidgets.kToggleButton);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
