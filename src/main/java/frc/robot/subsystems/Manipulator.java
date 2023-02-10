// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

//joystick
import edu.wpi.first.wpilibj.Joystick;

import frc.robot.RobotContainer;

//hardware
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.DoubleSolenoid;


import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder;

public class Manipulator extends SubsystemBase {
  private Joystick joystick;
  private Manipulator manipulator;

  private CANSparkMax armMotor;
  private CANSparkMax wristMotor;
  private RelativeEncoder armEncoder;
  private RelativeEncoder wristEncoder;

  /** Creates a new Manipulator. */
  public Manipulator(Joystick joystickManipulator, Manipulator m_manipulator) {
    joystick = joystickManipulator;
    manipulator = m_manipulator;

    armMotor = RobotContainer.manipulatorArmMotor;
    wristMotor = RobotContainer.manipulatorWristMotor;

    armEncoder = armMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    wristEncoder = wristMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void moveArm(double Speed) {

    double curArmPos = armEncoder.getPosition()/armEncoder.getCountsPerRevolution();

    boolean upperLimit = curArmPos > 0.125f;
    boolean lowerLimit = curArmPos < -0.125f;

    if (upperLimit) {
      Speed = Math.min(Speed, 0);
    } else if (lowerLimit) {
      Speed = Math.max(Speed, 0);
    }

    armMotor.set(Speed);
  }

  public void moveWrist(boolean up, boolean down) {
    double curWristPos = wristEncoder.getPosition()/wristEncoder.getCountsPerRevolution();

    boolean upperLimit = curWristPos > 0.25f;
    boolean lowerLimit = curWristPos < -0.25f;

    int upMotion = up ? 1 : 0;
    int downMotion = down ? -1 : 0;

    if (upperLimit) {
      upMotion = 0;
    } else if (lowerLimit) {
      downMotion = 0;
    }

    wristMotor.set(upMotion + downMotion);
  }


}
