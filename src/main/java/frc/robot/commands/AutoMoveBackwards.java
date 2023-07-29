package frc.robot.commands;

import frc.robot.subsystems.Drive;

import frc.robot.Constants;
import frc.robot.RobotContainer;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoMoveBackwards extends CommandBase {
  private Drive m_drive;

  /** Creates a new AutoMoveForward. */
  public AutoMoveBackwards(Drive drive) {
    m_drive = drive;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    RobotContainer.leftBackMotor.setIdleMode(IdleMode.kBrake);
    RobotContainer.leftFrontMotor.setIdleMode(IdleMode.kBrake);
    RobotContainer.rightBackMotor.setIdleMode(IdleMode.kBrake);
    RobotContainer.rightFrontMotor.setIdleMode(IdleMode.kBrake);
    m_drive.zeroEncoders();
    SmartDashboard.putNumber("Backwards end pos", Drive.m_leftBackEncoder.getPosition());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    m_drive.driveBack();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    SmartDashboard.putNumber("backwards started ending", Drive.m_leftBackEncoder.getPosition());
    m_drive.driveStop();
    RobotContainer.leftBackMotor.setIdleMode(IdleMode.kBrake);
    RobotContainer.leftFrontMotor.setIdleMode(IdleMode.kBrake);
    RobotContainer.rightBackMotor.setIdleMode(IdleMode.kBrake);
    RobotContainer.rightFrontMotor.setIdleMode(IdleMode.kBrake);

    SmartDashboard.putNumber("Backwards end pos", Drive.m_leftBackEncoder.getPosition());
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (Math.abs(Drive.m_leftBackEncoder.getPosition()) >= Constants.OperatorConstants.longDistance) {
      return true;
    } else {
      return false;
    }
  }
}
