// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.OperatorConstants; 
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NavX extends SubsystemBase {
  private static AHRS m_ahrs = RobotContainer.ahrs; 
  private boolean m_autoBalanceXMode = RobotContainer.autoBalanceXMode;
  private boolean m_autoBalanceYMode = RobotContainer.autoBalanceYMode;
  public double xAxisRate = RobotContainer.joystickDriver.getX();
  public double yAxisRate = RobotContainer.joystickDriver.getY();
  public double pitchAngleDegrees = m_ahrs.getPitch();
  public double rollAngleDegrees = m_ahrs.getRoll();

  /** Creates a new NavX. */
  public NavX() {
    try {
      m_ahrs = new AHRS(SPI.Port.kMXP); 
        } 
      catch (RuntimeException ex ) {
        DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
      }
      SmartDashboard.putString("NavX:", "Test");
    }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
      if (!m_autoBalanceXMode && 
           (Math.abs(pitchAngleDegrees) >= 
            Math.abs(OperatorConstants.kOffBalanceAngleThresholdDegrees))) {
          m_autoBalanceXMode = true;
      }
        else if (m_autoBalanceXMode && 
                (Math.abs(pitchAngleDegrees) <= 
                 Math.abs(OperatorConstants.kOnBalanceAngleThresholdDegrees))) {
          m_autoBalanceXMode = false;
      }

      if (!m_autoBalanceYMode && 
           (Math.abs(pitchAngleDegrees) >= 
            Math.abs(OperatorConstants.kOffBalanceAngleThresholdDegrees))) {
          m_autoBalanceYMode = true;
      }
        else if (m_autoBalanceYMode && 
                (Math.abs(pitchAngleDegrees) <= 
                 Math.abs(OperatorConstants.kOnBalanceAngleThresholdDegrees))) {
          m_autoBalanceYMode = false;
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
        Drive.tankDrive.tankDrive(xAxisRate, yAxisRate, m_autoBalanceXMode);
      } 
      catch(RuntimeException ex) {
          String err_string = "Drive system error: " + ex.getMessage();
          DriverStation.reportError(err_string, true);
      }
      SmartDashboard.putString("NavX:", "Periodic Test");
  }
}
