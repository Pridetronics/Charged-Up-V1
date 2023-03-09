// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Drive;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class TargetCenteringVision extends CommandBase {
  private Vision m_vision;
  private Drive m_drive;
  private double Target;

  /** Creates a new TargetCenteringVision. */
  public TargetCenteringVision(Vision vision) {
    m_vision = vision;
    Target = vision.TargetisCentered;
    addRequirements(m_vision);
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
    Target = m_vision.TargetisCentered;
    m_vision.centerTarget();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Target = 1;
    System.out.println("Command Interrrupted");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (Target == 1) {
      return true;
    } else {
      return false;
    }

  }
}
