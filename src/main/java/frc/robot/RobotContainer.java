// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

//Joystick Imports
import edu.wpi.first.wpilibj.Joystick;
//subsystems
import frc.robot.subsystems.Drive;
import frc.robot.Constants.OperatorConstants;

import frc.robot.subsystems.Manipulator;
import frc.robot.subsystems.Vision;

//Nav-X Imports
import com.kauailabs.navx.frc.AHRS;

//Hardware imports
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.commands.AutoBalance;
//Commands
import frc.robot.commands.Autos;
import frc.robot.commands.JoystickDrive;

//Subsystems
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.NavX;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.OperatorConstants;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}

 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
 //subsystems
 public static Drive m_drive;
 public static Vision m_vision;
  //controllers
  public static Joystick joystickDriver;
  public static Joystick joystickManipulator;
  //motors 
  public static NavX m_navX;
  public static CANSparkMax rightFrontMotor;
  public static CANSparkMax leftFrontMotor;
  public static CANSparkMax rightBackMotor;
  public static CANSparkMax leftBackMotor;

  //Nav-X
  public static AHRS ahrs; //Attitude and Heading Reference System (motion sensor).
  public static boolean autoBalanceXMode; //Object Declaration for autoBalanceXmode. True/False output.
  public static boolean autoBalanceYMode; //Object Declaration for autoBalanceYmode. True/False output.

 // private final Drive m_drive = new Drive();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    //motors
    rightFrontMotor = new CANSparkMax(OperatorConstants.kRightFrontDriveCANID, MotorType.kBrushless);
    leftFrontMotor = new CANSparkMax(OperatorConstants.kLeftFrontDriveCANID, MotorType.kBrushless);
    rightBackMotor = new CANSparkMax(OperatorConstants.kRightBackDriveCANID, MotorType.kBrushless);
    leftBackMotor = new CANSparkMax(OperatorConstants.kLeftBackDriveCANID, MotorType.kBrushless);
    //inverts the left motors and leaves the right motors 
    leftFrontMotor.setInverted(true);
    leftBackMotor.setInverted(true);
    rightFrontMotor.setInverted(false);
    rightBackMotor.setInverted(false);    
    //connects joystick ids to proper ports
    joystickDriver = new Joystick(OperatorConstants.kJoystickDriverID);
    joystickManipulator = new Joystick(OperatorConstants.kJoystickManipulatorID);
    //drive
    m_drive = new Drive(joystickDriver);
    
    m_navX = new NavX();
    ahrs = new AHRS();

    // Configure the trigger bindings
    m_vision = new Vision();
    //sendable chooser
    SendableChooser<Command> m_Chooser = new SendableChooser<>();
    
      // Configure the trigger bindings
    configureBindings();

    SmartDashboard.putString("Code: ", "Helen's");
    SmartDashboard.putData("BalanceMode", new AutoBalance(m_navX));
    SmartDashboard.putBoolean("AutoBalanceXMode", autoBalanceXMode);
    SmartDashboard.putBoolean("AutoBalanceYMode", autoBalanceYMode);
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    m_drive.setDefaultCommand(new JoystickDrive(joystickDriver, m_drive));
    
    m_navX.setDefaultCommand(new AutoBalance(m_navX));
    // if (joystickDriver.getRawButtonPressed(0)) {
    //   new AutoBalance(m_navX);
    // }
    // if (joystickDriver.getRawButton(1)) {
    //   new AutoBalance(m_navX);
    // } //Not needed as of 2/2/2023 for now
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autos.exampleAuto();
  }
}
