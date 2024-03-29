// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;


import java.lang.management.OperatingSystemMXBean;

//joystick
import edu.wpi.first.wpilibj.Joystick;

import frc.robot.Constants.OperatorConstants;
//subsystems
import frc.robot.subsystems.Manipulator;


import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class forearmInput extends InstantCommand {
    private Manipulator manipulator;
    private boolean moveForward;
  /** Creates a new JoystickDrive. */
  public forearmInput(Manipulator m_manipulator, boolean forwardButton) {
    manipulator = m_manipulator;
    moveForward = forwardButton;

    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}


  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    manipulator.moveForearm(moveForward);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
