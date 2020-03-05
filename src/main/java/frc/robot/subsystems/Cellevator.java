/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import static frc.robot.Constants.CellevatorConstants.*;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Cellevator extends SubsystemBase {
  private final WPI_TalonSRX m_loaderMotor;
  private final WPI_TalonSRX m_cellevatorMotor;

  private final DigitalInput m_ballDetectTop;
  private final DigitalInput m_ballDetectBottom;
  private final DigitalInput m_ballDetectMiddle;
  private final DigitalInput m_ballDetectFeed;

  private final ShuffleboardTab m_testingTab;
  private final ShuffleboardLayout m_cellevatorStatus;

  private int m_powerCellCount;
  private int m_powerCellsIn;
  private int m_powerCellsOut;
  private boolean m_previousBottomBall;
  private boolean m_previousMiddleBall;
  private boolean m_previousTopBall;

  private final NetworkTable m_partyTable;
  private final NetworkTableEntry m_cellCountPartyStatus;

  private final Timer m_CellCountTimer = new Timer();

  /**
   * Creates a new Cellevator.
   */
  public Cellevator() {
    m_cellevatorMotor = new WPI_TalonSRX(kCellevatorMotorPort);
    m_loaderMotor = new WPI_TalonSRX(kLoaderMotorPort);

    motorInit(m_cellevatorMotor, kHolderMotorInverted);
    motorInit(m_loaderMotor, kLoaderMotorInverted);

    m_ballDetectBottom = new DigitalInput(kBeamBreakLoaderPort);
    m_ballDetectTop = new DigitalInput(kBeamBreakHolderPort);
    m_ballDetectMiddle = new DigitalInput(kBeamBreakMiddlePort);
    m_ballDetectFeed = new DigitalInput(kBallFeedPort);

    m_powerCellCount = 0;
    m_powerCellsIn = 0;
    m_powerCellsOut = 0;
    m_previousBottomBall = false;
    m_previousMiddleBall = false;
    m_previousTopBall = false;

    m_testingTab = Shuffleboard.getTab("Testing");
    m_cellevatorStatus = m_testingTab.getLayout("Power Cells", BuiltInLayouts.kList)
        .withProperties(Map.of("Label position", "TOP"));

    m_partyTable = NetworkTableInstance.getDefault().getTable("Party Statuses");
    m_cellCountPartyStatus = m_partyTable.getEntry("Cell Count");

    m_CellCountTimer.start();

    shuffleboardInit();
  }

  private void motorInit(WPI_TalonSRX motor, boolean invert) {
    motor.configFactoryDefault(); // Reset settings in motor in case they are changed
    motor.setNeutralMode(NeutralMode.Brake); // Sets the motors to brake mode from the beginning
    motor.setInverted(invert); // Inverts the motor if needed
    motor.configContinuousCurrentLimit(kCellevatorCurrentLimit); // Set the continuous current limit
    motor.configPeakCurrentLimit(0); // Set peak to zero so that the continuous current limit is always obeyed
  }

  /**
   * Runs the Cellevator motors
   * 
   * @param speed The speed to run the Cellevator motors
   */
  private void runCellevator(double speed) {
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
  private void runLoader(double speed) {
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
  public void eject() {
    runLoader(-0.8);
    runCellevator(-0.8);
  }

  /**
   * Gets the beam break value to see if there is a power cell present at the top
   * of the cellevator
   */
  public boolean isTopBallPresent() {
    return !m_ballDetectTop.get();
  }

  /**
   * Returns whether a ball was previously present at the top of the cellevator
   */
  public boolean wasTopBallPresent() {
    return m_previousTopBall;
  }

  /**
   * Gets the beam break value to see if there is a power cell present at the
   * middle of the cellevator
   */
  public boolean isMiddleBallPresent() {
    return !m_ballDetectMiddle.get();
  }

  /**
   * Returns whether a ball was previously present at the middle of the cellevator
   */
  public boolean wasMiddleBallPresent() {
    return m_previousMiddleBall;
  }

  /**
   * Gets the beam break value to see if there is a power cell present at the
   * bottom of the cellevator
   */
  public boolean isBottomBallPresent() {
    return !m_ballDetectBottom.get();
  }

  /**
   * Returns whether a ball was previously present at the bottom of the cellevator
   */
  public boolean wasBottomBallPresent() {
    return m_previousBottomBall;
  }

  /**
   * Gets the sensor value to detect if a ball is in the hopper to be loaded
   */
  public boolean isBallInFeeder() {
    return !m_ballDetectFeed.get();
  }

  /**
   * adds to the count
   */
  private void addIntakePowerCellCount() {
    m_powerCellsIn++;
  }

  /**
   * subtracts from the count
   */
  private void addLaunchPowerCellCount() {
    m_powerCellsOut++;
  }

  /**
   * sets the number of power cells in the cellevator by detecting their presence
   * with the beam breaks
   */
  private void setPowerCellCount() {
    m_powerCellCount = (isBottomBallPresent() ? 1 : 0) + (isMiddleBallPresent() ? 1 : 0) + (isTopBallPresent() ? 1 : 0);
    m_cellCountPartyStatus.setNumber(m_powerCellCount);
  }

  /**
   * returns the amount of power cells in the cellevator
   */
  public int getCurrentCellCount() {
    return m_powerCellCount;
  }

  /**
   * Determines if it is safe to run the cellevator according to the following
   * criteria: 1) No ball in the top OR 2) Only a ball in the bottom (safe to move
   * to middle)
   */
  public boolean safeToCellelevateForIndex() {
    boolean topClearAndMiddleOccupied = !isTopBallPresent() && isMiddleBallPresent();
    boolean onlyBottomOccupied = isBottomBallPresent() && !isTopBallPresent() && !isMiddleBallPresent();

    return topClearAndMiddleOccupied || onlyBottomOccupied;
  }

  public boolean safeToCellevateForLaunch() {
    boolean topOccupied = isTopBallPresent();
    boolean middleOccupied = isMiddleBallPresent();
    boolean bottomOccupied = isBottomBallPresent();

    return topOccupied || middleOccupied || bottomOccupied;
  }

  public boolean safeToLoad() {
    return isBallInFeeder() && !isBottomBallPresent();
  }

  /**
   * Creates the entries for our shuffleboard tab
   */
  private void shuffleboardInit() {
    m_cellevatorStatus.addBoolean("Top Ball", () -> isTopBallPresent());
    m_cellevatorStatus.addBoolean("Bottom Ball", () -> isBottomBallPresent());
    m_cellevatorStatus.addBoolean("Middle Ball", () -> isMiddleBallPresent());
    m_cellevatorStatus.addNumber("Cellevator Count", () -> getCurrentCellCount());
    m_cellevatorStatus.addNumber("Cells In", () -> m_powerCellsIn);
    m_cellevatorStatus.addNumber("Cells Out", () -> m_powerCellsOut);
    m_cellevatorStatus.addBoolean("Ball in Feeder", () -> isBallInFeeder());
  }

  // This method will be called once per scheduler run
  @Override
  public void periodic() {
    // Sets the current count of power cells in the cellevator
    setPowerCellCount();

    /*
     * If the previous bottom beam break is not equal to the current then check if
     * the current value is true
     * 
     * This means that there is a power cell that has been taken in so we can add
     * one to the count
     */
    if (wasBottomBallPresent() != isBottomBallPresent()) {
      if (isBottomBallPresent()) {
        addIntakePowerCellCount();
      }
      // sets the previous value equal to the present
      m_previousBottomBall = isBottomBallPresent();
    }

    if (wasMiddleBallPresent() != isMiddleBallPresent()) {
      // sets the previous value equal to the present
      m_previousMiddleBall = isMiddleBallPresent();
    }

    if (wasTopBallPresent() != isTopBallPresent()) {
      // if the previous top beam break is not equal to the current then checki if the
      // current value is false
      // this means that the power cell left the cellevator and into the launcher so
      // we can subtract one from the count
      if (!isTopBallPresent()) {
        addLaunchPowerCellCount();
      }
      // sets the previous value equal to the present
      m_previousTopBall = isTopBallPresent();
    }
  }
}
