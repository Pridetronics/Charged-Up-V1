// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoTurnAround extends CommandBase {
  private Drive m_Drive;
  private double oneeightydegrees;

  /** Creates a new AutoTurnAround. */
  public AutoTurnAround(Drive drive) {
    m_Drive = drive;
    oneeightydegrees = drive.turn180Degrees;
    addRequirements(drive);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_Drive.zeroEncoders();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_Drive.driveRight();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (Math.abs(Drive.m_leftBackEncoder.getPosition()) >= oneeightydegrees) {
      return true;
    } else {
      return false;
    }

  }
}
