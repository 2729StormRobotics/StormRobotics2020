/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import static frc.robot.Constants.CellevatorConstants.*;

import java.util.Map;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Cellevator extends SubsystemBase {
  private final CANSparkMax m_loaderMotor;
  private final CANSparkMax m_cellevatorMotor;

  private final DigitalInput m_ballDetectTop;
  private final DigitalInput m_ballDetectBottom;
  private final DigitalInput m_ballDetectMiddle;
  private final DigitalInput m_ballDetectFeed;

  private final ShuffleboardTab m_cellevatorTab;
  private final ShuffleboardLayout m_cellevatorConditions;

  private int m_powerCellCount;
  private boolean m_previousBBMiddle;
  private boolean m_previousBBBottom;
  private boolean m_previousBBTop;

  /**
   * Creates a new Cellevator.
   */
  public Cellevator() {
    m_cellevatorMotor = new CANSparkMax(kCellevatorMotorPort, MotorType.kBrushed);
    m_loaderMotor = new CANSparkMax(kLoaderMotorPort, MotorType.kBrushed);

    motorInit(m_cellevatorMotor, kHolderMotorInverted);
    motorInit(m_loaderMotor, kLoaderMotorInverted);

    m_ballDetectBottom = new DigitalInput(kBeamBreakLoaderPort);
    m_ballDetectTop = new DigitalInput(kBeamBreakHolderPort);
    m_ballDetectMiddle = new DigitalInput(kBeamBreakMiddlePort);
    m_ballDetectFeed = new DigitalInput(kBallFeedPort);

    m_powerCellCount = 0;
    m_previousBBMiddle = false;
    m_previousBBBottom = false;
    m_previousBBTop = false;

    m_cellevatorTab = Shuffleboard.getTab(kShuffleboardTab);
    m_cellevatorConditions = m_cellevatorTab.getLayout("Power Cells", BuiltInLayouts.kList)
        .withProperties(Map.of("Label position", "TOP"));
    shuffleboardInit();

  }

  private void motorInit(CANSparkMax motor, boolean invert) {
    motor.restoreFactoryDefaults(); // Reset settings in motor in case they are changed
    motor.setIdleMode(IdleMode.kBrake); // Sets the motors to brake mode from the beginning
    motor.setInverted(invert); // Inverts the motor if needed
    motor.setSmartCurrentLimit(kCellevatorCurrentLimit);
  }

  /**
   * Runs the Cellevator motors
   * 
   * @param speed The speed to run the Cellevator motors
   */
  public void runCellevator(double speed) {
    m_cellevatorMotor.set(speed);
  }

  public void cellevate() {
    runCellevator(kCellevatorMotorSpeed);
  }

  /**
   * Stops the Cellevator motor
   */
  public void stopCellevator() {
    runCellevator(0);
  }

  /**
   * Runs the Loader motor
   * 
   * @param speed The speed to run the Cellevator motors
   */
  public void runLoader(double speed) {
    m_loaderMotor.set(speed);
  }

  public void load() {
    runLoader(kLoaderMotorSpeed);
  }

  /**
   * Stops the Cellevator motor
   */
  public void stopLoader() {
    runLoader(0);
  }

  /**
   * inverts the Cellevator to unload power cells
   */
  public void invertCellevator() {
    m_cellevatorMotor.setInverted(!m_cellevatorMotor.getInverted());
    m_loaderMotor.setInverted(!m_loaderMotor.getInverted());
  }

  /**
   * Gets the beam break value to see if there is a power cell present at the top
   * of the cellevator
   */
  public boolean isTopBallPresent() {
    return !m_ballDetectTop.get();
  }

  /**
   * Gets the beam break value to see if there is a power cell present at the
   * middle of the cellevator
   */
  public boolean isMiddleGapClear() {
    return m_ballDetectMiddle.get();
  }

  /**
   * Gets the beam break value to see if there is a power cell present at the
   * bottom of the cellevator
   */
  public boolean isBottomBallPresent() {
    return !m_ballDetectBottom.get();
  }

  /**
   * Gets the sensor value to detect if a ball is in the hopper to be loaded
   */
  public boolean isBallInFeeder() {
    return !m_ballDetectFeed.get();
  }

  /**
   * returns the value of the beam break middle value before the current value
   * 
   * @return
   */
  public boolean getMiddlePrevious() {
    return m_previousBBMiddle;
  }

  /**
   * returns the value of the beam break bottom value before the current value
   * 
   * @return
   */
  public boolean getBottomPrevious() {
    return m_previousBBBottom;
  }

  /**
   * returns the value of the beam break top value before the current value
   * 
   * @return
   */
  public boolean getTopPrevious() {
    return m_previousBBTop;
  }

  /**
   * sets the boolean value of the previous middle beam break
   */
  public void setBeamBreakMiddlePrevious(boolean value) {
    m_previousBBMiddle = value;
  }

  /**
   * sets the boolean value of the previous bottom beam break
   */
  public void setBeamBreakBottomPrevious(boolean value) {
    m_previousBBBottom = value;
  }

  /**
   * sets the boolean value of the previous top beam break
   */
  public void setBeamBreakTopPrevious(boolean value) {
    m_previousBBTop = value;
  }

  /**
   * adds to the count
   */
  public void addPowerCellCount() {
    m_powerCellCount++;
  }

  /**
   * subtracts from the count
   */
  public void subtractPowerCellCount() {
    m_powerCellCount--;
  }

  /**
   * returns the amount of power cells in the cellevator
   */
  public int getPowerCellCount() {
    return m_powerCellCount;
  }

  /**
   * creates a boolean with a value that represents which condition is happening
   * if either of the two conditions are true, then the holder motor will run
   */
  public boolean safeToCellelevate() {
    boolean topClearAndMiddleOccupied = !isTopBallPresent() && !isMiddleGapClear();
    boolean onlyBottomOccupied = isBottomBallPresent() && !isTopBallPresent() && isMiddleGapClear()
        && getMiddlePrevious();

    return topClearAndMiddleOccupied || onlyBottomOccupied;
  }

  public boolean readyToLoad() {
    return isBallInFeeder() && !isBottomBallPresent();
  }

  /**
   * Creates the entries for our shuffleboard tab
   */
  private void shuffleboardInit() {
    m_cellevatorConditions.addBoolean("Ball Held", () -> isTopBallPresent());
    m_cellevatorConditions.addBoolean("Ball Loaded", () -> isBottomBallPresent());
    m_cellevatorConditions.addBoolean("Gap Clear", () -> isMiddleGapClear());
    m_cellevatorConditions.addNumber("Cell Count", () -> getPowerCellCount());
    m_cellevatorConditions.addBoolean("Ball in Feeder", () -> isBallInFeeder());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    /*
     * If the previous bottom beam break is not equal to the current then check if
     * the current value is true
     * 
     * This means that there is a power cell that has been taken in so we can add
     * one to the count
     */
    if (m_previousBBBottom != isBottomBallPresent()) {
      if (isBottomBallPresent()) {
        addPowerCellCount();
      }
      // sets the previous value equal to the present
      setBeamBreakBottomPrevious(isBottomBallPresent());
    }

    if (m_previousBBMiddle != isMiddleGapClear()) {
      // sets the previous value equal to the present
      setBeamBreakMiddlePrevious(isMiddleGapClear());
    }

    if (m_previousBBTop != isTopBallPresent()) {
      // if the previous top beam break is not equal to the current then checki if the
      // current value is false
      // this means that the power cell left the cellevator and into the launcher so
      // we can subtract one from the count
      if (!isTopBallPresent()) {
        subtractPowerCellCount();
      }
      // sets the previous value equal to the present
      setBeamBreakTopPrevious(isTopBallPresent());
    }
  }

}
