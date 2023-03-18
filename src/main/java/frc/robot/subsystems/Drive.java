// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
//drive train
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
//hardware
import edu.wpi.first.wpilibj.Joystick;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;

//data collection
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive extends SubsystemBase {
  // Motors
  private CANSparkMax m_rightFrontMotor;
  private CANSparkMax m_leftFrontMotor;
  private CANSparkMax m_rightBackMotor;
  private CANSparkMax m_leftBackMotor;

  // Encoders
  public static RelativeEncoder m_rightFrontEncoder;
  public static RelativeEncoder m_leftFrontEncoder;
  public static RelativeEncoder m_rightBackEncoder;
  public static RelativeEncoder m_leftBackEncoder;

  // Makes differential drive and motorcontroller groups
  public static DifferentialDrive tankarcadeDrive;
  public MotorControllerGroup Left;
  public MotorControllerGroup Right;

  /** Creates a new Drive. */
  public Drive(Joystick joystickDriver) {

    // Motors are now set to same as robotcontainer
    m_rightFrontMotor = RobotContainer.rightFrontMotor;
    m_leftFrontMotor = RobotContainer.leftFrontMotor;
    m_rightBackMotor = RobotContainer.rightBackMotor;
    m_leftBackMotor = RobotContainer.leftBackMotor;

    // Detects and sets encoder values
    m_rightFrontEncoder = m_rightFrontMotor.getEncoder();
    m_leftFrontEncoder = m_leftFrontMotor.getEncoder();
    m_rightBackEncoder = m_rightBackMotor.getEncoder();
    m_leftBackEncoder = m_leftBackMotor.getEncoder();

    m_leftBackEncoder.setPositionConversionFactor(2.57);
    m_leftFrontEncoder.setPositionConversionFactor(2.57);
    m_rightFrontEncoder.setPositionConversionFactor(2.57);
    m_rightBackEncoder.setPositionConversionFactor(2.57);

    m_rightFrontMotor.setOpenLoopRampRate(1);// ramping
    m_rightBackMotor.setOpenLoopRampRate(1);
    m_leftBackMotor.setOpenLoopRampRate(1);
    m_leftFrontMotor.setOpenLoopRampRate(1);
    // Zeroes encoders

    // Two motorcontroller groups that will act as left and right in tank drive
    Left = new MotorControllerGroup(m_leftFrontMotor, m_leftBackMotor);
    Right = new MotorControllerGroup(m_rightFrontMotor, m_rightBackMotor);
    tankarcadeDrive = new DifferentialDrive(Left, Right);

    tankarcadeDrive.setSafetyEnabled(true);// drive settings, required for safety reasons
    tankarcadeDrive.setExpiration(.1);
    tankarcadeDrive.setMaxOutput(1);

    // calculations
    // TPR = m_leftFrontEncoder.getCountsPerRevolution();// raw values

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Front Left Encoder", m_leftFrontEncoder.getPosition());
    SmartDashboard.putNumber("Back Left Encoder", m_leftBackEncoder.getPosition());
    SmartDashboard.putNumber("Front Right Encoder", m_rightFrontEncoder.getPosition());
    SmartDashboard.putNumber("Back Right Encoder", m_rightBackEncoder.getPosition());
  }

  public void zeroEncoders() {
    m_rightFrontEncoder.setPosition(0);
    m_leftFrontEncoder.setPosition(0);
    m_rightBackEncoder.setPosition(0);
    m_leftBackEncoder.setPosition(0);
  }

  public void Tankarcadeinput(Joystick joystickDriver, double Yval1, double Yval2) {
    Yval1 = joystickDriver.getRawAxis(1); // Left side of the robot
    Yval2 = joystickDriver.getRawAxis(4); // Right side of the robot
    tankarcadeDrive.arcadeDrive(Yval1, Yval2, true);// better for driving, think of aim smoothing on fps games
  }

  public void driveStop() {
    m_leftFrontMotor.set(0);
    m_leftBackMotor.set(0);
    m_rightFrontMotor.set(0);
    m_rightBackMotor.set(0);
  }

  public void driveBack() {
    Left.set(.11);
    Right.set(.1);
  }

  public void driveForward() {
    Left.set(-.11);
    Right.set(-.1);
  }

  public void driveLeft() {
    Left.set(-.11);
    Right.set(.1);
  }

  public void driveRight() {
    Left.set(.11);
    Right.set(-.1);
  }
}
