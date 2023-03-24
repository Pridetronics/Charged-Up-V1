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
  private CANSparkMax clawMotor;
  private CANSparkMax foreArmMotor;

  private RelativeEncoder armEncoder;
  private RelativeEncoder forearmEncoder;

  private SparkMaxPIDController shoulderPID;
  private SparkMaxPIDController forearmPID;

  private double lastWristSetpoint = 0;
  private double lastShoulderSetpoint = 0;
  private double forarmSetpoint = 0;
  public boolean currentlyHoming = true;
  public boolean clawEnabled = false;
  private boolean wristMovingLast = false;
  public boolean isTeleOp = true;

  /** Creates a new Manipulator. */
  public Manipulator() {

    // Retrives Motors
    armMotor = RobotContainer.manipulatorArmMotor;
    clawMotor = RobotContainer.manipulatorClawMotor;
    foreArmMotor = RobotContainer.manipulatorForearmMotor;

    // Retrieves Encoders
    armEncoder = RobotContainer.armEncoder;

    forearmEncoder = foreArmMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    forearmEncoder.setPositionConversionFactor(OperatorConstants.kForearmCircum / 12);

    // Retrieves PIDs
    shoulderPID = RobotContainer.shoulderPID;
    RobotContainer.armEncoder.setPositionConversionFactor(360 / (36 * 3.75));

    forearmPID = RobotContainer.forearmPID;

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void zeroEncoder() {
    armEncoder.setPosition(0);
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
      Speed *= 0.55;
    }
    if (Math.abs(Speed) > 0.05) {
      lastShoulderSetpoint = curArmPos;
    }
    SmartDashboard.putNumber("Speed Shoulder", Speed);
    SmartDashboard.putNumber("lastShoulderSetpoint", lastShoulderSetpoint);
    double incrementSpeed = Speed * OperatorConstants.shoulderSpeed;
    // Updates PID/Motor with new speed, ensures velocity is the same
    if (isTeleOp == true) {
      shoulderPID.setReference(lastShoulderSetpoint + incrementSpeed, ControlType.kPosition);
    }
    // armMotor.set(0.2);
  }

  public void setClaw(int setToSpeed) {
    int isClawEnabled = clawEnabled ? 1 : 0;
    double finalSpeed = setToSpeed * OperatorConstants.wristSpeed;
    clawMotor.set(finalSpeed);
    SmartDashboard.putNumber("setToSpeed", finalSpeed);
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

  public void shoulderUpsies() {
    shoulderPID.setReference(OperatorConstants.kmMoveShoulderDegrees, ControlType.kPosition);
  }
}
