// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    //Controller IDs 
    public static final int kJoystickDriverID = 0;
    public static final int kJoystickManipulatorID = 1;
   
    //drive CANIDs 
    public static final int kRightFrontDriveCANID = 1;
    public static final int kLeftFrontDriveCANID = 2;
    public static final int kLeftBackDriveCANID = 3;
    public static final int kRightBackDriveCANID = 4;

    //Nav-x IDs
    public static final double kOffBalanceAngleThresholdDegrees = 10;
    public static final double kOnBalanceAngleThresholdDegrees  = 5;

    
    //drive Autonomous
    public static final double kAutoVolt = -.5;
   
    //manipulator Motor IDs 
    public static final int kArmMotorCANID = 5;
    public static final int kForearmMotorCANID = 6;
    public static final int kWristMotorCANID = 7;
    
    public static final int kArmInputAxis = 2;
    public static final int kManipulatorInputExtend = 11;
    public static final int kManipulatorInputRetract = 12;
    public static final int kWristInputUp = 5;
    public static final int kWristInputDown = 6;

    //Limit Switches
    public static final int kShoulderLowerLimitID = 0;
    public static final int kShoulderUpperLimitID = 1;
    public static final int kForearmLimitID = 2;
    public static final int kWristLimitID = 3;   
    public static final int kWristEncoderAID = 4;
    public static final int kWristEncoderBID = 5;

    //Solenoids
    public static final int kClawExtend = 0;
    public static final int kClawRetract = 1;

    public static final double kForearmCircum = 0.75*Math.PI;
    public static final double kForearmIncrement = 2;
  }
}
