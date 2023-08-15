package frc.robot.subsystems;

import com.revrobotics.AnalogInput;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxAnalogSensor;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.AnalogInput;

public class SwerveModule {
    private final CANSparkMax driveMotor;
    private final CANSparkMax turningMotor;

    private final RelativeEncoder drivEncoder;
    private final RelativeEncoder turningEncoder;

    private final PIDController  turnignPIDController;

    private final SparkMaxAnalogSensor absoluteEncoder;
    private final boolean absoluteEncoderReversed;
    private final double absoluteEncoderOffsetRad;

    public SwerveModule(int driveMotorId, int turningMotorId, boolean driveMotorReversed, 
    boolean turningMotorReversed, int absoluteEncoderId, double abolsoluteEncoderOffset, boolean absoluteEncoderReversed) {

        driveMotor = new CANSparkMax(absoluteEncoderId, MotorType.kBrushless);
        turningMotor = new CANSparkMax(turningMotorId, MotorType.kBrushless);

        driveMotor.setInverted(driveMotorReversed);
        turningMotor.setInverted(turningMotorReversed);

        this.absoluteEncoderOffsetRad = abolsoluteEncoderOffset;
        this.absoluteEncoderReversed = absoluteEncoderReversed;
        absoluteEncoder = turningMotor.getAnalog(SparkMaxAnalogSensor.Mode.kAbsolute);
        //absoluteEncoder = new AnalogInput(absoluteEncoderId);

        drivEncoder = driveMotor.getEncoder();
        turningEncoder = turningMotor.getEncoder();

        drivEncoder.set
    }
}
