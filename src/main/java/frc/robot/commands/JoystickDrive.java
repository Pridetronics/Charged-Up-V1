// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

//joystick
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.RobotContainer;
import frc.robot.Constants.OperatorConstants;
//subsystems
import frc.robot.subsystems.Drive;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class JoystickDrive extends CommandBase {
  private Drive m_drive;
  private Joystick m_joystickdriver;
  // private boolean m_brake;
  // private double Mode = 0;

  /** Creates a new JoystickDrive. */
  public JoystickDrive(Joystick joystickDriver, Drive drive) {
    m_joystickdriver = joystickDriver;
    m_drive = drive;
    addRequirements(m_drive);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // m_brake =
    // m_joystickdriver.getRawButtonPressed(OperatorConstants.kToggleBrake);

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double Yval1 = m_joystickdriver.getRawAxis(1);
    double Yval2 = m_joystickdriver.getRawAxis(4);
    m_drive.Tankarcadeinput(m_joystickdriver, Yval1, Yval2);
    // if (m_brake) {
    // m_drive.Brake();
    // Mode = 1;
    // } else if (m_brake && Mode == 1) {
    // m_drive.Coast();
    // Mode = 0;
    // }

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
