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
  private CANSparkMax m_shoulderMotor;
  private CANSparkMax m_forearmMotor;
  private CANSparkMax m_wristMotor;
  private static DoubleSolenoid m_wristPiston;

  public RelativeEncoder m_shoulderEncoder;
  public RelativeEncoder m_forearmEncoder;
  public Encoder m_wristEncoder;

  private DigitalInput m_upperShoulderLimitSwitch;
  private DigitalInput m_lowerShoulderLimitSwitch;
  private DigitalInput m_lowerForearmLimitSwitch;
  private DigitalInput m_lowerWristLimitSwitch;

  private DifferentialDrive ManipulatorMovement;

  /** Creates a new Manipulator. */
  public Manipulator(Joystick m_joystickManipulator) {
    m_shoulderMotor = RobotContainer.shoulderMotor;
    m_forearmMotor = RobotContainer.forearmMotor;
    m_wristMotor = RobotContainer.wristMotor;
    m_wristPiston = RobotContainer.wristPiston;
    m_joystickManipulator = RobotContainer.joystickManipulator;
    m_shoulderEncoder = m_shoulderMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    m_forearmEncoder = m_forearmMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    m_wristEncoder = new Encoder(OperatorConstants.kWristMotorDIOID1,
        OperatorConstants.kWristMotorDIOID2, false, EncodingType.k1X);

    m_upperShoulderLimitSwitch = RobotContainer.upperShoulderLimitSwitch;
    m_lowerShoulderLimitSwitch = RobotContainer.lowerShoulderLimitSwitch;
    m_lowerForearmLimitSwitch = RobotContainer.lowerForearmLimitSwitch;
    m_lowerWristLimitSwitch = RobotContainer.lowerWristLimitSwitch;

    ManipulatorMovement = new DifferentialDrive(m_shoulderMotor, m_wristMotor);
    ManipulatorMovement.setSafetyEnabled(true);
    ManipulatorMovement.setExpiration(.1);
    ManipulatorMovement.setMaxOutput(1);
    zeroEncoders();
  }

  public void ManipulatorMovement(Joystick m_joystickManipulator, double YaxisShoulder, double YaxisWrist) {
    m_joystickManipulator = RobotContainer.joystickManipulator;
    YaxisShoulder = m_joystickManipulator.getRawAxis(OperatorConstants.kShoulderAxisNumber);
    YaxisWrist = m_joystickManipulator.getRawAxis(OperatorConstants.kWristRotationAxisNumber);
    m_shoulderMotor.set(YaxisShoulder);
    m_wristMotor.set(YaxisWrist);
    ManipulatorMovement.tankDrive(YaxisShoulder, YaxisWrist, true);
  }

  public void forearmExtension() {
    m_forearmMotor.set(.6);
  }

  public void forearmRetraction() {
    m_forearmMotor.set(-.6);
  }

  public void extendWrist() {
    m_wristPiston.set(DoubleSolenoid.Value.kForward);
  }

  public void retractWrist() {
    m_wristPiston.set(DoubleSolenoid.Value.kReverse);
  }

  public void stopShoulder() {
    m_shoulderMotor.set(0);
  }

  public void stopForearmMotor() {
    m_forearmMotor.set(0);
  }

  public void stopWristMotor() {
    m_wristMotor.set(0);
  }

  public void zeroShoulderEncoder() {
    m_shoulderEncoder.setPosition(0);
  }

  public void zeroForearmEncoder() {
    m_forearmEncoder.setPosition(0);
  }

  public void zeroWristEncoder() {
    m_wristEncoder.reset();
  }

  public void zeroEncoders() {
    m_shoulderEncoder.setPosition(0);
    m_forearmEncoder.setPosition(0);
    m_wristEncoder.reset();
  }

  public boolean isShoulderAtTop() {
    boolean isShoulderAtTop;
    if (m_upperShoulderLimitSwitch.get() == true) {
      isShoulderAtTop = true;
    } else {
      isShoulderAtTop = false;
    }
    return isShoulderAtTop;
  }

  public boolean isShoulderAtBottom() {
    boolean isShoulderAtBottom;
    if (m_lowerForearmLimitSwitch.get() == true) {
      isShoulderAtBottom = true;
    } else {
      isShoulderAtBottom = false;
    }
    return isShoulderAtBottom;

  }

  // Might have to switch true/false due to magnetic limit switch
  public boolean isForearmAtBottom() {
    boolean isForearmAtBottom;
    if (m_lowerForearmLimitSwitch.get() == false) {
      isForearmAtBottom = true;
    } else {
      isForearmAtBottom = false;
    }
    return isForearmAtBottom;
  }

  public boolean isForearmAtTop() {
    boolean isForearmAtTop;
    m_forearmEncoder.getPosition();
    if (m_forearmEncoder.getPosition() >= OperatorConstants.MANIPULATOR_MAx_OUTPUT) {
      isForearmAtTop = true;
    } else {
      isForearmAtTop = false;
    }
    return isForearmAtTop;
  }

  public boolean isWristAtBottom() {
    boolean isWristAtBottom;
    if (m_lowerWristLimitSwitch.get() == true) {
      isWristAtBottom = true;
    } else {
      isWristAtBottom = false;
    }
    return isWristAtBottom;
  }

  public boolean isWristAtTop() {
    boolean isWristAtTop;
    m_wristEncoder.getDistance();
    if (m_wristEncoder.getDistance() >= OperatorConstants.MANIPULATOR_MAx_OUTPUT) {
      isWristAtTop = true;
    } else {
      isWristAtTop = false;
    }
    return isWristAtTop;
  }

  public void ShoulderUpSlowly() {
    if (isShoulderAtBottom() == true) {
      System.out.println("Shoulder Rotation Up");
      m_shoulderMotor.set(0.1);
    } else {
      stopShoulder();
      zeroShoulderEncoder();
    }
  }

  public void ShoulderDownSLowly() {
    if (isShoulderAtBottom() == false) {
      System.out.println("Shoulder Rotation Down");
      m_shoulderMotor.set(-0.1);
    } else {
      stopShoulder();
      zeroShoulderEncoder();
    }
  }

  public void ForearmUpSlowly() {
    if (isForearmAtBottom() == true) {
      System.out.println("Forearm Extension");
      m_shoulderMotor.set(0.1);
    } else {
      stopForearmMotor();
      zeroForearmEncoder();
    }
  }

  public void ForearmDownSlowly() {
    if (isForearmAtBottom() == false) {
      System.out.println("Forearm Retraction");
      m_shoulderMotor.set(-0.1);
    } else {
      stopForearmMotor();
      zeroForearmEncoder();
    }
  }

  public void WristUpSlowly() {
    if (isWristAtBottom() == true) {
      System.out.println("Wrist Rotation Up");
      m_shoulderMotor.set(0.1);
    } else {
      stopWristMotor();
      zeroWristEncoder();
    }
  }

  public void WristDownSlowly() {
    if (isWristAtBottom() == false) {
      System.out.println("Wrist Rotation Down");
      m_shoulderMotor.set(-0.1);
    } else {
      stopWristMotor();
      zeroWristEncoder();
    }

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Shoulder Encoder", m_shoulderEncoder.getPosition());
    SmartDashboard.putNumber("Forearm Encoder", m_forearmEncoder.getPosition());
    SmartDashboard.putNumber("Wrist Encoder", m_wristEncoder.getDistance());
    SmartDashboard.putBoolean("Lower Shoulder Limit Switch", RobotContainer.lowerShoulderLimitSwitch.get());
    SmartDashboard.putBoolean("Upper Shoulder Limit Switch", RobotContainer.upperShoulderLimitSwitch.get());
    SmartDashboard.putBoolean("Lower Forearm Limit Switch", RobotContainer.lowerForearmLimitSwitch.get());
    SmartDashboard.putBoolean("Lower Wrist Limit Switch", RobotContainer.lowerWristLimitSwitch.get());
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
