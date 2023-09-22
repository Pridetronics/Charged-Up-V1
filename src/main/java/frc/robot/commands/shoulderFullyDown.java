// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Manipulator;

public class shoulderFullyDown extends CommandBase {

Manipulator m_Manipulator;

  /** Creates a new shoulderFullyDown. */
  public shoulderFullyDown(Manipulator manipulator) {
    m_Manipulator = manipulator;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
    m_Manipulator.moveArm(0.45);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    boolean lowerLimit = m_Manipulator.getLowerLimitState();
    return lowerLimit;
  }
}
