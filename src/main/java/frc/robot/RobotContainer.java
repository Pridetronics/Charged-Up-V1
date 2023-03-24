// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

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
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.SparkMaxRelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

//Commands
import frc.robot.commands.*;

//Subsystems
import frc.robot.subsystems.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.OperatorConstants;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * 
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // subsystems
  public static Drive m_drive;
  public static Vision m_vision;
  public static Manipulator m_manipulator;
  public static NavX m_navX;
  // controllers
  public static Joystick joystickDriver;
  public static Joystick joystickManipulator;

  // Drive Motors
  public static CANSparkMax rightFrontMotor;
  public static CANSparkMax leftFrontMotor;
  public static CANSparkMax rightBackMotor;
  public static CANSparkMax leftBackMotor;

  // manipulator Motors
  public static CANSparkMax manipulatorArmMotor;
  public static CANSparkMax manipulatorForearmMotor;
  public static CANSparkMax manipulatorClawMotor;
  // Solenoids

  // Nav-X
  public static AHRS ahrs; // Attitude and Heading Reference System (motion sensor).
  public static boolean autoBalanceXMode; // Object Declaration for autoBalanceXmode. True/False output.
  public static boolean autoBalanceYMode; // Object Declaration for autoBalanceYmode. True/False output.

  // PID Controllers
  public static SparkMaxPIDController shoulderPID;
  public static SparkMaxPIDController forearmPID;
  public static PIDController wristPID;

  public static DigitalInput forearmLimitSwitch = new DigitalInput(OperatorConstants.kForearmLimitID);
  public static DigitalInput wristLimitSwitch = new DigitalInput(OperatorConstants.kWristLimitID);

  public static JoystickButton navXButton;

  public static Encoder wristEncoder;
  public static RelativeEncoder armEncoder;

  // Manipulator Buttons
  public static JoystickButton forearmExtendButton;
  public static JoystickButton forearmRetractButton;
  public static JoystickButton wristPistonButton;

  // driver Buttons
  public static JoystickButton targetCenteringButton;
  public static JoystickButton toggleBrakeButton;

  // sendablechooser
  public static SendableChooser<Command> m_Chooser;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Drive Motors
    rightFrontMotor = new CANSparkMax(OperatorConstants.kRightFrontDriveCANID, MotorType.kBrushless);
    leftFrontMotor = new CANSparkMax(OperatorConstants.kLeftFrontDriveCANID, MotorType.kBrushless);
    rightBackMotor = new CANSparkMax(OperatorConstants.kRightBackDriveCANID, MotorType.kBrushless);
    leftBackMotor = new CANSparkMax(OperatorConstants.kLeftBackDriveCANID, MotorType.kBrushless);
    // Manipulator Motors
    manipulatorArmMotor = new CANSparkMax(OperatorConstants.kArmMotorCANID, MotorType.kBrushless);
    manipulatorForearmMotor = new CANSparkMax(OperatorConstants.kForearmMotorCANID, MotorType.kBrushless);
    manipulatorClawMotor = new CANSparkMax(OperatorConstants.kWristMotorCANID, MotorType.kBrushed);
    // inverts the left motors and leaves the right motors
    leftFrontMotor.setInverted(true);
    leftBackMotor.setInverted(true);
    rightFrontMotor.setInverted(false);
    rightBackMotor.setInverted(false);

    // Connects joystick ids to proper ports
    joystickDriver = new Joystick(OperatorConstants.kJoystickDriverID);
    joystickManipulator = new Joystick(OperatorConstants.kJoystickManipulatorID);
    // drive
    m_drive = new Drive(joystickDriver);

    wristEncoder = new Encoder(OperatorConstants.kWristEncoderAID, OperatorConstants.kWristEncoderBID);
    armEncoder = manipulatorArmMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);

    // PID Controllers
    shoulderPID = manipulatorArmMotor.getPIDController();
    shoulderPID.setP(0.45);
    shoulderPID.setI(0);
    shoulderPID.setD(0);
    forearmPID = manipulatorForearmMotor.getPIDController();
    forearmPID.setP(0.1);
    forearmPID.setI(0);
    forearmPID.setD(0);
    wristPID = new PIDController(0.485, 0, 0);

    toggleBrakeButton = new JoystickButton(joystickDriver,
        OperatorConstants.kToggleBrake);
    m_drive = new Drive(joystickDriver);
    m_manipulator = new Manipulator();

    m_navX = new NavX();
    ahrs = new AHRS();
    m_vision = new Vision();
    m_Chooser = new SendableChooser<Command>();
    // sendable chooser options
    m_Chooser.setDefaultOption("Choose Command",
        new SequentialCommandGroup(new InstantCommand(m_drive::driveStop),
            new HomingCommand(m_manipulator)));
    m_Chooser.setDefaultOption("score",
        new SequentialCommandGroup(new InstantCommand(m_drive::driveStop),
            new HomingCommand(m_manipulator),
            new InstantCommand(m_manipulator::shoulderUpsies)));
    m_Chooser.addOption("Auto Backwards",
        new SequentialCommandGroup(new HomingCommand(m_manipulator),
            new InstantCommand(m_manipulator::shoulderUpsies),
            new WaitCommand(1),
            new AutoMoveBack(m_drive)));
    // m_Chooser.addOption("take targets",
    // new SequentialCommandGroup(new HomingCommand(m_manipulator),
    // new AutoMoveBackwards(m_drive),
    // new AutoRotateLeft(m_drive), new AutoMoveForward(m_drive)));
    m_Chooser.addOption("AutoForwards",
        new SequentialCommandGroup(new HomingCommand(m_manipulator),
            new AutoMoveForward(m_drive)));
    // m_Chooser.addOption("turn around",
    // new SequentialCommandGroup(new HomingCommand(m_manipulator),
    // new AutoForwardTarget(m_drive),
    // new AutoTurnAround(m_drive), new AutoMoveForward(m_drive)));
    m_Chooser.addOption("Community and Station",
        new SequentialCommandGroup(new HomingCommand(m_manipulator),
            new InstantCommand(m_manipulator::shoulderUpsies), new WaitCommand(1),
            new AutoMoveBackwards(m_drive), new AutoForwardTarget(m_drive)));

    m_Chooser.addOption("AutoBalance-NavX",
        new SequentialCommandGroup(new HomingCommand(m_manipulator),
            new InstantCommand(m_manipulator::shoulderUpsies), new WaitCommand(1),
            new AutoMoveBackwards(m_drive), new AutoForwardTarget(m_drive),
            new AutoBalance(m_navX, m_drive)));
    // Configure the trigger bindings
    configureBindings();
    SmartDashboard.putData("Chooser", m_Chooser);
    SmartDashboard.putData("BalanceMode", new SecondAutoBalance(m_navX, m_drive));
    SmartDashboard.putBoolean("AutoBalanceXMode", autoBalanceXMode);
    SmartDashboard.putBoolean("AutoBalanceYMode", autoBalanceYMode);
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be
   * created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
   * an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link
   * CommandXboxController
   * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or
   * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    m_drive.setDefaultCommand(new JoystickDrive(joystickDriver, m_drive));
    m_manipulator.setDefaultCommand(new ManipulatorInput(joystickManipulator, m_manipulator));

    targetCenteringButton = new JoystickButton(joystickDriver,
        OperatorConstants.kAimCentering);
    targetCenteringButton.toggleOnTrue(new TargetCenteringVision(m_vision));

    toggleBrakeButton.onTrue(new InstantCommand(m_drive::IdleCheck));

    forearmExtendButton = new JoystickButton(joystickManipulator,
        OperatorConstants.kManipulatorInputRetract);
    forearmRetractButton = new JoystickButton(joystickManipulator,
        OperatorConstants.kManipulatorInputExtend);

    forearmExtendButton.onTrue(new forearmInput(m_manipulator, true));
    forearmRetractButton.onTrue(new forearmInput(m_manipulator, false));

    JoystickButton homingButton = new JoystickButton(joystickManipulator,
        OperatorConstants.kManipulatorHomingInput);
    homingButton.onTrue(new HomingCommand(m_manipulator));

    JoystickButton clawToggleButton = new JoystickButton(joystickManipulator,
        OperatorConstants.kClawToggle);
    clawToggleButton.onTrue(new ClawIntakeCommand(m_manipulator, true));
    clawToggleButton.onFalse(new ClawIntakeCommand(m_manipulator, false));

    // m_navX.setDefaultCommand(new AutoBalance(m_navX, m_drive));

    navXButton = new JoystickButton(joystickDriver, Constants.OperatorConstants.kNavXButtonNumber);
    navXButton.onTrue(new SecondAutoBalance(m_navX, m_drive));

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return m_Chooser.getSelected();
  }
  // m_navX.setDefaultCommand(new AutoBalance_2(m_navX, m_drive));
}
