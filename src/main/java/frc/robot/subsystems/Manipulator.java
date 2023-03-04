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
  private DigitalInput armLimitSwitch = new DigitalInput(OperatorConstants.kShoulderLowerLimitID);
  private DigitalInput upperArmLimitSwitch = new DigitalInput(OperatorConstants.kShoulderUpperLimitID);
  private DigitalInput wristLimitSwitch = new DigitalInput(OperatorConstants.kWristLimitID);
  private DigitalInput forearmLimitSwitch = new DigitalInput(OperatorConstants.kForearmLimitID);

  private CANSparkMax armMotor;
  private CANSparkMax wristMotor;
  private CANSparkMax foreArmMotor;

  private DoubleSolenoid claw;

  private RelativeEncoder armEncoder;
  private RelativeEncoder wristEncoder;
  private RelativeEncoder forearmEncoder;

  private SparkMaxPIDController shoulderPID;
  private SparkMaxPIDController forearmPID;


  /** Creates a new Manipulator. */
  public Manipulator(Joystick joystickManipulator, Manipulator m_manipulator) {
    //Retrieves input devices
    joystick = joystickManipulator;
    manipulator = m_manipulator;

    //Retrives Motors
    armMotor = RobotContainer.manipulatorArmMotor;
    wristMotor = RobotContainer.manipulatorWristMotor;
    foreArmMotor = RobotContainer.manipulatorForearmMotor;
    claw = RobotContainer.clawPiston;

    //Retrieves Encoders
    armEncoder = armMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    wristEncoder = wristMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    forearmEncoder = foreArmMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);

    //Retrieves PIDs
    shoulderPID = RobotContainer.shoulderPID;
    forearmPID = RobotContainer.forearmPID;
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
    boolean upperLimit = !upperArmLimitSwitch.get();
    boolean lowerLimit = !armLimitSwitch.get();

    //Updates the motor speed based on limits
    if (upperLimit) {
      Speed = Math.min(Speed, 0);
    } else if (lowerLimit) {
      Speed = Math.max(Speed, 0);
    }

    //Updates PID/Motor with new speed, ensures velocity is the same
    shoulderPID.setReference(Speed*OperatorConstants.shoulderSpeed, ControlType.kVelocity);
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
    shoulderPID.setReference((upMotion + downMotion)*OperatorConstants.wristSpeed, ControlType.kVelocity);
  }

  public void moveForearm(boolean forwards) {
    int direction = forwards ? 1 : -1;

    //Gets the number of rotations to make in a decimal percent form (1 full rotation == 1)
    double rotationsToMake = OperatorConstants.kForearmIncrement/OperatorConstants.kForearmCircum;

    //Converts the rotations into a form that works with the encoders, while also setting the direction it needs to go in
    double increment = rotationsToMake*forearmEncoder.getCountsPerRevolution()*direction;
    //Gets the current position
    double currentPos = forearmEncoder.getPosition();
    //Find where the rotational goal is, set into the encoders countsPerRevlolutions form
    double moveTo = increment+currentPos;

    //Converts the encoder positioning to a decimal percent of rotations (1 full rotation == 1)
    double rotationsMade = currentPos/forearmEncoder.getCountsPerRevolution();
    //Converts the total rotation positioning into a distance of inches moved
    double distanceMade = rotationsMade*OperatorConstants.kForearmCircum;

    //Checks if any limit bounds have been reached
    boolean lowerLimit = !forearmLimitSwitch.get();
    boolean upperLimit = distanceMade+increment > OperatorConstants.forearmExtendLimit;

    //Updates the goal position based on limits
    if (lowerLimit && !forwards) {
      moveTo = Math.max(moveTo, currentPos);
    } else if (upperLimit && forwards) {
      moveTo = Math.min(moveTo, currentPos);
    }

    //Tells motor to move to the new position
    forearmPID.setReference(moveTo, ControlType.kPosition);
  };

  public void toggleClaw() {
    
    DoubleSolenoid.Value clawEnabled = claw.get();
    if (clawEnabled == DoubleSolenoid.Value.kForward) {
      clawEnabled = DoubleSolenoid.Value.kReverse;
    } else {
      clawEnabled = DoubleSolenoid.Value.kForward;
    }

    claw.set(clawEnabled);
  }

}
