/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.IntakeConstants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Intake extends SubsystemBase {
  private final CANSparkMax m_intakeMotor;
  private final DoubleSolenoid m_intakePistons;

  /**
   * Creates a new Intake.
   */
  public Intake() {
    m_intakeMotor = new CANSparkMax(kIntakeMotorPort, MotorType.kBrushed);
    m_intakePistons = new DoubleSolenoid(kIntakeRaiseChannel, kIntakeLowerChannel);

    m_intakeMotor.restoreFactoryDefaults();
    m_intakeMotor.setIdleMode(IdleMode.kCoast);
    m_intakeMotor.setInverted(true);
  }

  // starts the intake motor
  // it is continuous until stopIntakeMotor is called
  public void runIntake(double speed) {
    m_intakeMotor.set(speed);
  }

  public void intake() {
    runIntake(kIntakeMotorSpeed);
  }

  // stops the intake motor
  public void stopIntake() {
    m_intakeMotor.set(0);
  }

  // raises the intake mechanism using pistons
  public void raiseIntake() {
    m_intakePistons.set(kIntakeRaiseValue);
  }

  // lowers the intake mechanism using pistons
  public void lowerIntake() {
    m_intakePistons.set(kIntakeLowerValue);
  }

  public void toggleIntakePistons() {
    if (isIntakeLowered()) {
      raiseIntake();
    } else {
      lowerIntake();
    }
  }

  public boolean isIntakeLowered() {
    if (m_intakePistons.get() == kIntakeLowerValue) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
