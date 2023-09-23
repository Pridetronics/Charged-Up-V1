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
import edu.wpi.first.wpilibj.Encoder;


public class ClawIntakeCommand extends CommandBase {
  private Manipulator m_manipulator;
  private CANSparkMax forearmMotor;
  private SparkMaxPIDController forearmPID;
  private RelativeEncoder forearmEncoder;

  private DigitalInput forearmLimitSwitch = RobotContainer.forearmLimitSwitch;
  private boolean enabled = false;


  
  /** Creates a new HoningCommand. */
  public ClawIntakeCommand(Manipulator manipulator, boolean enable) {
    m_manipulator = manipulator;

    forearmMotor = RobotContainer.manipulatorForearmMotor;
    forearmPID = RobotContainer.forearmPID;
    enabled = enable;

    addRequirements(m_manipulator);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  
    m_manipulator.clawEnabled = enabled;
    

  }


  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {    

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}