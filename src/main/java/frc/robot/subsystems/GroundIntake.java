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
  private double deployPosition = -33.9;
  private double climbingPosition = -13.104;
  private double origin = -2;

  private double[] m_setPoints = {origin, deployPosition, climbingPosition};
  
  private final SparkPIDController intakeController;
  private RelativeEncoder intakeEncoder;
  private CANSparkMax intakeMotor;
  private CANSparkMax rollerMotor;


  public GroundIntake() {
    intakeMotor = new CANSparkMax(Constants.IntakeConstants.intake_ID, MotorType.kBrushless);
    rollerMotor = new CANSparkMax(Constants.IntakeConstants.roller_ID, MotorType.kBrushless);
    intakeEncoder = intakeMotor.getEncoder();
    intakeMotor.setIdleMode(IdleMode.kCoast);
    intakeMotor.burnFlash();
    rollerMotor.setIdleMode(IdleMode.kBrake);
    rollerMotor.burnFlash();

    rollerMotor.setSmartCurrentLimit(30);
    intakeMotor.setSmartCurrentLimit(30);

    intakeController = intakeMotor.getPIDController();

    intakeController.setP(Constants.IntakeConstants.kP);
    intakeController.setI(Constants.IntakeConstants.kI);
    intakeController.setD(Constants.IntakeConstants.kD);
    intakeController.setFF(Constants.IntakeConstants.FF);

    intakeController.setFeedbackDevice(intakeEncoder);
    intakeController.setSmartMotionMaxAccel(Constants.IntakeConstants.max_accel, 0);
    intakeMotor.setIdleMode(IdleMode.kBrake);
    rollerMotor.setIdleMode(IdleMode.kCoast);
   // intakeController.setSmartMotionMinOutputVelocity(Constants.IntakeConstants.min_vel, 0);
    intakeController.setSmartMotionMaxVelocity(Constants.IntakeConstants.max_vel, 0);
    intakeController.setSmartMotionAllowedClosedLoopError(Constants.IntakeConstants.allowed_error, 0);
  }

  public void toSetPoint(int setPoint) 
  {
    intakeController.setReference(m_setPoints[setPoint], CANSparkMax.ControlType.kSmartMotion);
  }

  public void setRollers(double OutputPercent)
  {
      OutputPercent /= 100.;
      rollerMotor.set(-OutputPercent);
  
  }
  /* public void check vFinished()
  {
    if (atSetPoint)
    {
      armController.
    }
  } */


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  public double getEncoder()
  {
    return intakeEncoder.getPosition();
  }
 
  public void print(String message)
  {
    SmartDashboard.putString("DB/String 2", message);
  }
  /* protected void interrupted()
  {
   
  } */

}