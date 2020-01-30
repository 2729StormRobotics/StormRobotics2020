/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    
        // ADD THE PORT NUMBERS TO CONSTANTS!!!!!

        public static final class DriveConstants {
            public static final int kLeftDriveMotor1Port = 0;
            public static final int kLeftDriveMotor2Port = 0;
            public static final int kRightDriveMotor1Port = 0;
            public static final int kRightDriveMotor2Port = 0;
    
            public static final double kWheelDiameter = 0; //ADD UNIT OF MEASUREMENT
    
        }
    
        public static final class LauncherConstants {
            public static final int kLauncherMotor1Port = 0;
            public static final int kLauncherMotor2Port = 0;
    
            // pistons adjust the angle of the launcher
            public static final int kLauncherSolenoidPort = 0; 
    
        }
    
        public static final class IntakeConstants {
            
            // beam breaker detects the number of powercells in the cellevator
            public static final int kBeamBreak1OutputPort = 0;
            public static final int kBeamBreak2OutputPort = 0;
    
            public static final int kIntakeMotorPort = 0;
            
            // pistons that lower and raise the intake
            public static final int kIntakeSolenoidPort = 0;
        }
    
        public static final class LoadingConstants {
            
            public static final int kLoadingMotor1Port = 0;
            public static final int kLoadingMotor2Port = 0;
            public static final int kLoadingMotor3Port = 0;
            public static final int kLoadingMotor4Port = 0;
        }
    
        public static final class ControlPanelConstants {
    
            public static final int kSpinnerMotorPort = 0; // motor for the wheel that spins the control panal
            public static final int kColorSensorPort = 0; // Color sensor that senses the colors on the control panel
    
        }
    
        public static final class HangerConstants {
    
            public static final int kHangerMotor1Port = 0;
            public static final int kHangerMotor2Port = 0;
    
        }
    
        public static final class LimeLightConstants {
    
    
        }
    
    }
