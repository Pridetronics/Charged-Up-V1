// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.*;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoRotateLeft extends CommandBase {
  private Drive m_Drive;

  /** Creates a new AutoRotateLeft. */
  public AutoRotateLeft(Drive drive) {
    m_Drive = drive;

    addRequirements(drive);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_Drive.zeroEncoders();
    RobotContainer.leftBackMotor.setIdleMode(IdleMode.kBrake);
    RobotContainer.leftFrontMotor.setIdleMode(IdleMode.kBrake);
    RobotContainer.rightBackMotor.setIdleMode(IdleMode.kBrake);
    RobotContainer.rightFrontMotor.setIdleMode(IdleMode.kBrake);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    m_Drive.driveLeft();

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_Drive.driveStop();
    RobotContainer.leftBackMotor.setIdleMode(IdleMode.kCoast);
    RobotContainer.leftFrontMotor.setIdleMode(IdleMode.kCoast);
    RobotContainer.rightBackMotor.setIdleMode(IdleMode.kCoast);
    RobotContainer.rightFrontMotor.setIdleMode(IdleMode.kCoast);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (Math.abs(Drive.m_leftBackEncoder.getPosition()) >= Constants.OperatorConstants.turn90Degrees) {
      return true;
    } else {
      return false;
    }

  }
}
