// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
//import edu.wpi.first.wpilibj.drive;
//hardware
import edu.wpi.first.wpilibj.Joystick;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder;
public class Drive extends SubsystemBase {
  //motors
  private CANSparkMax m_rightFrontMotor;
  private CANSparkMax m_leftFrontMotor;
  private CANSparkMax m_rightBackMotor;
  private CANSparkMax m_leftBackMotor;
  //encoders
  public static RelativeEncoder m_rightFrontEncoder;
  public static RelativeEncoder m_leftFrontEncoder;
  public static RelativeEncoder m_rightBackEncoder;
  public static RelativeEncoder m_leftBackEncoder;
  //makes differential drive and motorcontroller groups
  public DifferentialDrive tankDrive;
  public MotorControllerGroup Left;
  public MotorControllerGroup Right;
  /** Creates a new Drive. */
    public Drive( Joystick joystickDriver) {
    //motors are now set to same as robotcontainer
    m_rightFrontMotor = RobotContainer.rightFrontMotor;
    m_leftFrontMotor = RobotContainer.leftFrontMotor;
    m_rightBackMotor = RobotContainer.rightBackMotor;
    m_leftBackMotor = RobotContainer.leftBackMotor;
    //Detects and sets encoder values
    m_rightFrontEncoder = m_rightFrontMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    m_leftFrontEncoder = m_leftFrontMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    m_rightBackEncoder = m_rightBackMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    m_leftBackEncoder = m_leftBackMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    
    //zeroes encoders
    zeroEncoders();
      //two motorcontroller groups that will act as left and right in tank drive 
      Left = new MotorControllerGroup(m_leftFrontMotor, m_leftBackMotor);
      Right = new MotorControllerGroup(m_rightFrontMotor, m_rightBackMotor);
      tankDrive = new DifferentialDrive(Left, Right);
        
        tankDrive.setSafetyEnabled(true);
        tankDrive.setExpiration(.1);
        tankDrive.setMaxOutput(1);
      



  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    
  }
  public void zeroEncoders(){
    m_rightFrontEncoder.setPosition(0);
    m_leftFrontEncoder.setPosition(0);
    m_rightBackEncoder.setPosition(0);
    m_leftBackEncoder.setPosition(0);
  }
  public void Tankdrive(Joystick joystickDriver,double Yval1, double Yval2){
    Yval1 = joystickDriver.getRawAxis(1);
    Yval2 = joystickDriver.getRawAxis(5);
      tankDrive.tankDrive(Yval1, Yval2);

  }


}
