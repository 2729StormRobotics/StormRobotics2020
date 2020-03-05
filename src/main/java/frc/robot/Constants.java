/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.ColorMatch;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.util.Color;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final class DriveConstants {
        /**
         * CAN Bus IDs, set in their respective tools (CTRE, Rev, etc). These should be
         * equal to their corresponding PDP port. Check with Control Systems to confirm
         * if necessary. REV doesn't allow a CAN ID of 0, so we use 36 instead because
         * it's a good number.
         */
        public static final int kLeftMotor1Port = 2;
        public static final int kLeftMotor2Port = 3;
        public static final int kRightMotor1Port = 1;
        public static final int kRightMotor2Port = 36;

        /**
         * Used to define which side is reversed by default. In standard drivetrains,
         * it's the right side. These will also be used to reverse the encoders, which
         * is why we're not using the standard reverse from the DifferentialDrivetrain
         * class.
         * 
         * Because we're using flipped gearboxes for our drivetrain, it's the left side
         * that gets reversed.
         */
        public static final boolean kLeftReversedDefault = true;
        public static final boolean kRightReversedDefault = !kLeftReversedDefault;

        /**
         * PCM Solenoid port for shifting gears
         */
        public static final int kGearShiftChannel = 7;

        /**
         * Our drive encoder calculations. Since the encoder is built in to the motor,
         * we need to account for gearing.
         */
        private static final double kWheelDiameterInches = 6.0;
        private static final double kWheelDiameterFeet = kWheelDiameterInches / 12.0;
        private static final double kWheelDiameterMeters = kWheelDiameterInches * 2.54 / 100.0;
        private static final double kInitialGear = 14.0 / 58.0 * 18.0 / 38.0;
        private static final double kHighGear = kInitialGear * 32.0 / 34.0;
        private static final double kLowGear = kInitialGear * 22.0 / 44.0;

        // All measurements are based on inches and seconds
        public static final double kHighDistancePerPulse = kWheelDiameterInches * Math.PI * kHighGear;
        public static final double kHighSpeedPerPulse = kHighDistancePerPulse / 60.0;
        public static final double kLowDistancePerPulse = kWheelDiameterInches * Math.PI * kLowGear;
        public static final double kLowSpeedPerPulse = kLowDistancePerPulse / 60.0;

        // Speed used to define when to automatically shift gears
        public static final double kShiftSpeed = 80;

        // Our current limit for the drivetrain, measured in Amps
        public static final int kCurrentLimit = 60;

        // The necessary values for Feedforward
        public static final double kLeftS = 0.174;
        public static final double kLeftV = 6.31;
        public static final double kLeftA = 0.722;

        public static final double kRightS = 0.224;
        public static final double kRightV = 6.27;
        public static final double kRightA = 0.567;

        // Shuffleboard Tab
        public static final String kShuffleboardTab = "Drivetrain";

        /**
         * Our PID values for Drive Distance. Must be determined experimentally.
         */
        public static final class DriveDistancePID {
            // The PID profile slot on the SPARKMAX
            public static final int kProfile = 0;

            // The necessary PID values for driving straight with PID
            public static final double kLeftP = 0.0576;
            public static final double kLeftI = 0.0;
            public static final double kLeftD = 0.0;

            public static final double kRightP = 0.0576;
            public static final double kRightI = 0.0;
            public static final double kRightD = 0.0;

            public static final double kMaxOutput = 1.0;
            public static final double kMinOutput = -1.0;

            // All based on inches and seconds
            public static final double kPositionTolerance = 1.0;
            public static final double kVelocityTolerance = 5.0;
            public static final double kMaxVelocity = 15.0;
            public static final double kMaxAcceleration = 5.0;
        }

        /**
         * Our PID values for Point Turn. Must be determined experimentally.
         */
        public static final class PointTurnPID {
            public static final double kP = 0.13;
            public static final double kI = 0.0;
            public static final double kD = 0.0;
            public static final double kAngleTolerance = 1.0; // The tolerance in degrees
            public static final double kTurnSpeedTolerance = 1.0; // Degrees per second
        }
    }

    public static final class LauncherConstants {
        /**
         * CAN Bus IDs, set in their respective tools (CTRE, Rev, etc). These should be
         * equal to their corresponding PDP port. Check with Control Systems to confirm
         * if necessary.
         */
        public static final int kRightMotorPort = 8;
        public static final int kLeftMotorPort = 9;

        public static final int kShortAngleChannel = 3;
        public static final int kLongAngleChannel = 4;

        public static final boolean kInvertLeftLauncher = true;
        public static final boolean kInvertRightLauncher = !kInvertLeftLauncher;

        public static final double kVelocityConversion = 1.0 / 60.0;

        public static final double kWallShotSpeed = 52;
        public static final double kTrenchShotSpeed = 84;
        public static final double kMaxShotSpeed = 85;

        public static final Value kShortLaunchValue = Value.kReverse;
        public static final Value kLongLaunchValue = Value.kForward;

        // Used to calculate the feedforward
        public static final double kS = 0.171;
        public static final double kV = 0.129;
        public static final double kA = 0.018;

        // Shuffleboard Tab
        public static final String kShuffleboardTab = "Launcher";

        /**
         * Our PID values for Maintaining Speed. Must be determined experimentally.
         */
        public static final class LauncherPID {
            public static final double kP = 0.0133;
            public static final double kI = 0.0;
            public static final double kD = 0.0;
            public static final double kIz = 0;
            public static final double kF = 0;
            public static final double kMinOutput = -1;
            public static final double kMaxOutput = 1;

            // The tolerance for our PID control - measured in RPS
            public static final double kVelocityTolerance = 0.5;
        }
    }

    public static final class IntakeConstants {
        public static final int kIntakeMotorPort = 4;

        // piston (double solenoid) that raises and lowers the intake
        public static final int kIntakeRaiseChannel = 5;
        public static final int kIntakeLowerChannel = 2;

        public static final Value kIntakeLowerValue = Value.kReverse;
        public static final Value kIntakeRaiseValue = Value.kForward;

        public static final double kIntakeMotorSpeed = 0.65;
        public static final double kEjectMotorSpeed = -1;

        // Shuffleboard Tab
        public static final String kShuffleboardTab = "Power Cells";
    }

    public static final class HopperConstants {
        public static final int kHopperMotorPort = 5;
        public static final double kHopperMotorSpeed = 0.25; // TODO: Change the speed after testing.

        // Shuffleboard Tab
        public static final String kShuffleboardTab = "Power Cells";
    }

    public static final class CellevatorConstants {
        // beam breakers detect the number of powercells in the cellevator
        public static final int kBeamBreakLoaderPort = 2;
        public static final int kBeamBreakMiddlePort = 4;
        public static final int kBeamBreakHolderPort = 6;

        public static final int kBallFeedPort = 0;

        // motors in cellevator that transport powercells to launcher
        public static final int kCellevatorMotorPort = 13;
        public static final int kLoaderMotorPort = 14;

        // Define if a motor is inverted or not
        public static final boolean kHolderMotorInverted = false;
        public static final boolean kLoaderMotorInverted = true;

        // Define the current limit for the celevator motors
        public static final int kCellevatorCurrentLimit = 45;

        // the constant speed for the cellevator motor
        public static final double kCellevatorMotorSpeed = 0.5; // TODO: test and update value

        // speed for the loader Motor
        public static final double kLoaderMotorSpeed = 0.5; // TODO: test and update value

        // Shuffleboard Tab
        public static final String kShuffleboardTab = "Life Cycle";
    }

    public static final class ControlPanelConstants {
        // Motor for the wheel that spins the control panal
        public static final int kSpinnerMotorPort = 6;

        // The main speed of the control panel motor.
        public static final double kCountRevSpeed = 0.4; // TODO Update with testing
        // The speed to run when seeking color
        public static final double kFindColorSpeed = 0.18; // TODO Update with testing

        /** Values the color sensor uses to detect the colors on control panel */
        // Red
        private static final double kRedTargetR = 0.476;
        private static final double kRedTargetG = 0.376;
        private static final double kRedTargetB = 0.15;
        // Yellow
        private static final double kYellowTargetR = 0.381;
        private static final double kYellowTargetG = 0.545;
        private static final double kYellowTargetB = 0.136;
        // Green
        private static final double kGreenTargetR = 0.18;
        private static final double kGreenTargetG = 0.568;
        private static final double kGreenTargetB = 0.249;
        // Blue
        private static final double kBlueTargetR = 0.15;
        private static final double kBlueTargetG = 0.4467;
        private static final double kBlueTargetB = 0.40;
        // Zero
        private static final double kNoColorR = 0;
        private static final double kNoColorG = 0;
        private static final double kNoColorB = 0;
        // Unknown
        private static final double kUnknownColorR = 1;
        private static final double kUnknownColorB = 1;
        private static final double kUnknownColorG = 1;
        // Colors
        public static final Color kRedTarget = ColorMatch.makeColor(kRedTargetR, kRedTargetG, kRedTargetB);
        public static final Color kYellowTarget = ColorMatch.makeColor(kYellowTargetR, kYellowTargetG, kYellowTargetB);
        public static final Color kGreenTarget = ColorMatch.makeColor(kBlueTargetR, kBlueTargetG, kBlueTargetB);
        public static final Color kBlueTarget = ColorMatch.makeColor(kGreenTargetR, kGreenTargetG, kGreenTargetB);
        public static final Color kNoColor = ColorMatch.makeColor(kNoColorR, kNoColorG, kNoColorB);
        public static final Color kUnknownColor = ColorMatch.makeColor(kUnknownColorR, kUnknownColorG, kUnknownColorB);

        // The confidence threshold for a given color
        public static final double kConfidence = 0.85; // TODO determine ideal confidence from testing

        public static final double kCPR = 4096;
        public static final double kSpinnerWheelDiameter = 4;
        public static final double kControlPanelDiameter = 32;
        public static final double kRevConversion = 1 / kCPR * kControlPanelDiameter / kSpinnerWheelDiameter;

        // Shuffleboard Tab
        public static final String kShuffleboardTab = "Control Panel";
    }

    public static final class ClimberConstants {
        // Gearing for calculations, given in output turns per motor turn
        private static final double kGearing = 10.0 / 58.0 * 16.0 / 60.0;
        private static final double kPulleyDiameter = 1.662; // Inches

        // Conversion for distance and speed per encoder pulse, given in inches
        public static final double kDistancePerPulse = Math.PI * kPulleyDiameter * kGearing;
        public static final double kSpeedPerPulse = kDistancePerPulse / 60.0;

        public static final int kClimberMotorPort = 15;

        // piston that applies the friction brake on the climbing motors
        public static final int kFrictionBrakeChannel = 6;

        // Set the constants to invert the left and right climber motors
        public static final boolean kMotorInverted = true;

        // Define the speeds for up and down climbing
        public static final double kClimbUpSpeed = 0.5;
        public static final double kClimbDownSpeed = -0.5;
        public static final double kHoldVoltage = 4.41;

        // Define the maximum height of the climber
        public static final double kMaxHeight = 21.5;

        public static final boolean kFrictionBrakeEnabled = false;
        public static final boolean kFrictionBrakeDisabled = true;

        // Shuffleboard Tab
        public static final String kShuffleboardTab = "Climber";
    }

    public static final class VisionConstants {
        public static final double kLimelightHeight = 0.975;
        public static final double kLimelightAngle = 16;
        public static final double kPortHeight = 2.37;

        public static final int kDefaultPipeline = 0;

        // Alignment constants for LimeLight
        public static final double kAutoAlignP = 0.05;
        public static final double kAutoAlignI = 0.0;
        public static final double kAutoAlignD = 0.01;
        public static final double kAutoAlignTolerance = 1.0;
        public static final double kAutoAlignSpeedTolerance = 0.5;

        public static final double kDistanceTolerance = 1.0;

        // Shuffleboard Tab
        public static final String kShuffleboardTab = "Vision";
    }

    public static final class PartyConstants {
        // DIO port number constants for the two LED Blinkin Drivers
        public static final int kLedBlinkinDriverPort = 9;

        // Each possible LED mode correlates to a value for the Spark, as defined in the Rev Blinkin docs.
        // The LED modes correspond to a particular activity on the robot, used as LED color feedback.
        public static final double kDisabled = 0.43;            // Beats per minute: Red and Orange
        public static final double kHighGear = 0.93;            // Solid White
        public static final double kLowGear = 0.67;             // Solid Gold
        public static final double k1PC = 0.17;                 // Solid: Orange
        public static final double k2PC = 0.27;                 // Heartbeat Fast: Orange
        public static final double k3PC = 0.35;                 // Strobe: Orange
        public static final double kAligned = 0.75;             // Solid: Dark Green
        public static final double kRevToSpeed = 0.57;          // Solid: Hot Pink 
        public static final double kRevvedAndAligned = -0.03;   // Solid: Red
        public static final double kLaunchAngleToggle = 0.79;   // Solid: Blue Green
        public static final double kControlPanel = 0.71;        // Solid Lawn Green
        public static final double kDoneClimb = -0.99;          // Rainbow: Rainbow
    }

    public static final class OIConstants {
        /**
         * Ports assigned in Driver Station to each controller and the deadzone for each
         * one.
         */
        public static final int kDriverControllerPort = 0;
        public static final int kWeaponsControllerPort = 1;

        public static final double kDriveDeadzone = 0.02;
        public static final String kAutoCommandsTab = "Autonomous";
    }
}
