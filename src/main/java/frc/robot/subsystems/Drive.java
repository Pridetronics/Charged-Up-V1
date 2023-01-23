// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

//hardware
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
  //makes differential drive and drivetrains
  public DifferentialDrive WCDrive;
  
  /** Creates a new Drive. */
    public Drive() {
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
    
    //zeroes encoders when set
    zeroEncoders();
      
 
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


}
