// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.NavX;

public class PIDAutoBalance extends CommandBase {
  private Drive m_drive;
  private NavX m_navX;

  /** Creates a new PIDAutoBalance. */
  public PIDAutoBalance(NavX navX, Drive drive) {
    m_drive = drive;
    m_navX = navX;
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
    m_navX.PIDAutoBalance();
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
