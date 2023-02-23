// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
//hardware
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.DoubleSolenoid;
public class Manipulator extends SubsystemBase {
  /** Creates a new Manipulator. */
  public Manipulator() {
    //Controls the wrist motor, rotating forwards and backwards
    if (joystickDriver.getRawAxis(5)){
      clawWristMotor.set(0.5)
    }
    else if (joystickDriver.getRawAxis(6)){
      clawWristMotor.set(-0.5)
    }
    else
    {
      clawWrist.set(0);
    }
    //Claw Solenoid When Pressing Triggers
    if (joystickDriver.getRawButton(1)){
      clawSolenoid.toggle();
    }
  }
  

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
