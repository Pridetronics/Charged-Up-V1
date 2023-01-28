// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.RobotContainer;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CameraServerJNI;
import edu.wpi.first.cscore.UsbCamera;
//networktable imports to organize limelight and camera
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
//hardware imports for automatic actions
import com.revrobotics.CANSparkMax;

public class Vision extends SubsystemBase {
 UsbCamera camera_0;

 
 private double ty;
 private double tx;
 private double tv;
 private double ta;

 private double heightTotal;
 private double angleTotal;
 private double angleTan;
//  private double initialDistance;
//  private double distanceInInches;
//  private double distanceInFeet;
//  private double roundedDistance;
 
  public Vision() {
    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    NetworkTable table = inst.getTable("Limelight");
    inst.startDSClient();
    //normal camera
    // UsbCamera camera_0 = new UsbCamera("POV Camera", 0);
    // CameraServer.startAutomaticCapture(camera_0);
    // camera_0.setFPS(30);
    // camera_0.setResolution(120, 120);
    
 
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
