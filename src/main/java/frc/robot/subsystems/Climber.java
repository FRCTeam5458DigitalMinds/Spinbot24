package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Constants.ClimbConstants;

public class Climber extends SubsystemBase {
    private double stage1 = (306.5961938 / 42);
    private double stage2 = (1642.5750 / 42);
    private double[] m_setPoints = {0, stage1, stage2};
    private int climb_ID1 = ClimbConstants.climb_ID1;
    private int climb_ID2 = ClimbConstants.climb_ID2;
    private final SparkPIDController climbController;
    private RelativeEncoder climbEncoder;
    private CANSparkMax climbMotor_1;
    private CANSparkMax climbMotor_2;
    private int current_stage = 0;

  public Climber() {
    climbMotor_1 = new CANSparkMax(climb_ID1, MotorType.kBrushless);
    climbMotor_2 = new CANSparkMax(climb_ID2, MotorType.kBrushless);

    climbEncoder = climbMotor_1.getEncoder();

    climbMotor_2.follow(climbMotor_1, true);
    climbMotor_2.burnFlash();

    climbMotor_1.setIdleMode(IdleMode.kBrake);
    climbMotor_1.burnFlash();

    climbMotor_1.setSmartCurrentLimit(30);
    climbMotor_2.setSmartCurrentLimit(30);


    climbController = climbMotor_1.getPIDController();

    climbController.setP(Constants.ClimbConstants.kP);
    climbController.setI(Constants.ClimbConstants.kI);
    climbController.setD(Constants.ClimbConstants.kD);
    climbController.setFF(Constants.ClimbConstants.FF);

    climbController.setFeedbackDevice(climbEncoder);
    climbController.setSmartMotionMaxAccel(Constants.ClimbConstants.max_accel, 0);

    climbController.setSmartMotionMinOutputVelocity(Constants.ClimbConstants.min_vel, 0);
    climbController.setSmartMotionMaxVelocity(Constants.ClimbConstants.max_vel, 0);
    climbController.setSmartMotionAllowedClosedLoopError(Constants.ClimbConstants.allowed_error, 0);
  }

  public void toSetPoint(int setPoint) 
  {

   
    /*
    if (setPoint == 1)
    {
      climbMotor_1.set(0.05);
    }
    else
    {
      climbMotor_1.set(0);
    } */
    climbController.setReference(m_setPoints[setPoint], CANSparkMax.ControlType.kSmartMotion);
    SmartDashboard.putString("DB/String 4", Double.toString(m_setPoints[setPoint]));
    SmartDashboard.putString("DB/String 6", Double.toString(climbMotor_1.get()));
    current_stage = setPoint;
  }
  public int getStage()
  {
    return current_stage;
  }
  public double getInches()
  {
    //CHANGE TO CONVERSION 
    double current_inches = climbEncoder.getPosition() * (1/16.3) * (1.0/42.0) * (1.5992 * 3.14159);
    return current_inches;
  }
  public double getEncoder()
  {
    return climbEncoder.getPosition();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
 
  /* protected void interrupted()
  {
   
  } */

}