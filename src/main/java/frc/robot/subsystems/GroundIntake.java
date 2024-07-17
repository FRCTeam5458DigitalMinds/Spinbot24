package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

public class GroundIntake extends SubsystemBase {
  //PID setpoints of ideal encoder values
  private double deployPosition = -36;
  private double climbingPosition = -13.104;
  private double origin = -0.2;
  private double ejectPosition = -27;

  private double[] m_setPoints = {origin, deployPosition, climbingPosition, ejectPosition};
  
  //declaring intake motors/encoders
  private final SparkPIDController intakeController;
  private RelativeEncoder intakeEncoder;
  
  private CANSparkMax intakeMotor;
  private CANSparkMax rollerMotor;

  public GroundIntake() {

    //motor definitions
    intakeMotor = new CANSparkMax(Constants.IntakeConstants.intake_ID, MotorType.kBrushless);
    rollerMotor = new CANSparkMax(Constants.IntakeConstants.roller_ID, MotorType.kBrushless);

    //delcaring & resetting intake slap down encoder
    intakeEncoder = intakeMotor.getEncoder();

    //configuring the motors
    intakeMotor.restoreFactoryDefaults();
    rollerMotor.restoreFactoryDefaults();
    intakeMotor.setIdleMode(IdleMode.kBrake);
    intakeMotor.burnFlash();
    rollerMotor.setIdleMode(IdleMode.kCoast);
    rollerMotor.burnFlash();

    //setting motor current limits
    rollerMotor.setSmartCurrentLimit(35);
    intakeMotor.setSmartCurrentLimit(30);

    //intake slap down PID declaration and initial config of values
    intakeController = intakeMotor.getPIDController();

    intakeController.setP(Constants.IntakeConstants.kP);
    intakeController.setI(Constants.IntakeConstants.kI);
    intakeController.setD(Constants.IntakeConstants.kD);
    intakeController.setFF(Constants.IntakeConstants.FF);

    intakeController.setFeedbackDevice(intakeEncoder);
    intakeController.setSmartMotionMaxAccel(Constants.IntakeConstants.max_accel, 0);
    intakeController.setSmartMotionMaxVelocity(Constants.IntakeConstants.max_vel, 0);
    intakeController.setSmartMotionAllowedClosedLoopError(Constants.IntakeConstants.allowed_error, 0);

  }

  //moves intake slapdown to a certain encoder value from preset array
  //setPoint passed in as index
  public void toSetPoint(int setPoint) 
  {
    SmartDashboard.putNumber("Intake setpoint", setPoint);
    intakeController.setReference(m_setPoints[setPoint], CANSparkMax.ControlType.kSmartMotion);
  }

  //sets the speed of the intake rollers
  public void setRollers(double OutputPercent)
  {
      OutputPercent /= 100.;
      rollerMotor.set(-OutputPercent);
  }

  //returns the output current of the intake roller motor
  public double voltageOutput()
  {
    SmartDashboard.putNumber("roller output current", rollerMotor.getOutputCurrent());
    
    return rollerMotor.getOutputCurrent();
  }

  //returns the intake slap down motor encoder position
  public double getPos()
  {
    return intakeEncoder.getPosition();
  }
  public void setZero()
  {
    intakeEncoder.setPosition(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

}