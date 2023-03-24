// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

//Nav-X Imports
import com.kauailabs.navx.frc.AHRS;

//Hardware imports
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

//Joystick Imports
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

//Commands
import frc.robot.commands.*; //This gives all the command imports

//Subsystems
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Manipulator;
import frc.robot.subsystems.NavX;
import frc.robot.subsystems.Vision;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.OperatorConstants;
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
  // Subsystems
  public static Drive m_drive;
  public static Vision m_vision;
  public static NavX m_navX;
  public static Manipulator m_manipulator;

  // Controllers
  public static Joystick joystickDriver;
  public static Joystick joystickManipulator;

  // Drive Motors
  public static CANSparkMax rightFrontMotor;
  public static CANSparkMax leftFrontMotor;
  public static CANSparkMax rightBackMotor;
  public static CANSparkMax leftBackMotor;

  // Manipulator Motors
  public static CANSparkMax shoulderMotor;
  public static CANSparkMax forearmMotor;
  public static CANSparkMax wristMotor;

  // Wrist Piston
  public static DoubleSolenoid wristPiston;

  // Manipulator Buttons
  public static JoystickButton forearmExtendButton;
  public static JoystickButton forearmRetractButton;

  public static JoystickButton navXButton;

  public static JoystickButton wristPistonButton;

  // Nav-X
  public static AHRS ahrs; // Attitude and Heading Reference System (motion sensor).
  public static boolean autoBalanceXMode; // Object Declaration for autoBalanceXmode. True/False output.
  public static boolean autoBalanceYMode; // Object Declaration for autoBalanceYmode. True/False output.

  // Manipulator Limit Switches
  public static DigitalInput upperShoulderLimitSwitch;
  public static DigitalInput lowerShoulderLimitSwitch;
  public static DigitalInput lowerForearmLimitSwitch;
  public static DigitalInput lowerWristLimitSwitch;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Drive Motors
    rightFrontMotor = new CANSparkMax(OperatorConstants.kRightFrontDriveCANID, MotorType.kBrushless);
    leftFrontMotor = new CANSparkMax(OperatorConstants.kLeftFrontDriveCANID, MotorType.kBrushless);
    rightBackMotor = new CANSparkMax(OperatorConstants.kRightBackDriveCANID, MotorType.kBrushless);
    leftBackMotor = new CANSparkMax(OperatorConstants.kLeftBackDriveCANID, MotorType.kBrushless);

    // Inverts the left motors and leaves the right motors
    leftFrontMotor.setInverted(true);
    leftBackMotor.setInverted(true);
    rightFrontMotor.setInverted(false);
    rightBackMotor.setInverted(false);

    // Manipulator Motors
    shoulderMotor = new CANSparkMax(OperatorConstants.kShoulderMotorCANID, MotorType.kBrushless);
    forearmMotor = new CANSparkMax(OperatorConstants.kForearmMotorCANID, MotorType.kBrushless);
    wristMotor = new CANSparkMax(OperatorConstants.kWristMotorCANID, MotorType.kBrushed);

    // Manipulator Invertions
    shoulderMotor.setInverted(false);
    forearmMotor.setInverted(false);
    wristMotor.setInverted(false);

    // Manipulator Piston
    wristPiston = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, OperatorConstants.kPistonForwardWristChannel,
        OperatorConstants.kPistonReverseWristChannel);

    // Manipulator Limit Switches
    upperShoulderLimitSwitch = new DigitalInput(OperatorConstants.kUpperShoulderLimitSwitchChannel);
    lowerShoulderLimitSwitch = new DigitalInput(OperatorConstants.kLowerShoulderLimitSwitchChannel);
    lowerForearmLimitSwitch = new DigitalInput(OperatorConstants.kLowerForearmLimitSwitchChannel);
    lowerWristLimitSwitch = new DigitalInput(OperatorConstants.kLowerWristLimitSwitchChannel);

    // Connects joystick ids to proper ports
    joystickDriver = new Joystick(OperatorConstants.kJoystickDriverID);
    joystickManipulator = new Joystick(OperatorConstants.kJoystickManipulatorID);

    m_drive = new Drive(joystickDriver);
    m_manipulator = new Manipulator(joystickManipulator);
    ahrs = new AHRS();
    m_navX = new NavX();
    m_vision = new Vision();

    // Sendable chooser
    SendableChooser<Command> m_Chooser = new SendableChooser<>();
    // Sendable chooser options
    m_Chooser.addOption("AutoForwards", new AutoMoveForward(m_drive));
    m_Chooser.addOption("auto rotate and forward",
        new SequentialCommandGroup(new InstantCommand(m_drive::calculateDistance), new AutoMoveForward(m_drive),
            new InstantCommand(m_drive::calculateDistance)));
    m_Chooser.setDefaultOption("Choose Command", new InstantCommand(m_drive::driveStop));

    // Configure the trigger bindings
    configureBindings();

    SmartDashboard.putString("Code: ", "Helen's");
    SmartDashboard.putData("BalanceMode", new AutoBalance(m_navX, m_drive));
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

    // This will do Shoulder and Wrist movements on Axis 2 (Shoulder) and Axis 5,6
    // (Wrist)
    // m_manipulator.setDefaultCommand(new ManipulatorMovement(joystickManipulator,
    // m_manipulator));

    // forearmExtendButton = new JoystickButton(joystickManipulator,
    // OperatorConstants.kForearmExtendButtonNumber);
    // forearmExtendButton.whileTrue(new ForearmExtension(m_manipulator));
    // forearmRetractButton = new JoystickButton(joystickManipulator,
    // OperatorConstants.kForearmRetractButtonNumber);
    // forearmRetractButton.whileTrue(new ForearmRetraction(m_manipulator));
    // // Extend/Retract Wrist Piston
    // wristPistonButton = new JoystickButton(joystickManipulator,
    // OperatorConstants.kWristPistonButtonNumber);
    // wristPistonButton.toggleOnTrue(new InstantCommand(m_manipulator::extendWrist,
    // m_manipulator))
    // .toggleOnFalse(new InstantCommand(m_manipulator::retractWrist,
    // m_manipulator));

    // m_navX.setDefaultCommand(new AutoBalance(m_navX, m_drive));

    // m_navX.setDefaultCommand(new AutoBalance_2(m_navX, m_drive));

    navXButton = new JoystickButton(joystickDriver, Constants.OperatorConstants.kNavXButtonNumber);
    navXButton.onTrue(new AutoBalance_2(m_navX, m_drive));
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
