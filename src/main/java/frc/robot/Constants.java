/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.util.Color;

/*   The Consta s class
 * provides a convenient p ce for tea  s to hol robot-wide
 * merical or constants.   r any other purpose.  All constants sho d be
 *
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 *
 *
 *   <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

        public static final class DriveConstants {
            public static final int kLeftDriveMotor1Port = 0;
            public static final int kLeftDriveMotor2Port = 15;
            public static final int kRightDriveMotor1Port = 3;
            public static final int kRightDriveMotor2Port = 12;

            // pistons that shift the gear of the drive train
            public static final int kDriveSolenoid = 7;

            public static final double kWheelDiameter = 6; // Inches

            // constant for the distance that mechanism travels for every pulse of the encoder
            public static final double kEncoderDistancePerPulse = 0; // ADD CONVERSION
            // constant for the speed of the motor per pulse of the encoder
            public static final double kEncoderSpeedPerPulse = 0; // ADD CONVERSION

            public static final boolean kLeftEncoderInverted = false;
            public static final boolean kRightEncoderInverted = false;

            // Current limit for the drivetrain motors in amps
            public static final int kDrivetrainCurrentLimit = 60;

        }

        public static final class LauncherConstants {
            public static final int kRightLauncherMotorPort = 13;
            public static final int kLeftLauncherMotorPort = 14;

            // piston (double solenoid) that adjusts the angle of the launcher
            public static final int kLongLaunchSolenoidPort = 3;
            public static final int kShortLaunchSolenoidPort = 4;
        }

        public static final class IntakeConstants {

            public static final int kIntakeMotorPort = 7;

            // piston (double solenoid) that raises and lowers the intake
            public static final int kIntakeRaiseSolenoidPort = 5;
            public static final int kIntakeLowerSolenoidPort = 2;

            public static final double kIntakeMotorSpeed = 1.0; //TODO: Test and update this value.

        }

        public static final class HopperConstants {
            public static final int kHopperMotorPort = 6;
            public static final double kHopperMotorSpeed = 1.0; //TODO: Change the speed after testing.
        }

        public static final class CelevatorConstants {

            // beam breakers detect the number of powercells in the cellevator
            public static final int kBeamBreakLoaderPort = 0;
            public static final int kBeamBreakHolderPort = 0;

            // motors in cellevator that transport powercells to launcher
            public static final int kHolderMotorPort = 5;
            public static final int kLoaderMotorPort = 8;

            // Define if a motor is inverted or not
            public static final boolean kHolderMotorInverted = false;
            public static final boolean kLoaderMotorInverted = false;

            // Define the current limit for the celevator motors
            public static final int kCelevatorCurrentLimit = 45;
        }

        public static final class ControlPanelConstants {

            public static final int kSpinnerMotorPort = 9; // motor for the wheel that spins the control panal

            public static final int kColorSensorPort = 0; // Color sensor that senses the colors on the control panel

            /*values the color sensor values to detect the colors on control panel
            red*/
            public static final double kRedTargetR = 0.476;
            public static final double kRedTargetG = 0.376;
            public static final double kRedTargetB = 0.15;
            // Yellow
            public static final double kYellowTargetR = 0.381;
            public static final double kYellowTargetG = 0.545;
            public static final double kYellowTargetB = 0.136;
            // Green
            public static final double kGreenTargetR = 0.18;
            public static final double kGreenTargetG = 0.568;
            public static final double kGreenTargetB = 0.249;
            // Blue
            public static final double kBlueTargetR = 0.15;
            public static final double kBlueTargetG = 0.4467;
            public static final double kBlueTargetB = 0.40;
            // Colors
            public static final Color kRedTarget = new Color(kRedTargetR, kRedTargetG, kRedTargetB);
            public static final Color kYellowTarget = new Color(kYellowTargetR, kYellowTargetG, kYellowTargetB);
            public static final Color kGreenTarget = new Color(kBlueTargetR, kBlueTargetG, kBlueTargetB);
            public static final Color kBlueTarget = new Color(kGreenTargetR, kGreenTargetG, kGreenTargetB);

        }

        public static final class ClimberConstants {

            public static final double kEncoderDistancePerPulse = 0; // ADD CONVERSION
            public static final double kEncoderSpeedPerPulse = 0; // ADD CONVERSION

            public static final int kRightClimberMotorPort = 2;
            public static final int kLeftClimberMotorPort = 1;

            public static final int kFrictionSolenoidPort = 6; // piston that applies the friction brake on the hanging elevator
        }

        public static final class LimelightConstants {

            public static final double kLimelightOffset = 13.67; // In inches
            public static final double kLimelightHeight = 43; // In inches
            public static final double kLimelightAngle = 48; // In degrees
            public static final double kPortHeight = 98.25; // In inches

            // Alignment constants for LimeLight
            public static final double kLimelightAlignP = 1.0;
            public static final double kLimelightAlignI = 0.0;
            public static final double kLimelightAlignD = 0.0;
            public static final double kLimelightTolernce = 5.0;
            public static final double kLimelightAlignTolerance = 1.5;

            // Distance constants for LimeLight
            public static final double kLimelightDistanceP = 1.0;
            public static final double kLimelightDistanceI = 0.0;
            public static final double kLimelightDistanceD = 0.0;
            public static final double kLimelightDistance = 50.0;
            public static final double kLimelightDistanceTolerance = 5.0;
        }

    }
