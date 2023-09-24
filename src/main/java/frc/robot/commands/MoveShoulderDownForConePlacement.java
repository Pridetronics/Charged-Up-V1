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

public class MoveShoulderDownForConePlacement extends CommandBase {

  public Manipulator m_Manipulator;
  public SparkMaxPIDController shoulderPID = RobotContainer.shoulderPID;
  public RelativeEncoder armEncoder = RobotContainer.armEncoder;

  public double shoulderPosition;
  /** Creates a new MoveShoulderDownForConePlacement. */
  public MoveShoulderDownForConePlacement(Manipulator manipulator) {
    m_Manipulator = manipulator;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    shoulderPosition = armEncoder.getPosition();
    //shoulderPID.setReference(shoulderPosition+5, ControlType.kPosition);
  }

  
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_Manipulator.moveArm(0.45);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_Manipulator.moveArm(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return armEncoder.getPosition() >= shoulderPosition+5;
  }
}
