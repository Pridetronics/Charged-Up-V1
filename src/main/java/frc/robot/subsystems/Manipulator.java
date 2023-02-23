// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

//joystick
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.RobotContainer;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.ManipulatorInput;

//hardware
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DigitalInput;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.SparkMaxRelativeEncoder;

public class Manipulator extends SubsystemBase {
  private Joystick joystick;
  private Manipulator manipulator;
  private DigitalInput armLimitSwitch = new DigitalInput(OperatorConstants.kArmLimitID);
  private DigitalInput wristLimitSwitch = new DigitalInput(OperatorConstants.kClawLimitID);

  private CANSparkMax armMotor;
  private CANSparkMax wristMotor;
  private DoubleSolenoid claw;
  private RelativeEncoder armEncoder;
  private RelativeEncoder wristEncoder;
  private DigitalInput armLowerLimit = new DigitalInput(OperatorConstants.kArmLimitID);
  private DigitalInput wristLowerLimit = new DigitalInput(OperatorConstants.kClawLimitID);

  private SparkMaxPIDController shoulderPID;


  /** Creates a new Manipulator. */
  public Manipulator(Joystick joystickManipulator, Manipulator m_manipulator) {
    joystick = joystickManipulator;
    manipulator = m_manipulator;

    armMotor = RobotContainer.manipulatorArmMotor;
    wristMotor = RobotContainer.manipulatorWristMotor;
    claw = RobotContainer.clawSolenoid;

    armEncoder = armMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    wristEncoder = wristMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);

    shoulderPID = RobotContainer.shoulderPID;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void moveArm(double Speed) {

    double curArmPos = armEncoder.getPosition()/armEncoder.getCountsPerRevolution();

    boolean upperLimit = curArmPos > 0.125f;
    boolean lowerLimit = !armLimitSwitch.get();

    if (upperLimit) {
      Speed = Math.min(Speed, 0);
    } else if (lowerLimit) {
      Speed = Math.max(Speed, 0);
    }

    shoulderPID.setReference(Speed, ControlType.kVelocity);
  }

  public void moveWrist(boolean up, boolean down) {
    double curWristPos = wristEncoder.getPosition()/wristEncoder.getCountsPerRevolution();

    boolean upperLimit = curWristPos > 0.25f;
    boolean lowerLimit = !wristLimitSwitch.get();

    int upMotion = up ? 1 : 0;
    int downMotion = down ? -1 : 0;

    if (upperLimit) {
      upMotion = 0;
    } else if (lowerLimit) {
      downMotion = 0;
    }

    shoulderPID.setReference(upMotion + downMotion, ControlType.kVelocity);
  }

  public void moveTelescopic()

  public void controlClaw() {
    boolean clawInput = joystick.getRawButtonPressed(OperatorConstants.clawOpenCloseButtonNumber);
    if(clawInput){
      claw.toggle(); //Please don't hate me for this code
    }
  }


}
