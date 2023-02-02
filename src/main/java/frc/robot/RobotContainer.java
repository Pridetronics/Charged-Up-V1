// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import edu.wpi.first.wpilibj.Joystick;
//subsystems
import frc.robot.subsystems.Drive;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Manipulator;


//Hardware imports
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

//commands
import frc.robot.commands.Autos;

import frc.robot.commands.JoystickDrive;
//Driver station stuff
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//to be removed
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
 //subsystems
 public static Drive m_drive;
  //controllers
  public static Joystick joystickDriver;
  public static Joystick joystickShooter;
  //motors 
  public static CANSparkMax rightFrontMotor;
  public static CANSparkMax leftFrontMotor;
  public static CANSparkMax rightBackMotor;
  public static CANSparkMax leftBackMotor;
  

  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
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
    joystickShooter = new Joystick(OperatorConstants.kJoystickShooterID);
    //drive
    m_drive = new Drive(joystickDriver);
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    
    
    
    
    
    
      // Configure the trigger bindings
    configureBindings();
    
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
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
   // new Trigger(m_exampleSubsystem::exampleCondition)
    //    .onTrue(new ExampleCommand(m_exampleSubsystem));

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
   // m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autos.exampleAuto(m_exampleSubsystem);
  }
}
