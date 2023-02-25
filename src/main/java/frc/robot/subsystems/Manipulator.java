// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.OperatorConstants;

//hardware
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
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
  // limit switches
  private DigitalInput m_ShoulderUpper = new DigitalInput(OperatorConstants.kShoulderUpperLimit);
  private DigitalInput m_ShoulderLower = new DigitalInput(OperatorConstants.kShoulderLowerLimit);
  private DigitalInput m_ForearmLower = new DigitalInput(OperatorConstants.kForearmLowerLimit);
  private DigitalInput m_Wristlimit = new DigitalInput(OperatorConstants.kWristLimit);
  // double solenoid
  private DoubleSolenoid m_grippers = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, OperatorConstants.kExtendPist,
      OperatorConstants.kRetractPist);

  // motors
  private static CANSparkMax m_wrist = new CANSparkMax(OperatorConstants.kWristMotor, MotorType.kBrushed);
  private static CANSparkMax m_shoulder = new CANSparkMax(OperatorConstants.kShoulderMotorCANID, MotorType.kBrushless);
  private static CANSparkMax m_forearm = new CANSparkMax(OperatorConstants.kTelescopicMotorCANID,
      MotorType.kBrushless);
  // encoders
  private RelativeEncoder m_ShoulderEncoder = m_shoulder.getEncoder();
  private RelativeEncoder m_ForearmEncoder = m_forearm.getEncoder();
  private Encoder m_wristEncoder = new Encoder(6, 7, false, EncodingType.k1X);

  /** Creates a new Manipulator. */
  public Manipulator(Joystick joystickManipulator) {
    joystickManipulator = RobotContainer.joystickManipulator;

  }

  @Override
  public void periodic() {
    // all smartdashboard code here is to be commented out in the production version
    SmartDashboard.putNumber("JohnsonMotor Encoder", m_wristEncoder.getDistance());
    SmartDashboard.putNumber("Shoulder Encoder", m_ShoulderEncoder.getPosition());
    SmartDashboard.putNumber("Forearm Encoder", m_ForearmEncoder.getPosition());
    SmartDashboard.putBoolean("upper shoulder lim", m_ShoulderUpper.get());
    SmartDashboard.putBoolean("lower shoulder lim", m_ForearmLower.get());
    SmartDashboard.putBoolean("forearm lim", m_ForearmLower.get());
    SmartDashboard.putBoolean("wrist lim", m_Wristlimit.get());
    // This method will be called once per scheduler run
  }

  public void zeroJohnson() {
    m_wristEncoder.reset();
  }

  public void zeroForearm() {
    m_ForearmEncoder.setPosition(0);
  }

  public void zeroShoulder() {
    m_ShoulderEncoder.setPosition(0);
  }

  public void zeroAll() {
    m_ForearmEncoder.setPosition(0);
    m_ShoulderEncoder.setPosition(0);
    m_wristEncoder.reset();
  }

  public void honeForearm() {

  }

  public void grippersOpen() {
    m_grippers.set(DoubleSolenoid.Value.kForward);
  }

  public void grippersClose() {
    m_grippers.set(DoubleSolenoid.Value.kReverse);
  }

  public void forearmMaxExtention() {

  }

  public void ManipulatorIn(Joystick joystickManipulator, double Yaxis, double YaxisTop, boolean Trigger) {
    joystickManipulator = RobotContainer.joystickManipulator;
    Yaxis = joystickManipulator.getRawAxis(2);
    YaxisTop = joystickManipulator.getRawAxis(5);// hat of joystick
    Trigger = joystickManipulator.getRawButton(OperatorConstants.Gripper);
    m_shoulder.setVoltage(Yaxis);
    m_wrist.setVoltage(YaxisTop);

    if (Trigger) {
      m_grippers.set(DoubleSolenoid.Value.kForward);
    } else {
      m_grippers.set(DoubleSolenoid.Value.kReverse);
    }
  }
}
