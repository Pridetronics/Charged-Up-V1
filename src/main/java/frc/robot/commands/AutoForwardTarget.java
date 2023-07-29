// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Vision;
import frc.robot.Constants;
import frc.robot.RobotContainer;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoForwardTarget extends CommandBase {
  private Drive m_drive;
  private double startPos;

  /** Creates a new AutoMoveForward. */
  public AutoForwardTarget(Drive drive) {
    m_drive = drive;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.leftBackMotor.setIdleMode(IdleMode.kBrake);
    RobotContainer.leftFrontMotor.setIdleMode(IdleMode.kBrake);
    RobotContainer.rightBackMotor.setIdleMode(IdleMode.kBrake);
    RobotContainer.rightFrontMotor.setIdleMode(IdleMode.kBrake);
    //m_drive.zeroEncoders();
    SmartDashboard.putNumber("forward start pos", Drive.m_leftBackEncoder.getPosition());
    startPos = Drive.m_leftBackEncoder.getPosition();
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
    RobotContainer.leftBackMotor.setIdleMode(IdleMode.kBrake);
    RobotContainer.leftFrontMotor.setIdleMode(IdleMode.kBrake);
    RobotContainer.rightBackMotor.setIdleMode(IdleMode.kBrake);
    RobotContainer.rightFrontMotor.setIdleMode(IdleMode.kBrake);
    SmartDashboard.putNumber("Forward end pos", Drive.m_leftBackEncoder.getPosition());
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    SmartDashboard.putNumber("Forward checking", Drive.m_leftBackEncoder.getPosition());
    if (Math.abs(Drive.m_leftBackEncoder.getPosition()-startPos) >= Constants.OperatorConstants.targetMid) {
      return true;
    } else {
      return false;
    }
  }
}
