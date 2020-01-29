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
        public static final int kDriveMotor1 = 0;
        public static final int kDriveMotor2 = 0;
        public static final int kDriveMotor3 = 0;
        public static final int kDriveMotor4 = 0;
        
        public static final int kGyroSensor = 0; //IMU for point turn

        public static final double kWheelDiameter = 0; //ADD UNIT OF MEASUREMENT

    }

    public static final class LauncherConstants {
        public static final int kLauncherMotor1 = 0;
        public static final int kLauncherMotor2 = 0;
        // pistons adjust the angle of the launcher
        public static final int kLauncherPiston1 = 0; // double solenoid
        public static final int kLauncherPiston2 = 0; // double solenoid

    }

    public static final class IntakeConstants {
        
        // beam breaker detects the number of powercells in the cellevator
        public static final int kBeamBreak1 = 0;
        public static final int kBeamBreak2 = 0;

        public static final int kIntakeMotor1 = 0;
        public static final int kIntakeMotor2 = 0;
        public static final int kIntakeMotor3 = 0;
        public static final int kIntakeMotor4 = 0;
        public static final int kIntakeMotor5 = 0;

        // pistons that lower and raise the intake
        public static final int kIntakePiston1 = 0; // double solenoid
        public static final int kIntakePiston2 = 0; // double solenoid

    }

    public static final class ControlPanelConstants {
        public static final int kSpinnerMotor = 0; 
        public static final int kColorSensor = 0;

    }

    public static final class HangerConstants {
        public static final int kHangerMotor1 = 0;
        public static final int kHangerMotor2 = 0;

    }

    public static final class LimeLightConstants {
        public static final int kLimelight = 0;

    }

}
