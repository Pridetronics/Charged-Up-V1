// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoMoveForwardsLong extends CommandBase {
  private Drive m_drive;

  /** Creates a new AutoMoveForwardsLong. */
  public AutoMoveForwardsLong(Drive drive) {
    m_drive = drive;
    addRequirements(drive);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_drive.zeroEncoders();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_drive.driveForward();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drive.driveStop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (Math.abs(Drive.m_leftBackEncoder.getPosition()) >= OperatorConstants.longDistance) {
      return true;
    } else {
      return false;
    }
  }
}
