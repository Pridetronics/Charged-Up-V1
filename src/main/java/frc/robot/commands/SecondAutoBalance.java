// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.NavX;

public class SecondAutoBalance extends CommandBase {
  private NavX m_navX;
  private Drive m_drive;
  // private AHRS m_ahrs;

  /** Creates a new AutoBalance_2. */
  public SecondAutoBalance(NavX navX, Drive drive) {
    // m_ahrs = RobotContainer.ahrs;
    m_navX = navX;
    m_drive = drive;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_navX, m_drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_navX.secondAutoBalance();
    // double rollAngleDegrees = m_ahrs.getRoll();
    // if (rollAngleDegrees > Constants.OperatorConstants.kFirstPitchDegree) {
    // m_drive.autoBalanceForward();
    // } else if (rollAngleDegrees < Constants.OperatorConstants.kSecondPitchDegree)
    // {
    // m_drive.autoBalanceBackward();
    // } else {
    // m_drive.driveStop();
    // }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
