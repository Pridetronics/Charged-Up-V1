// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.RobotContainer;
//camera imports
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CameraServerJNI;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoMode.PixelFormat;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.MjpegServer;
//networktable imports to organize limelight and camera
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
//hardware imports for automatic actions
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

public class Vision extends SubsystemBase {
 //software
 public MjpegServer Server1;
 
 public CvSource outputStream;
  //Hardware
  UsbCamera camera_0;
  //motors
  private CANSparkMax m_rightFrontMotor;
  private CANSparkMax m_leftFrontMotor;
  private CANSparkMax m_rightBackMotor;
  private CANSparkMax m_leftBackMotor;
//Encoders
  private static RelativeEncoder m_rightFrontEncoder;
  private static RelativeEncoder m_leftFrontEncoder;
  private static RelativeEncoder m_rightBackEncoder;
  private static RelativeEncoder m_leftBackEncoder;
 //variables
 private double ty;
 private double tx;
 private double tv;
 private double ta;
//converted variables
 private double heightTotal;
 private double angleTotal;
 private double angleTan;
 private double initialDistance;
 private double distanceInInches;
 private double distanceInFeet;
 private double roundedDistance;
NetworkTableInstance inst = NetworkTableInstance.getDefault();
NetworkTable table = inst.getTable("Limelight");
  public Vision() {
    //motors
    m_leftFrontMotor  = RobotContainer.leftFrontMotor;
m_leftFrontEncoder = m_leftFrontMotor.getEncoder();
    m_leftBackMotor = RobotContainer.leftBackMotor;
m_leftBackEncoder = m_leftBackMotor.getEncoder();
    m_rightFrontMotor = RobotContainer.rightFrontMotor;
m_rightFrontEncoder = m_rightFrontMotor.getEncoder();
    m_rightBackMotor = RobotContainer.rightBackMotor;
m_rightBackEncoder = m_rightBackMotor.getEncoder();
  //pov camera
    camera_0 = new UsbCamera("POV", 0);
    Server1 = new MjpegServer("Serve_POV_Camera", 0);
    Server1.setSource(camera_0);
    CameraServer.addServer(Server1);
    Server1.setFPS(15);
    Server1.setCompression(13);
    CameraServer.putVideo("Serve_POV_Camera", 360, 360);
    //starts new  DS client, Very important for lime 
    inst.startDSClient();
    

    //network tables 
    NetworkTableEntry yEntry = table.getEntry("ty");
    NetworkTableEntry xEntry = table.getEntry("tx");
    NetworkTableEntry aEntry = table.getEntry("ta");
    NetworkTableEntry vEntry = table.getEntry("tv");
  
    ta = aEntry.getDouble(0.0); // Target Area (0% of image to 100% of image)
    ty = yEntry.getDouble(0.0); // Vertical Offset From Crosshair To Target (-20.5 degrees to 20.5 degrees)
    tx = xEntry.getDouble(0.0); // Horizontal Offset From Crosshair to Target (-27.5 degrees to 27.5 degrees)
    tv = vEntry.getDouble(0.0); // Whether the limelight has any valid targets (0 or 1)

    
  }

  @Override
  public void periodic() {
   
    SmartDashboard.putNumber("Limelight Area", ta); // Displays base limelight values to Shuffleboard
    SmartDashboard.putNumber("Limelight X", tx);
    SmartDashboard.putNumber("Limelight Y", ty);
    SmartDashboard.putNumber("Limelight V", tv);
   
    // This method will be called once per scheduler run
  }
  public void findDistance() {
    // Equation for distance is d = (h1 - h2)/(tan(a1 + a2)) //Defaults to
    // meters/radian
    heightTotal = 104 - 25.75; // Input inches
    angleTotal = 0.628 + ty; // Input radians
    angleTan = Math.tan(angleTotal);

    initialDistance = Math.abs(heightTotal / angleTan); // Returns negative, so there's a negative on the next line
    distanceInInches = initialDistance * 13.6; // Converts distance into inches.
    distanceInFeet = distanceInInches / 12; // Converts distance in inches to feet
    roundedDistance = Math.round(distanceInFeet); // Rounds distance in feet

    SmartDashboard.putNumber("Initial Distance", initialDistance); // Puts all these values on the smartdashboard when
                                                                   // run. This is solely for testing purposes
    SmartDashboard.putNumber("Distance in Inches", distanceInInches);
    SmartDashboard.putNumber("Distance in Feet", distanceInFeet);
    SmartDashboard.putNumber("Rounded Distance", roundedDistance);
  }
  public void lightsOut() {
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
    System.out.println("Lights off");
  }
  public void DriveMotorsStop() {
    m_leftFrontMotor.set(0);
    m_rightFrontMotor.set(0);
    m_leftBackMotor.set(0);
    m_rightBackMotor.set(0);
    System.out.println("Stopped");
  }








}
