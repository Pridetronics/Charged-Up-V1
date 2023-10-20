// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Manipulator;

public class RetractForarmEntirly extends CommandBase {

  private Manipulator m_Manipulator;
  private SparkMaxPIDController forearmPID = RobotContainer.forearmPID;
  private RelativeEncoder forearmEncoder;
  /** Creates a new RetractForearm. */
  public RetractForarmEntirly(Manipulator manipulator) {

    
    m_Manipulator = manipulator;
    forearmEncoder = manipulator.forearmEncoder;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
    forearmPID.setReference(0, ControlType.kPosition);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Math.abs(0-forearmEncoder.getPosition()) <= 2;
  }
}
