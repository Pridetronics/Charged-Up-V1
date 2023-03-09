// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.RobotContainer;

//Camera imports
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CameraServerJNI;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoMode.PixelFormat;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.MjpegServer;

//Networktable imports to organize limelight and camera
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

//Hardware imports for automatic actions
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

public class Vision extends SubsystemBase {
  // software
  public MjpegServer Server1;

  public CvSource outputStream;
  // Hardware
  UsbCamera camera_0;

  // Motors
  private CANSparkMax m_rightFrontMotor;
  private CANSparkMax m_leftFrontMotor;
  private CANSparkMax m_rightBackMotor;
  private CANSparkMax m_leftBackMotor;

  // Encoders
  private static RelativeEncoder m_rightFrontEncoder;
  private static RelativeEncoder m_leftFrontEncoder;
  private static RelativeEncoder m_rightBackEncoder;
  private static RelativeEncoder m_leftBackEncoder;
  // motor groups
  private MotorControllerGroup Left;
  private MotorControllerGroup Right;
  // Variables
  private double ty;
  private double tx;
  private double tv;
  private double ta;
  // converted variables
  private double heightTotal;
  private double angleTotal;
  private double angleTan;
  private double initialDistance;
  private double distanceInInches;
  private double distanceInFeet;
  private double roundedDistance;
  // Confirmation variables
  public double TargetisCentered;
  NetworkTableInstance inst = NetworkTableInstance.getDefault();
  NetworkTable table = inst.getTable("limelight");

  public Vision() {
    // motors
    m_leftFrontMotor = RobotContainer.leftFrontMotor;
    m_leftFrontEncoder = m_leftFrontMotor.getEncoder();
    m_leftBackMotor = RobotContainer.leftBackMotor;
    m_leftBackEncoder = m_leftBackMotor.getEncoder();
    m_rightFrontMotor = RobotContainer.rightFrontMotor;
    m_rightFrontEncoder = m_rightFrontMotor.getEncoder();
    m_rightBackMotor = RobotContainer.rightBackMotor;
    m_rightBackEncoder = m_rightBackMotor.getEncoder();
    // pov camera
    camera_0 = CameraServer.startAutomaticCapture(0);
    NetworkTableInstance.getDefault().getTable("USB Camera 0").getEntry("mode")
        .setString("340x240 MJPEG 15 fps");
    // CameraServer.putVideo("Serve_POV", 320, 240);// makes the cameraserver
    // retrieve the Server1 name and sets
    // resolution
    System.out.println("See All");
    inst.startClient3("3853");
    // starts new DS client, Very important for lime
    inst.startDSClient();
    inst.getEntry("stream").setDouble(0);
    // network tables

    SmartDashboard.putNumber("Limelight Area", ta); // Displays base limelight values to Shuffleboard
  }

  @Override
  public void periodic() {
    NetworkTableEntry yEntry = table.getEntry("ty");
    NetworkTableEntry xEntry = table.getEntry("tx");
    NetworkTableEntry aEntry = table.getEntry("ta");
    NetworkTableEntry vEntry = table.getEntry("tv");

    ta = aEntry.getDouble(0.0); // Target Area (0% of image to 100% of image)
    ty = yEntry.getDouble(0.0); // Vertical Offset From Crosshair To Target (-20.5 degrees to 20.5 degrees)
    tx = xEntry.getDouble(0.0); // Horizontal Offset From Crosshair to Target (-27.5 degrees to 27.5 degrees)
    tv = vEntry.getDouble(0.0);
    SmartDashboard.putNumber("Limelight X", tx);// will be commented out after testing
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
    SmartDashboard.putNumber("Distance in Feet", distanceInFeet);// all to be commented out after testing
    SmartDashboard.putNumber("Rounded Distance", roundedDistance);
  }

  public void lightsOut() {
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
    System.out.println("Lights off");
  }

  public void DriveMotorsStop() {
    Left.set(0);
    Right.set(0);
    System.out.println("Stopped");
  }

  public void driveBack() {
    Left.set(-.6);
    Right.set(-.6);
    System.out.println("Backwards");
  }

  public void driveForward() {
    Left.set(.6);
    Right.set(.6);
    System.out.println("Forwards");
  }

  public void setPipe1() {// changing pipelines dor apriltags and normal limelight
    inst.getEntry("pipeline").setDouble(0);
  }

  public void setPipe2() {// ignore nt index button on limelight interface is to be disabled for this to
                          // work
    inst.getEntry("pipeline").setDouble(1);
  }

  public void centerTarget() {
    if (tv == 1) {// greater than and less than values need to be adjusted based on position of
                  // limelight on robot
      if (tx > 8 && tx < 30) {// (adjust when not centered), currently based on a centered limelight, adjusts
                              // to the right
        // original values are >8 and <30
        Left.set(50);
        Right.set(-50);
        TargetisCentered = 0;
      } else if (tx > -8 && tx < -30) {// adjust to the left
        Left.set(-50);
        Right.set(50);
        TargetisCentered = 0;
      } else {
        Left.set(0);
        Right.set(0);
        TargetisCentered = 1;
      }
    } else if (tv == 0) {
      Left.set(0);
      Right.set(0);
      System.out.println("Target not Found");
    }

  }

}
