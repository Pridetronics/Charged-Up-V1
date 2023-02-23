package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LimitSwitchesConstants;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LimitSwitches extends SubsystemBase {
  /**
   * Creates a new LimitSwitches.
   */
  private static final DigitalInput m_UpperLimitSwitch = new DigitalInput(
      LimitSwitchesConstants.kUpperLimitSwitchChannel);
  private static final DigitalInput m_LowerLimitSwitch = new DigitalInput(
      LimitSwitchesConstants.kLowerLimitSwitchChannel);

  public LimitSwitches() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("Upper Limit Switch", m_UpperLimitSwitch.get());
    SmartDashboard.putBoolean("Lower Limit Switch", m_LowerLimitSwitch.get());

  }
}
