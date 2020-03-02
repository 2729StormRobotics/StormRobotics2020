/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.IntakeConstants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Intake extends SubsystemBase {
  private final CANSparkMax m_intakeMotor;
  private final DoubleSolenoid m_intakePistons;

  private final NetworkTable m_intakeTable;
  private final NetworkTableEntry m_intakeStatus;

  /**
   * Creates a new Intake.
   */
  public Intake() {
    m_intakeMotor = new CANSparkMax(kIntakeMotorPort, MotorType.kBrushed);
    m_intakePistons = new DoubleSolenoid(kIntakeRaiseChannel, kIntakeLowerChannel);

    m_intakeMotor.restoreFactoryDefaults();
    m_intakeMotor.setIdleMode(IdleMode.kCoast);
    m_intakeMotor.setInverted(true);

    m_intakeTable = NetworkTableInstance.getDefault().getTable("Power Cells");
    m_intakeStatus = m_intakeTable.getEntry("Intake Running");
  }

  // starts the intake motor
  // it is continuous until stopIntakeMotor is called
  public void runIntake(double speed) {
    m_intakeStatus.setBoolean(true);
    m_intakeMotor.set(speed);
  }

  public void intake() {
    runIntake(kIntakeMotorSpeed);
  }

  public void eject() {
    runIntake(kEjectMotorSpeed);
  }

  // stops the intake motor
  public void stopIntake() {
    m_intakeMotor.set(0);
    m_intakeStatus.setBoolean(false);
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
