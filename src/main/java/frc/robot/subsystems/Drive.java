// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
//drive train
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
//hardware
import edu.wpi.first.wpilibj.Joystick;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder;
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
  public static DifferentialDrive tankDrive;
  public MotorControllerGroup Left;
  public MotorControllerGroup Right;
  // Variables
  public double TPR;// ticks per revolution
  public double TPI;// Ticks per inch
  public double wheelCircumference;
  public double desiredDistance;

  /** Creates a new Drive. */
  public Drive(Joystick joystickDriver) {

    // Motors are now set to same as robotcontainer
    m_rightFrontMotor = RobotContainer.rightFrontMotor;
    m_leftFrontMotor = RobotContainer.leftFrontMotor;
    m_rightBackMotor = RobotContainer.rightBackMotor;
    m_leftBackMotor = RobotContainer.leftBackMotor;

    // Detects and sets encoder values
    m_rightFrontEncoder = m_rightFrontMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    m_leftFrontEncoder = m_leftFrontMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    m_rightBackEncoder = m_rightBackMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    m_leftBackEncoder = m_leftBackMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);

    // Zeroes encoders
    zeroEncoders();
    // Two motorcontroller groups that will act as left and right in tank drive
    Left = new MotorControllerGroup(m_leftFrontMotor, m_leftBackMotor);
    Right = new MotorControllerGroup(m_rightFrontMotor, m_rightBackMotor);
    tankDrive = new DifferentialDrive(Left, Right);

    tankDrive.setSafetyEnabled(true);
    tankDrive.setExpiration(.1);
    tankDrive.setMaxOutput(1);

    // calculations
    TPR = m_leftFrontEncoder.getCountsPerRevolution();
    SmartDashboard.putNumber("Ticks per revolution", TPR);
    wheelCircumference = 2 * (Math.PI * 3);// circumference of wheel in inches
    TPI = TPR * wheelCircumference;
    desiredDistance = 24;// 2 feet in inches, sujbect to change
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

  public void Tankinput(Joystick joystickDriver, double Yval1, double Yval2) {
    Yval1 = joystickDriver.getRawAxis(1); // Left side of the robot
    Yval2 = joystickDriver.getRawAxis(5); // Right side of the robot
    // reduces speed so field is not torn apart
    Yval1 = Yval1 * .61; // .61
    Yval2 = Yval2 * .6; // .6
    tankDrive.tankDrive(Yval1, Yval2, true);
  }

  public void driveStop() {
    m_leftFrontMotor.set(0);
    m_leftBackMotor.set(0);
    m_rightFrontMotor.set(0);
    m_rightBackMotor.set(0);
  }

}
