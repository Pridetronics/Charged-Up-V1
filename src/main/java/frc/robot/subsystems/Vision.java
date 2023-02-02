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
NetworkTableInstance inst = NetworkTableInstance.getDefault();
NetworkTable table = inst.getTable("Limelight");
  public Vision() {
   
    inst.startDSClient();
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
}
