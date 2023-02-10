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
    public static final int kTelescopicMotorCANID = 6;
    public static final int kWristMotor = 7;
    
 
 
 
  }
}
