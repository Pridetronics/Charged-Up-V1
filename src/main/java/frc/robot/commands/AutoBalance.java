// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.NavX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoBalance extends CommandBase {
  private NavX m_navX;

  /** Creates a new AutoBalance. */
  public AutoBalance(NavX navX) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_navX = navX;
    addRequirements(m_navX);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_navX.autoBalance();
    SmartDashboard.putString("AutoBalance:", "Test");
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
