// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    // Controller IDs
    public static final int kJoystickDriverID = 0;
    public static final int kJoystickManipulatorID = 1;

    // Drive CANIDs
    public static final int kRightFrontDriveCANID = 1;
    public static final int kLeftFrontDriveCANID = 2;
    public static final int kLeftBackDriveCANID = 3;
    public static final int kRightBackDriveCANID = 4;

    // Nav-x IDs
    public static final double kOffBalanceAngleThresholdDegrees = 10;
    public static final double kOnBalanceAngleThresholdDegrees = 5;

    // Drive Autonomous
    public static final double kAutoVolt = -.5;

    // Manipulator Motor IDs
    public static final int kShoulderMotorCANID = 5;
    public static final int kForearmMotorCANID = 6;
    public static final int kWristMotorCANID = 7;

    // Wrist Encoder IDs
    public static final int kWristMotorDIOID1 = 4;
    public static final int kWristMotorDIOID2 = 5;

    // Pistion Channels
    public static final int kPistonForwardWristChannel = 0;
    public static final int kPistonReverseWristChannel = 1;

    // manipulator controller buttons
    // Shoulder Button
    public static final int kShoulderAxisNumber = 2;

    // Forearm Buttons
    public static final int kForearmExtendButtonNumber = 11;
    public static final int kForearmRetractButtonNumber = 12;

    // Wrist Buttons
    public static final int kWristRotationAxisNumber = 5;
    public static final int kWristPistonButtonNumber = 1;

    // Limit Switches
    public static final int kUpperShoulderLimitSwitchChannel = 0;
    public static final int kLowerShoulderLimitSwitchChannel = 1;
    public static final int kLowerForearmLimitSwitchChannel = 2;
    public static final int kLowerWristLimitSwitchChannel = 3;

    // PID here
    public static final double MANIPULATOR_MIN_OUTPUT = -6000.0;
    public static final double MANIPULATOR_MAx_OUTPUT = 6000.0;

    // Upper Limit For Forearm + Wrist
    public static final double encoderForearmDistance = 145;
    public static final double encoderWristDistance = 100;

    // Driver buttons
    public static final int kAimCentering = 1;

    // autonoous distances in inches
    public static final int kAutoDistanceOne = 12;// placeholder values
    public static final int kAutoDistanceTwo = 14;

    public static final double wheelCircumference = 2 * (Math.PI * 3);;
    public static final double desiredDistance = 56;
    public static final double turn90Degrees = 18.81 / 4;// one full wheel rotation is 18.81 inches
    public static final double shortDistance = 12;
    public static final double turn180Degrees = 18.81 / 2;
    public static final double longDistance = 96;
  }
}
