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
         */
        public static final boolean kLeftMotorsReversed = true;
        public static final boolean kRightMotorsReversed = !kLeftMotorsReversed;

        /**
         * PCM Solenoid port for shifting gears
         */
        public static final int kGearShiftPort = 7;

        /**
         * Our drive encoder calculations. Since the encoder is built in to the motor,
         * we need to account for gearing.
         */
        private static final double kWheelDiameterInches = 6.0;
        private static final double kWheelDiameterFeet = kWheelDiameterInches / 12.0;
        private static final double kWheelDiameterMeters = kWheelDiameterInches * 2.54 / 100.0;
        private static final double kInitialDriveGearing = 14.0 / 58.0 * 18.0 / 38.0;
        private static final double kHighDriveGearing = kInitialDriveGearing * 32.0 / 34.0;
        private static final double kLowDriveGearing = kInitialDriveGearing * 22.0 / 44.0;

        // All measurements are based on inches and seconds
        public static final double kHighDriveDistancePerPulse = kWheelDiameterInches * Math.PI * kHighDriveGearing;
        public static final double kHighDriveSpeedPerPulse = kHighDriveDistancePerPulse / 60.0;
        public static final double kLowDriveDistancePerPulse = kWheelDiameterInches * Math.PI * kLowDriveGearing;
        public static final double kLowDriveSpeedPerPulse = kLowDriveDistancePerPulse / 60.0;

        // Speed used to define when to shift gears
        public static final double kShiftSpeed = 80; // inches per second

        // Our current limit for the drivetrain
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
         * Our PID values for Drive Straight. Must be determined experimentally.
         */
        public static final class DriveDistance {
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

            public static final double kPositionTolerance = 1.0;
            public static final double kVelocityTolerance = 5.0;
            public static final double kMaxVelocity = 15.0;
            public static final double kMaxAcceleration = 5.0;
        }

        /**
         * Our PID values for Point Turn. Must be determined experimentally.
         */
        public static final class PointTurnPID {
            // The PID profile slot on the SPARKMAX
            public static final int kProfile = 1;

            public static final double kP = 1.0;
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
        public static final int kLauncherMotorRightPort = 8;
        public static final int kLauncherMotorLeftPort = 9;

        public static final int kLauncherShortAnglePort = 4;
        public static final int kLauncherLongAnglePort = 3;

        public static final boolean kInvertLeftLauncher = true;
        public static final boolean kInvertRightLauncher = !kInvertLeftLauncher;

        public static final Value kShortLaunchSolenoidSetting = Value.kForward;
        public static final Value kLongLaunchSolenoidSetting = Value.kReverse;

        /**
         * Our launcher encoder calculations.
         */
        public static final double kLaunchDistanceConversion = 0.0;

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

            // The tolerance for our PID control - measured in RPSs
            public static final double kVelocityTolerance = 0.5;
        }
    }

    public static final class IntakeConstants {
        public static final int kIntakeMotorPort = 4;

        // piston (double solenoid) that raises and lowers the intake
        public static final int kIntakeRaiseSolenoidPort = 5;
        public static final int kIntakeLowerSolenoidPort = 2;

        public static final Value kIntakeLowerSolenoidSetting = Value.kReverse;
        public static final Value kIntakeRaiseSolenoidSetting = Value.kForward;

        public static final double kIntakeMotorSpeed = 0.75;

        // Shuffleboard Tab
        public static final String kShuffleboardTab = "Life Cycle";
    }

    public static final class HopperConstants {
        public static final int kHopperMotorPort = 5;
        public static final double kHopperMotorSpeed = 0.25; // TODO: Change the speed after testing.

        // Shuffleboard Tab
        public static final String kShuffleboardTab = "Life Cycle";
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
        public static final Color kNoColor = new Color(kNoColorR, kNoColorB, kNoColorG);
        public static final Color kUnknownColor = new Color(kUnknownColorR, kUnknownColorB, kUnknownColorG);

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
        public static final double kEncoderDistancePerPulse = Math.PI * kPulleyDiameter * kGearing;
        public static final double kEncoderSpeedPerPulse = kEncoderDistancePerPulse / 60;

        public static final int kRightClimberMotorPort = 15;
        public static final int kLeftClimberMotorPort = 12;

        // piston that applies the friction brake on the climbing motors
        public static final int kFrictionSolenoidPort = 6;

        // Set the constants to invert the left and right climber motors
        public static final boolean kRightClimberMotorInverted = true;
        public static final boolean kLeftClimberMotorInverted = !kRightClimberMotorInverted;

        // Define the speeds for up and down climbing
        public static final double kClimbUpSpeed = 0.5; // TODO Determine best speed
        public static final double kClimbDownSpeed = -0.5; // TODO Determine best speed

        // Define the maximum height of the climber
        public static final double kMaxHeight = 0.0; // TODO Determine max height

        public static final boolean kFrictionBrakeEnabled = true;
        public static final boolean kFrictionBrakeDisabled = false;

        // Shuffleboard Tab
        public static final String kShuffleboardTab = "Climber";
    }

    public static final class VisionConstants {
        public static final double kLimelightOffset = 0;
        public static final double kLimelightHeight = 43;
        public static final double kLimelightAngle = 16;
        public static final double kPortHeight = 98.25;

        // Alignment constants for LimeLight
        public static final double kAutoAlignP = 1.0;
        public static final double kAutoAlignI = 0.0;
        public static final double kAutoAlignD = 0.0;
        public static final double kAutoAlignTolerance = 1.0;
        public static final double kAutoAlignSpeedTolerance = 0.5;

        public static final double kDistanceTolerance = 1.0;

        // Shuffleboard Tab
        public static final String kShuffleboardTab = "Vision";
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
