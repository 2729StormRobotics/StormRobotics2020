/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.CellevatorConstants.*;

public class Cellevator extends SubsystemBase {
  private final CANSparkMax m_holderMotor;
  private final DigitalInput m_beamBreakTop;
  private final DigitalInput m_beamBreakBottom;
  private final DigitalInput m_beamBreakMiddle;
  private int powerCellCount;
  private boolean previousBBMiddle;

  /**
   * Creates a new Cellevator subsystem
   */
  public Cellevator() {

    m_holderMotor = new CANSparkMax(kHolderMotorPort, MotorType.kBrushed);
    m_beamBreakBottom = new DigitalInput(kBeamBreakLoaderPort);
    m_beamBreakTop = new DigitalInput(kBeamBreakHolderPort);
    m_beamBreakMiddle = new DigitalInput(kBeamBreakMiddlePort);
    powerCellCount = 0;
    previousBBMiddle = false;

    // intializes the motors
    motorInit(m_holderMotor, kHolderMotorInverted);
  }

  private void motorInit(CANSparkMax motor, boolean invert) {
    motor.restoreFactoryDefaults(); // Reset settings in motor in case they are changed
    motor.setIdleMode(IdleMode.kBrake); // Sets the motors to brake mode from the beginning
    motor.setInverted(invert); // Inverts the motor if needed
    motor.setSmartCurrentLimit(kCellevatorCurrentLimit);
  }


  /**
   * Runs the holder motors
   * 
   * @param speed The speed to run the Holder motors
   */
  public void runHolderMotor(double speed) {
    m_holderMotor.set(speed);
  }

  /**
   * Gets the beam break value to see if there is a power cell present at the top
   * of the cellavator
   */
  public boolean isTopBallPresent() {
    return m_beamBreakTop.get();
    }

  /**
   * Gets the beam break value to see if there is a power cell present at the
   * middle of the cellavator
   * sets the previous boolean to the value so that it is stroed for the next time that this method is called
   */
  public boolean isMiddleBallPresent() {
    previousBBMiddle = m_beamBreakMiddle.get();
    return m_beamBreakMiddle.get();
    }

  /**
   * Gets the beam break value to see if there is a power cell present at the
   * bottom of the cellavator
   */
  public boolean isBottomBallPresent() {
    return m_beamBreakBottom.get();
  }

  /**
   * Stops the holder motor
   */
  public void stopHolderMotor() {
    runHolderMotor(0);
  }

  /**
   * adds to the count
   * @return
   */
  public int addPowerCellCount() {
    powerCellCount += 1;
    return powerCellCount;
  }
  /**
   * subtracts from the count 
   * @return
   */
  public int subtractPowerCellCount() {
    powerCellCount -= 1;
    return powerCellCount;
  }

  /**
   * displays data onto SmartDashboard
   */
  public void log() {
    SmartDashboard.putBoolean("Top Beam Value", m_beamBreakTop.get());
    SmartDashboard.putBoolean("Bottom Beam Value", m_beamBreakBottom.get());
    SmartDashboard.putBoolean("Middle Beam Value", m_beamBreakMiddle.get());
    SmartDashboard.putNumber("Holder Motor Speed", m_holderMotor.get());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    log();
  }
}
