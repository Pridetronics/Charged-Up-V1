// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Vision;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoMoveForward extends CommandBase {
  private Drive m_drive;
  private Double desiredDistance;
  private Double ticksPerInch;
  private Double ticksPerRotation;

  /** Creates a new AutoMoveForward. */
  public AutoMoveForward(Drive drive) {
    m_drive = drive;
    ticksPerInch = drive.TPI;
    ticksPerRotation = drive.TPR;
    desiredDistance = drive.desiredDistance;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    m_drive.zeroEncoders();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_drive.calculateDistance();
    m_drive.driveForward();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (Math.abs(Drive.m_leftBackEncoder.getPosition()) < desiredDistance)
      m_drive.driveForward();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (Math.abs(Drive.m_leftBackEncoder.getPosition()) >= desiredDistance) {
      return true;
    } else {
      return false;
    }
  }
}
