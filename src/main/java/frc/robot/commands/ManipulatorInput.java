// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;
import java.lang.management.OperatingSystemMXBean;

//joystick
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.OperatorConstants;
//subsystems
import frc.robot.subsystems.Manipulator;


import edu.wpi.first.wpilibj2.command.CommandBase;

public class ManipulatorInput extends CommandBase {
    private Joystick joystick;
    private Manipulator manipulator;

  /** Creates a new JoystickDrive. */
  public ManipulatorInput(Joystick joystickManipulator, Manipulator m_manipulator) {
    joystick = joystickManipulator;
    manipulator = m_manipulator;


    addRequirements(manipulator);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double armJoystickMovement = joystick.getRawAxis(OperatorConstants.kArmInputAxis);
    manipulator.moveArm(armJoystickMovement);
    
    

    double joyStickValue = joystick.getRawAxis(OperatorConstants.kManipulatorClawIntake);

    if (joyStickValue <= 0) {
        manipulator.setClaw(true);
    } else if (joyStickValue > 0) {
        manipulator.setClaw(false);

    }

    manipulator.forarmUpdate();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
