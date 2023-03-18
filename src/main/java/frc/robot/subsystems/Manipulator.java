// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

//joystick
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.ManipulatorInput;

//hardware
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import edu.wpi.first.wpilibj.Encoder;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.SparkMaxRelativeEncoder;

public class Manipulator extends SubsystemBase {
  private Joystick joystick;
  private DigitalInput armLimitSwitch = new DigitalInput(OperatorConstants.kShoulderLowerLimitID);
  private DigitalInput upperArmLimitSwitch = new DigitalInput(OperatorConstants.kShoulderUpperLimitID);
  private DigitalInput wristLimitSwitch = RobotContainer.wristLimitSwitch;
  private DigitalInput forearmLimitSwitch = RobotContainer.forearmLimitSwitch;

  private CANSparkMax armMotor;
  private CANSparkMax wristMotor;
  private CANSparkMax foreArmMotor;

  private DoubleSolenoid claw;

  private RelativeEncoder armEncoder;
  private Encoder wristEncoder;
  private RelativeEncoder forearmEncoder;

  private SparkMaxPIDController shoulderPID;
  private PIDController wristPID;
  private SparkMaxPIDController forearmPID;

  private double lastWristSetpoint = 0;
  private double lastShoulderSetpoint = 0;
  private double forarmSetpoint = 0;
  public boolean currentlyHoming = true;
  private boolean wristMovingLast = false;

  /** Creates a new Manipulator. */
  public Manipulator() {

    // Retrives Motors
    armMotor = RobotContainer.manipulatorArmMotor;
    wristMotor = RobotContainer.manipulatorWristMotor;
    foreArmMotor = RobotContainer.manipulatorForearmMotor;
    claw = RobotContainer.clawPiston;

    // Retrieves Encoders
    armEncoder = RobotContainer.armEncoder;
    wristEncoder = RobotContainer.wristEncoder;

    forearmEncoder = foreArmMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    forearmEncoder.setPositionConversionFactor(OperatorConstants.kForearmCircum / 12);

    // Retrieves PIDs
    shoulderPID = RobotContainer.shoulderPID;
    forearmPID = RobotContainer.forearmPID;
    wristPID = RobotContainer.wristPID;

    claw.set(DoubleSolenoid.Value.kReverse);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void zeroEncoder() {
    armEncoder.setPosition(0);
    wristEncoder.reset();
  }

  // Method called by ManipulatorInput to update shoulder
  public void moveArm(double Speed) {
    SmartDashboard.putNumber("First Speed", Speed);
    double conversionFactor = 42;
    // Gets a decimal percentage of the total amount of rotations made (A full
    // roation == 1)
    double curArmPos = armEncoder.getPosition();
    // Checks if motor is out of limits
    boolean upperLimit = upperArmLimitSwitch.get();
    boolean lowerLimit = armLimitSwitch.get();
    SmartDashboard.putBoolean("UpperLimit Shoulder", upperLimit);
    SmartDashboard.putBoolean("lowerLimit Shoudldrr", lowerLimit);

    // Updates the motor speed based on limits
    if (upperLimit) {
      Speed = Math.max(Speed, 0);
    } else if (lowerLimit) {
      Speed = Math.min(Speed, 0);
    }
    if (Speed > 0) {
      Speed *= 0.1;
    }
    if (Math.abs(Speed) > 0.05) {
      lastShoulderSetpoint = curArmPos;
    }
    SmartDashboard.putNumber("Speed Shoulder", Speed);
    SmartDashboard.putNumber("lastShoulderSetpoint", lastShoulderSetpoint);
    double incrementSpeed = Speed * OperatorConstants.shoulderSpeed;
    // Updates PID/Motor with new speed, ensures velocity is the same

    shoulderPID.setReference(lastShoulderSetpoint + incrementSpeed, ControlType.kPosition);
    // armMotor.set(0.2);
  }

  // Method called by ManipulatorInput to update wrist
  public void moveWrist(boolean up, boolean down) {
    SmartDashboard.putBoolean("Wrist Limit", wristLimitSwitch.get());
    if (!currentlyHoming) {
      // Degrees the wrist has rotated
      double curWristPos = wristEncoder.getDistance() * 4.33;
      SmartDashboard.putNumber("Current Wrist Position", curWristPos);
      // Checks if motor is out of limits
      boolean lowerLimit = curWristPos >= 95;
      boolean upperLimit = wristLimitSwitch.get();

      // Sets speed based on what buttons are pressed
      int upMotion = up ? -1 : 0;
      int downMotion = down ? 1 : 0;

      // Updates the motor speed based on limits
      if (upperLimit) {
        upMotion = 0;
      } else if (lowerLimit) {
        downMotion = 0;
      }
      // Updates PID/Motor with new speed, ensures velocity is the same
      double newPos = wristPID.calculate(
          wristEncoder.getDistance(),
          lastWristSetpoint + (upMotion + downMotion) * OperatorConstants.wristSpeed);
      SmartDashboard.putNumber("Wrist PID output", newPos);
      SmartDashboard.putNumber("Goal speed",
          lastWristSetpoint + (upMotion + downMotion) * OperatorConstants.wristSpeed);
      SmartDashboard.putNumber("Wrist set point", lastWristSetpoint);
      SmartDashboard.putNumber("Wrist Encoder", wristEncoder.getDistance());

      wristMotor.set(newPos);

      if (upMotion + downMotion != 0) {
        lastWristSetpoint = wristEncoder.getDistance();
        wristMovingLast = true;
      } else {
        if (wristMovingLast == true) {
          lastWristSetpoint = wristEncoder.getDistance();
        }
        wristMovingLast = false;
      }
    }

  }

  public void moveForearm(boolean forwards) {
    int direction = forwards ? 1 : -1;

    // Converts the rotations into a form that works with the encoders, while also
    // setting the direction it needs to go in
    double increment = OperatorConstants.kForearmIncrement * direction;
    // Gets the current position
    double currentPos = forarmSetpoint;
    // Find where the rotational goal is, set into the encoders
    // countsPerRevlolutions form
    double moveTo = increment + currentPos;

    // Checks if any limit bounds have been reached
    boolean lowerLimit = (!forearmLimitSwitch.get());
    boolean upperLimit = moveTo > OperatorConstants.forearmExtendLimit;

    // Updates the goal position based on limits
    if (upperLimit && forwards) {
      moveTo = Math.min(moveTo, OperatorConstants.forearmExtendLimit);
    }
    if (lowerLimit) {
      moveTo = Math.max(moveTo, 0);
    }

    forarmSetpoint = moveTo;
  };

  public void forarmUpdate() {

    if (!currentlyHoming) {
      double moveTo = forarmSetpoint;
      boolean lowerLimit = !forearmLimitSwitch.get();

      double currentPos = forearmEncoder.getPosition();
      if (lowerLimit) {
        moveTo = Math.max(moveTo, currentPos);
      }

      forearmPID.setReference(moveTo, ControlType.kPosition);
    }
  }

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
