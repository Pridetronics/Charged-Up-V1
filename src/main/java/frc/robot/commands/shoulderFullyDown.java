// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Manipulator;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class shoulderFullyDown extends InstantCommand {

  private SparkMaxPIDController shoulderPID;


  public shoulderFullyDown(Manipulator m_Manipulator) {
    // Use addRequirements() here to declare subsystem dependencies.
    shoulderPID = RobotContainer.shoulderPID;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    shoulderPID.setReference(0, ControlType.kPosition);
  }
}
