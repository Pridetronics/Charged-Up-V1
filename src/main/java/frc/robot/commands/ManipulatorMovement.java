// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Manipulator;

public class ManipulatorMovement extends CommandBase {
  private Manipulator m_manipulator;
  private Joystick m_joystickManipulator;
  private double YaxisShoulder;
  private double YaxisWrist;

  /** Creates a new ManipulatorMovement. */
  public ManipulatorMovement(Joystick joystickManipulator, Manipulator Manipulator) {
    m_joystickManipulator = joystickManipulator;
    m_manipulator = Manipulator;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_manipulator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    YaxisShoulder = m_joystickManipulator.getRawAxis(2);
    YaxisWrist = m_joystickManipulator.getRawAxis(5);
    m_manipulator.ManipulatorMovement(m_joystickManipulator, YaxisShoulder, YaxisWrist);
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
