// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Manipulator;

public class JoystickManipulator extends CommandBase {
  private Manipulator m_manipulator;
  private Joystick m_joystickManipulator;
  private double YaxisShoulder;
  private double YaxisWrist;

  /** Creates a new ManipulatorMovement. */
  public JoystickManipulator(Joystick joystickManipulator, Manipulator Manipulator) {
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
    m_manipulator.ManipulatorInput(m_joystickManipulator, YaxisShoulder, YaxisWrist);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // If isShoulderAtBottom() == true, return true.
    // || means "or".
    // If isShoulderAtTop() == true, return true.
    // If isWristAtBottom() == true, return true.
    // If isWristAtTop() == true, return true.
    // If none of these are true, return false, until it gets true.
    // If = if condition 1 is true
    // Else if = if condition 1 is false, condition 2 is true.
    // Else = if condition 1 and 2 is false.
    if (m_manipulator.isShoulderAtBottom() == true || m_manipulator.isShoulderAtTop() == true) {
      return true;
    } else if (m_manipulator.isWristAtBottom() == true || m_manipulator.isWristAtTop() == true) {
      return true;
    } else {
      return false;
    }
    // return false;
  }
  // Somehow add limit switches from Shoulder and Wrist? Test code
}
