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
import frc.robot.RobotContainesar;
import frc.robot.Constants.OperatorConstants;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class HomingCommand extends CommandBase {
  private Manipulator m_manipulator;
  private CANSparkMax forearmMotor;
  private SparkMaxPIDController forearmPID;
  private RelativeEncoder forearmEncoder;

  private DigitalInput forearmLimitSwitch = RobotContainer.forearmLimitSwitch;
  private boolean forearmEnded = false;


  
  /** Creates a new HoningCommand. */
  public HomingCommand(Manipulator manipulator) {
    m_manipulator = manipulator;

    forearmMotor = RobotContainer.manipulatorForearmMotor;
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
    if (!forearmEnded && !forearmLimitSwitch.get()) {
      endForearm();
    } else {
      forearmPID.setReference(-0.1, ControlType.kDutyCycle);
    }

  }

  private void endForearm() {
    forearmEnded = true;
    forearmEncoder = forearmMotor.getEncoder();
    forearmEncoder.setPosition(0);
    
    forearmPID.setReference(0, ControlType.kPosition);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {    

    m_manipulator.currentlyHoming = false;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    SmartDashboard.putBoolean("homing finished", forearmEnded);
    return forearmEnded;
  }
}