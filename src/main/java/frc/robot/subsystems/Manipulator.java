// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.OperatorConstants;

//hardware
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Manipulator extends SubsystemBase {
  private DigitalInput m_ShoulderUpper = new DigitalInput(OperatorConstants.kShoulderUpperLimit);
  private DigitalInput m_ShoulderLower = new DigitalInput(OperatorConstants.kShoulderLowerLimit);
  private DigitalInput m_ForearmLower = new DigitalInput(OperatorConstants.kForearmLowerLimit);
  private DigitalInput m_Wristlimit = new DigitalInput(OperatorConstants.kWristLimit);
  private CANSparkMax m_johnson = new CANSparkMax(OperatorConstants.kWristMotor, MotorType.kBrushed);
  private Encoder m_johnsonEncoder = new Encoder(12, 2, false, EncodingType.k1X);
  private DoubleSolenoid m_grippers = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, OperatorConstants.kExtendPist,
      OperatorConstants.kRetractPist);
  private CANSparkMax m_shoulder = new CANSparkMax(OperatorConstants.kShoulderMotorCANID, MotorType.kBrushless);
  private CANSparkMax m_telescopic = new CANSparkMax(OperatorConstants.kTelescopicMotorCANID, MotorType.kBrushless);

  /** Creates a new Manipulator. */
  public Manipulator(Joystick joystickManipulator) {
    joystickManipulator = RobotContainer.joystickManipulator;

    // Controls the wrist motor, rotating forwards and backwards

  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("JohnsonMotor Encoder", m_johnsonEncoder.getDistance());
    // This method will be called once per scheduler run
  }

}
