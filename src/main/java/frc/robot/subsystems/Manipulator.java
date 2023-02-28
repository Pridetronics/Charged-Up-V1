// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.Constants.OperatorConstants;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
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
  private DifferentialDrive ManipulatorControl;
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

  private boolean forearmRetract;
  private boolean wristBottom;
  private boolean shoulderUp;
  private boolean shoulderDown;

  /** Creates a new Manipulator. */
  public Manipulator(Joystick joystickManipulator) {
    joystickManipulator = RobotContainer.joystickManipulator;
    ManipulatorControl = new DifferentialDrive(m_shoulder, m_wrist);
    ManipulatorControl.setSafetyEnabled(true);
    ManipulatorControl.setExpiration(.1);
    ManipulatorControl.setMaxOutput(1);
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
    shoulderDown = m_ShoulderLower.get();// booleans for limit switches
    shoulderUp = m_ShoulderUpper.get();
    wristBottom = m_Wristlimit.get();
    forearmRetract = m_ForearmLower.get();
  }

  public void wristCheck() {

  }

  public void forearmCheck() {

  }

  public void upperShoulderCheck() {

  }

  public void lowerShoulderCheck() {

  }

  public void zeroWrist() {
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
    forearmRetract = m_ForearmLower.get();
    if (forearmRetract == false) {
      m_forearm.set(-.3);
    } else if (forearmRetract) {
      m_forearm.set(0);
      m_ForearmEncoder.setPosition(0);
    } else {
      System.out.println("Forearm Error");
    }
  }

  public void grippersOpen() {
    m_grippers.set(DoubleSolenoid.Value.kForward);
  }

  public void grippersClose() {
    m_grippers.set(DoubleSolenoid.Value.kReverse);
  }

  public void forearmExtention() {
    m_forearm.set(.6);

  }

  public void forearmRetract() {
    forearmRetract = m_ForearmLower.get();
    m_forearm.set(-.6);
    new WaitCommand(.15);
    if (forearmRetract) {
      m_forearm.set(0);
    }
  }

  public void ManipulatorControl(Joystick joystickManipulator, double YaxisShoulder, double YaxisWrist) {
    joystickManipulator = RobotContainer.joystickManipulator;
    YaxisShoulder = joystickManipulator.getRawAxis(1);
    YaxisWrist = joystickManipulator.getRawAxis(5);

    ManipulatorControl.tankDrive(YaxisShoulder, YaxisWrist, true);

  }

}
