// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Manipulator;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.Constants.OperatorConstants;

public class ManipulatorControl extends CommandBase {
  private Manipulator manipulator;
  private Joystick Manipulatorjoystick;

  /** Creates a new ManipulatorControl. */
  public ManipulatorControl(Joystick ManipulatorJoystick, Manipulator m_manipulator) {
    Manipulatorjoystick = ManipulatorJoystick;
    manipulator = m_manipulator;
    addRequirements(m_manipulator);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    manipulator.zeroAll();
    manipulator.grippersClose();

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double YaxisShoulder = Manipulatorjoystick.getRawAxis(1);
    double YaxisWrist = Manipulatorjoystick.getRawAxis(5);
    manipulator.ManipulatorControl(Manipulatorjoystick, YaxisShoulder, YaxisWrist);

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
