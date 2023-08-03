package frc.robot.subsystems;

import com.revrobotics.AnalogInput;
import com.revrobotics.CANAnalog;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxAnalogSensor;

import edu.wpi.first.math.controller.PIDController;

public class SwerveModule {
    private final CANSparkMax driveMotor;
    private final CANSparkMax turningMotor;

    private final RelativeEncoder drivEncoder;
    private final RelativeEncoder turningEncoder;

    private final PIDController  turnignPIDController;

    private SparkMaxAnalogSensor absoluteEncoder;
    private final boolean absoluteEncoderReversed;
    private final double absoluteEncoderOffsetRad;

    public SwerveModule(int driveMotorId, int turningMotorId, boolean driveMotorReversed, 
    boolean turningMotorReversed, int absoluteEncoderId, double abolsoluteEncoderOffset, boolean absoluteEncoderReversed) {

        this.absoluteEncoderOffsetRad = abolsoluteEncoderOffset;
        this.absoluteEncoderReversed = absoluteEncoderReversed;
        absoluteEncoder = turningMotor.getAnalog(SparkMaxAnalogSensor.Mode.kAbsolute);


    }
}
