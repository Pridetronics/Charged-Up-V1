// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.Manipulator;

public class ForearmExtension extends CommandBase {
  private Manipulator m_manipulator;

  /** Creates a new ShoulderExtension. */
  public ForearmExtension(Manipulator manipulator) {
    m_manipulator = manipulator;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(manipulator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_manipulator.forearmExtension();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_manipulator.stopForearmMotor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (m_manipulator.m_forearmEncoder.getPosition() < OperatorConstants.encoderForearmDistance) {
      return false;
    } else {
      return true;
    }
  }
}
