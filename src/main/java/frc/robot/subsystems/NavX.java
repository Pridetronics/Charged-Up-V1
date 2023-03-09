// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.Constants.OperatorConstants;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NavX extends SubsystemBase {
  private static AHRS m_ahrs;
  private boolean m_autoBalanceXMode;
  private boolean m_autoBalanceYMode;
  public double xAxisRate;
  public double yAxisRate;

  /** Creates a new NavX. */
  public NavX() {
    m_ahrs = RobotContainer.ahrs;
    m_autoBalanceXMode = RobotContainer.autoBalanceXMode;
    m_autoBalanceYMode = RobotContainer.autoBalanceYMode;
    xAxisRate = RobotContainer.joystickDriver.getX();
    yAxisRate = RobotContainer.joystickDriver.getY();
    // try method tests a block of code to execute
    try {
      m_ahrs = new AHRS(SPI.Port.kMXP);
    } catch (RuntimeException ex) {
      DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
    }
    resetNavX();
    SmartDashboard.putString("NavX:", "Test"); // Shows "NavX: Test" on SmartDashboard
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("PitchingAngleDegrees", m_ahrs.getPitch()); // Shows NavX's Rotation around the X axis
                                                                         // value on SmartDashboard
    SmartDashboard.putNumber("RollAngleDegrees", m_ahrs.getRoll()); // Shows NavX's Rotation around the Y axis value on
                                                                    // SmartDashboard
    SmartDashboard.putNumber("YawDegrees", m_ahrs.getYaw()); // Shows NavX's Rotation Z axis value on SmartDashboard
  }

  public void autoBalance() {
    // double pitchAngleDegrees = m_ahrs.getPitch(); //NavX's Rotation around the X
    // axis (left + right)
    double rollAngleDegrees = m_ahrs.getRoll(); // NavX's Rotation around the Y axis (forward + backward)

    // if (!m_autoBalanceXMode &&
    // (Math.abs(pitchAngleDegrees) >=
    // Math.abs(OperatorConstants.kOffBalanceAngleThresholdDegrees))) {
    // m_autoBalanceXMode = true; //If m_autoBalanceXMode AND absolute value of
    // pitchAngleDegrees is
    // //greater/equal than the absolute value of the
    // kOffBalanceAngleThresholdDegree, then m_autoBalanceXMode
    // //will equal true.
    // }
    // else if (m_autoBalanceXMode &&
    // (Math.abs(pitchAngleDegrees) <=
    // Math.abs(OperatorConstants.kOnBalanceAngleThresholdDegrees))) {
    // m_autoBalanceXMode = false; //If m_autoBalanceXMode AND absolute value of
    // pitchAngleDegrees is
    // //less/equal than the absolute value of the kOnBalanceAngleThresholdDegree,
    // then m_autoBalanceXMode
    // //will equal false.
    // }

    if (!m_autoBalanceYMode &&
        (Math.abs(rollAngleDegrees) >= Math.abs(OperatorConstants.kOffBalanceAngleThresholdDegrees))) {
      m_autoBalanceYMode = true; // If m_autoBalanceXMode AND absolute value of rollingAngleDegree is
      // greater/equal than the absolute value of the kOffBalanceAngleThresholdDegree,
      // then m_autoBalanceYMode
      // will equal true.
    } else if (m_autoBalanceYMode &&
        (Math.abs(rollAngleDegrees) <= Math.abs(OperatorConstants.kOnBalanceAngleThresholdDegrees))) {
      m_autoBalanceYMode = false; // If m_autoBalanceYMode AND absolute value of rollingAngleDegree is
      // less/equal than the absolute value of the kOnBalanceAngleThresholdDegree,
      // then m_autoBalanceYMode
      // will equal false.
    }

    // Control drive system automatically,
    // driving in reverse direction of pitch/roll angle,
    // with a magnitude based upon the angle

    // if (m_autoBalanceXMode) {
    // double pitchAngleRadians = pitchAngleDegrees * (Math.PI / 180.0);
    // xAxisRate = Math.sin(pitchAngleRadians) * -1;
    // }
    if (m_autoBalanceYMode) {
      double rollAngleRadians = rollAngleDegrees * (Math.PI / 180.0);
      yAxisRate = Math.sin(rollAngleRadians) * -1;
    }

    try {
      Drive.tankarcadeDrive.tankDrive(xAxisRate, yAxisRate, m_autoBalanceYMode);
    } catch (RuntimeException ex) {
      String err_string = "Drive system error: " + ex.getMessage();
      DriverStation.reportError(err_string, true);
    }
    SmartDashboard.putString("NavX:", "Periodic Test");

  }

  public void resetNavX() {
    m_ahrs.reset();
  }
}
