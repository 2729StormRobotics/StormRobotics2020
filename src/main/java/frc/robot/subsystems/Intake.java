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

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.IntakeConstants.*;

public class Intake extends SubsystemBase {

  // intake motor that spins the intake wheels
  private final CANSparkMax m_intakeMotor;

  // pistons that raise and lower the intake mechanism
  private final DoubleSolenoid m_intakePiston;

  /**
   * Creates a new Intake.
   */
  public Intake() {

    // Instantiates the motor with the ports and sets the type to burshed motor
    m_intakeMotor = new CANSparkMax(kIntakeMotorPort, MotorType.kBrushed);

    // Instantiates the double solenoid with the raise and lower piston ports
    m_intakePiston = new DoubleSolenoid(kIntakeRaiseSolenoidPort, kIntakeLowerSolenoidPort);

    // initializes the intake motor
    motorInit(m_intakeMotor);

    addChild("Intake Pistons", m_intakePiston);
  }

  //
  public void motorInit(CANSparkMax motor) {
    motor.restoreFactoryDefaults(); // makes sure any changed settings are reset from previous runs
    motor.setIdleMode(IdleMode.kBrake); // sets the motor to brake mode
  }

  // starts the intake motor
  // it is continuous until stopIntakeMotor is called
  public void startIntakeMotor(double speed) {
    m_intakeMotor.set(speed);
  }

  // stops the intake motor
  public void stopIntakeMotor() {
    m_intakeMotor.set(0);
  }

  // raises the intake mechanism using pistons
  public void raiseIntake() {
    m_intakePiston.set(Value.kReverse); // TODO Determine if this is correct
  }

  // lowers the intake mechanism using pistons
  public void lowerIntake() {
    m_intakePiston.set(Value.kForward); // TODO Determine if this is correct
  }

  // displays data on the SmartDashboard
  public void log() {
    // shows if the intake is raised or lowered by the pistons
    SmartDashboard.putData("Intake Position", m_intakePiston); // FIXME This may only display kForward etc
  }

  /*
   * gets the position of the intake pistons if it is lowered, then it will raise
   * the intake and vice versa
   **/
  public void toggleIntakePistons() {
    Value intakeState = m_intakePiston.get();
    if (intakeState == Value.kForward) {
      raiseIntake();
    } else {
      lowerIntake();
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    log();
  }
}
