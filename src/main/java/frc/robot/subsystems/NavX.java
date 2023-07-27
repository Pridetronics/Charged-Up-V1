// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.Constants.OperatorConstants;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NavX extends SubsystemBase {
  private static AHRS m_ahrs;
  private boolean m_autoBalanceXMode;
  private boolean m_autoBalanceYMode;
  public double xAxisRate;
  public double yAxisRate;
  public PIDController PIDController;

  private Drive m_drive;

  private Accelerometer m_accelerometer;

  /** Creates a new NavX. */
  public NavX() {
    m_drive = RobotContainer.m_drive;
    m_ahrs = RobotContainer.ahrs;
    m_autoBalanceXMode = RobotContainer.autoBalanceXMode;
    m_autoBalanceYMode = RobotContainer.autoBalanceYMode;
    xAxisRate = RobotContainer.joystickDriver.getX();
    yAxisRate = RobotContainer.joystickDriver.getY();
    PIDController = new PIDController(0.35, 0.0, 0.0); // P: 0.35
    m_accelerometer = RobotContainer.accelerometer;

    // try method tests a block of code to execute
    try {
      m_ahrs = new AHRS(SPI.Port.kMXP);
    } catch (RuntimeException ex) {
      DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // SmartDashboard.putNumber("Roll Degree", m_ahrs.getRoll());
    // SmartDashboard.putNumber("Pitch Degree", m_ahrs.getPitch());
    // SmartDashboard.putBoolean("Calibration Finished?", m_ahrs.isCalibrating());\
    // SmartDashboard.putNumber("AccelerometerX", m_accelerometer.getX());
    SmartDashboard.putNumber("AccelerometerY", m_accelerometer.getY() - 0.05);
  }

  public void autoBalance() {

    double pitchAngleDegrees = m_ahrs.getPitch(); // NavX's Rotation around the X
    // axis (left + right)
    double rollAngleDegrees = m_ahrs.getRoll(); // NavX's Rotation around the Y
    // // axis (forward + backward)

    // Probably try to go with the rollAngleDegrees??
    // if and else statement
    // 3944

    if (!m_autoBalanceXMode &&
        (Math.abs(pitchAngleDegrees) >= Math.abs(OperatorConstants.kOffBalanceAngleThresholdDegrees))) {
      m_autoBalanceXMode = true; // If m_autoBalanceXMode AND absolute value of
      // pitchAngleDegrees is greater/equal than the absolute value of the
      // kOffBalanceAngleThresholdDegree, then m_autoBalanceXMode will equal true.
    } else if (m_autoBalanceXMode &&
        (Math.abs(pitchAngleDegrees) <= Math.abs(OperatorConstants.kOnBalanceAngleThresholdDegrees))) {
      m_autoBalanceXMode = false; // If m_autoBalanceXMode AND absolute value of
      // pitchAngleDegrees is
      // less/equal than the absolute value of the kOnBalanceAngleThresholdDegree,
      // then m_autoBalanceXMode will equal false.
    }

    if (!m_autoBalanceYMode &&
        (Math.abs(pitchAngleDegrees) >= Math.abs(OperatorConstants.kOffBalanceAngleThresholdDegrees))) {
      m_autoBalanceYMode = true; // If m_autoBalanceXMode AND absolute value of
      // rollingAngleDegree is
      // greater/equal than the absolute value of the
      // kOffBalanceAngleThresholdDegree,
      // then m_autoBalanceYMode
      // will equal true.
    } else if (m_autoBalanceYMode &&
        (Math.abs(pitchAngleDegrees) <= Math.abs(OperatorConstants.kOnBalanceAngleThresholdDegrees))) {
      m_autoBalanceYMode = false; // If m_autoBalanceYMode AND absolute value of
      // rollingAngleDegree is
      // less/equal than the absolute value of the kOnBalanceAngleThresholdDegree,
      // then m_autoBalanceYMode
      // will equal false.
    }

    // Control drive system automatically,
    // driving in reverse direction of pitch/roll angle,
    // with a magnitude based upon the angle

    if (m_autoBalanceXMode) {
      double pitchAngleRadians = pitchAngleDegrees * (Math.PI / 180.0);
      xAxisRate = Math.sin(pitchAngleRadians) * -1;
    }
    if (m_autoBalanceYMode) {
      double rollAngleRadians = rollAngleDegrees * (Math.PI / 180.0);
      yAxisRate = Math.sin(rollAngleRadians) * -1;
    }

    try {
      Drive.tankArcadeDrive.tankDrive(xAxisRate, yAxisRate, m_autoBalanceXMode);
    } catch (RuntimeException ex) {
      String err_string = "Drive system error: " + ex.getMessage();
      DriverStation.reportError(err_string, true);
    }
  }

  public static void resetYaw() {
    m_ahrs.reset();
  }

  // public void secondAutoBalance() {
  // double rollAngleDegrees = m_ahrs.getRoll();
  // if (rollAngleDegrees > Constants.OperatorConstants.kFirstRollDegree) {
  // m_drive.autoBalanceForward();
  // } else if (rollAngleDegrees < Constants.OperatorConstants.kSecondRollDegree)
  // {
  // m_drive.autoBalanceBackward();
  // } else {
  // m_drive.driveStop();
  // }
  // }
  public void PIDAutoBalance() {
    double pitchAngleDegrees = m_ahrs.getRoll();
    pitchAngleDegrees /= 10;
    double PIDValue = PIDController.calculate(pitchAngleDegrees, 0);
    SmartDashboard.putNumber("PIDValue", PIDValue);
    SmartDashboard.putNumber("PitchAngle", pitchAngleDegrees);
    if (Math.abs(PIDValue) > Constants.OperatorConstants.kFirstRollDegree) {
      m_drive.autoBalancePIDSet(PIDValue);
    } else {
      m_drive.driveStop();
    }
    // double rollAngleDegrees = m_ahrs.getRoll();
    // rollAngleDegrees *= 1.3;
    // double PIDValue = -PIDController.calculate(rollAngleDegrees, 0);
    // SmartDashboard.putNumber("PIDValue", PIDValue);
    // SmartDashboard.putNumber("RollAngle", rollAngleDegrees);
    // if (Math.abs(PIDValue) > Constants.OperatorConstants.kFirstRollDegree) {
    // m_drive.autoBalancePIDSet(PIDValue);
    // } else {
    // m_drive.driveStop();
    // }
  }

  public void RoboRioAccelerometer() {
    // double accelerometerX = m_accelerometer.getX();
    double accelerometerY = m_accelerometer.getY() - 0.05; // 0.03
    double PIDValue = -PIDController.calculate(accelerometerY, 0);
    SmartDashboard.putNumber("PIDValue", PIDValue);
    if (Math.abs(PIDValue) > Constants.OperatorConstants.kFirstRollDegree
        || PIDValue < Constants.OperatorConstants.kSecondRollDegree) {
      m_drive.autoBalancePIDSet(PIDValue);
    } else if (-0.04 <= PIDValue && PIDValue <= 0.04) {
      m_drive.driveStop();
    } else {
      m_drive.driveStop();
    }
  }
}
