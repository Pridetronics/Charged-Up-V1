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
    //Retrieves input devices
    joystick = joystickManipulator;
    manipulator = m_manipulator;

    //Retrives Motors
    armMotor = RobotContainer.manipulatorArmMotor;
    wristMotor = RobotContainer.manipulatorWristMotor;
    claw = RobotContainer.clawSolenoid;

    //Retrieves Encoders
    armEncoder = armMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    wristEncoder = wristMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);

    //Retrieves PIDs
    shoulderPID = RobotContainer.shoulderPID;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  //Method called by ManipulatorInput to update shoulder
  public void moveArm(double Speed) {

    //Gets a decimal percentage of the total amount of rotations made (A full roation == 1)
    double curArmPos = armEncoder.getPosition()/armEncoder.getCountsPerRevolution();

    //Checks if motor is out of limits
    boolean upperLimit = curArmPos > 0.125f;
    boolean lowerLimit = !armLimitSwitch.get();

    //Updates the motor speed based on limits
    if (upperLimit) {
      Speed = Math.min(Speed, 0);
    } else if (lowerLimit) {
      Speed = Math.max(Speed, 0);
    }

    //Updates PID/Motor with new speed, ensures velocity is the same
    shoulderPID.setReference(Speed, ControlType.kVelocity);
  }

  //Method called by ManipulatorInput to update wrist
  public void moveWrist(boolean up, boolean down) {
    
    //Gets a decimal percentage of the total amount of rotations made (A full roation == 1)
    double curWristPos = wristEncoder.getPosition()/wristEncoder.getCountsPerRevolution();

    //Checks if motor is out of limits
    boolean upperLimit = curWristPos > 0.25f;
    boolean lowerLimit = !wristLimitSwitch.get();

    //Sets speed based on what buttons are pressed
    int upMotion = up ? 1 : 0;
    int downMotion = down ? -1 : 0;

    //Updates the motor speed based on limits
    if (upperLimit) {
      upMotion = 0;
    } else if (lowerLimit) {
      downMotion = 0;
    }

    //Updates PID/Motor with new speed, ensures velocity is the same
    shoulderPID.setReference(upMotion + downMotion, ControlType.kVelocity);
  }

  //Forearm code
  public void moveTelescopic(double distance) {
    


  }
    




  public void controlClaw() {
    boolean clawInput = joystick.getRawButtonPressed(OperatorConstants.clawOpenCloseButtonNumber);
    if(clawInput){
      claw.toggle(); //Please don't hate me for this code
    }
  }


}
