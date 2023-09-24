// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.Manipulator;

public class ExtendForearm extends CommandBase {

  private Manipulator m_manipulator;
  private SparkMaxPIDController forearmPID;
  private RelativeEncoder forearmEncoder;
  private CANSparkMax forearmMotor;

  /** Creates a new ExtendForearm. */
  public ExtendForearm(Manipulator manipulator) {
    m_manipulator = manipulator;

    
    forearmMotor = RobotContainer.manipulatorForearmMotor;
    forearmPID = RobotContainer.forearmPID;
    forearmEncoder = forearmMotor.getEncoder();

    addRequirements(m_manipulator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double distanceToSubtractFromExtentionLimit = 0;
    forearmPID.setReference(OperatorConstants.forearmExtendLimit-distanceToSubtractFromExtentionLimit, ControlType.kPosition);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    double distanceToSubtractFromExtentionLimit = 1;
    SmartDashboard.putNumber("Forearm Current Pos", forearmEncoder.getPosition());
    SmartDashboard.putNumber("Forearm Target Pos", OperatorConstants.forearmExtendLimit-distanceToSubtractFromExtentionLimit);
    return forearmEncoder.getPosition() >= OperatorConstants.forearmExtendLimit-distanceToSubtractFromExtentionLimit;
  }
}
