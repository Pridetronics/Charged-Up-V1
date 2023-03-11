// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Manipulator;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.Constants.OperatorConstants;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class HomingCommand extends CommandBase {
  private Manipulator m_manipulator;
  private CANSparkMax forearmMotor;
  private SparkMaxPIDController forearmPID;
  private RelativeEncoder forearmEncoder;

  private DigitalInput forearmLimitSwitch = RobotContainer.forearmLimitSwitch;

  
  /** Creates a new HoningCommand. */
  public HomingCommand(Manipulator manipulator) {
    m_manipulator = manipulator;

    forearmMotor = RobotContainer.manipulatorForearmMotor;
    forearmEncoder = forearmMotor.getEncoder();
    forearmPID = RobotContainer.forearmPID;

    addRequirements(m_manipulator);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    m_manipulator.currentlyHoming = true;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    SmartDashboard.putNumber("ForearmPos", forearmEncoder.getPosition());
    forearmPID.setReference(-0.1, ControlType.kVelocity);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    forearmPID.setReference(0, ControlType.kVelocity);
    forearmEncoder.setPosition(0);
    m_manipulator.currentlyHoming = false;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    SmartDashboard.putBoolean("homing finished", !forearmLimitSwitch.get());
    return !forearmLimitSwitch.get();
  }
}
