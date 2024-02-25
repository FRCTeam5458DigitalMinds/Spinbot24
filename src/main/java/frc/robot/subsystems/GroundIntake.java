package frc.robot.subsystems;

import com.playingwithfusion.TimeOfFlight;
import com.playingwithfusion.TimeOfFlight.RangingMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

public class GroundIntake extends SubsystemBase {
  private double deployPosition = -36.2;
  private double climbingPosition = -13.104;
  private double origin = -1.75;
  private double ejectPosition = -27;
  private double intakedistance;

  private double[] m_setPoints = {origin, deployPosition, climbingPosition, ejectPosition};
  
  private final SparkPIDController intakeController;
  private RelativeEncoder intakeEncoder;
  
  private CANSparkMax intakeMotor;
  private CANSparkMax rollerMotor;
  private final TimeOfFlight m_rangeSensor = new TimeOfFlight(0);



  public GroundIntake() {
    intakeMotor = new CANSparkMax(Constants.IntakeConstants.intake_ID, MotorType.kBrushless);
    rollerMotor = new CANSparkMax(Constants.IntakeConstants.roller_ID, MotorType.kBrushless);
    intakeEncoder = intakeMotor.getEncoder();
    intakeMotor.restoreFactoryDefaults();
    rollerMotor.restoreFactoryDefaults();
    intakeMotor.setIdleMode(IdleMode.kCoast);
    intakeMotor.burnFlash();
    rollerMotor.setIdleMode(IdleMode.kBrake);
    rollerMotor.burnFlash();

    rollerMotor.setSmartCurrentLimit(35);
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
    //intakeController.set
    m_rangeSensor.setRangingMode(RangingMode.Short, 40);
  }

  public void toSetPoint(int setPoint) 
  {
    SmartDashboard.putNumber("Intake setpoint", setPoint);
    intakeController.setReference(m_setPoints[setPoint], CANSparkMax.ControlType.kSmartMotion);
    //intakeController.set
  }

  public void setRollers(double OutputPercent)
  {
        SmartDashboard.putNumber("Intake roller", OutputPercent);

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

  public double intakedistance()
  {
    
    intakedistance = (int)m_rangeSensor.getRange();

    SmartDashboard.putNumber("space, time (of flight)", intakedistance);
    return intakedistance;
  }
  /* protected void interrupted()
  {
   
  } */

}