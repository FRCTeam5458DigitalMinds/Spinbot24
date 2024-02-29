package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.Slot1Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.VoltageConfigs;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends SubsystemBase{
    private TalonFX shooterMotor = new TalonFX(Constants.ShooterConstants.Shooter_ID);
    private final MotionMagicVoltage M_MMREQ = new MotionMagicVoltage(0);

    private TalonFX flyWheelOne = new TalonFX(Constants.ShooterConstants.FlyWheelOne_ID);
    private TalonFX flyWheelTwo = new TalonFX(Constants.ShooterConstants.FlyWheelTwo_ID);
    private TalonFX feederWheel = new TalonFX(Constants.ShooterConstants.FeederWheel_ID);

    private double intakeHandoff = 18;
    private double climbingPosition = 39.2;
    private double ampPosition = 50;
    private double m_ff;

    private double[] m_setPoints = {0, intakeHandoff, climbingPosition, ampPosition};

  /** Creates a new ExampleSubsyste m. */
    public Shooter() {
      TalonFXConfiguration cfg = new TalonFXConfiguration();
      MotionMagicConfigs mm = cfg.MotionMagic;

      TalonFXConfiguration fig = new TalonFXConfiguration();
      CurrentLimitsConfigs currentconfig = fig.CurrentLimits;
      CurrentLimitsConfigs curconfig = cfg.CurrentLimits;

      curconfig.StatorCurrentLimit = 40;
      curconfig.StatorCurrentLimitEnable = true;

      currentconfig.StatorCurrentLimit = 40;
      currentconfig.StatorCurrentLimitEnable = true;
 
      mm.MotionMagicCruiseVelocity = 150;
      mm.MotionMagicAcceleration = 115;
      mm.MotionMagicJerk = 0;
      

      shooterMotor.setPosition(0);
      flyWheelOne.setInverted(true);
      flyWheelTwo.setControl(new Follower(13, false));
    
     // feederWheel.getConfigurator().apply(new VoltageConfigs());

     // var talonFXConfigs = new TalonFXConfiguration();

      Slot0Configs slot0Configs = cfg.Slot0;
      Slot1Configs slot1Configs = cfg.Slot1;

      //slot0Configs.kV = Constants.ShooterConstants.kV;
      slot0Configs.kP = 45;
      slot0Configs.kI = 10.0;
      slot0Configs.kD = 0.1;
      slot0Configs.kS = 0.3;
      slot0Configs.kV = 0.1;
      m_ff = 1;

      slot1Configs.kP = 4.8;
      slot1Configs.kI = 0;
      slot1Configs.kD = 0.1;
      slot1Configs.kS = 0.25;
      slot1Configs.kV = 0.12;

      FeedbackConfigs fdb = cfg.Feedback;
      shooterMotor.getConfigurator().apply(cfg);
      flyWheelOne.getConfigurator().apply(cfg);
      flyWheelTwo.getConfigurator().apply(cfg);
      feederWheel.getConfigurator().apply(cfg);

      shooterMotor.getConfigurator().apply(slot0Configs, 0.20);
  
      //shooterMotor.getConfigurator().apply(new CurrentLimitsConfigs().withSupplyCurrentLimit(30), 0.02);
      //flyWheelOne.getConfigurator().apply(new CurrentLimitsConfigs().withSupplyCurrentLimit(30), 0.02);
      //flyWheelTwo.getConfigurator().apply(new CurrentLimitsConfigs().withSupplyCurrentLimit(30), 0.02);
      //feederWheel.getConfigurator().apply(new CurrentLimitsConfigs().withSupplyCurrentLimit(30), 0.02);


      SmartDashboard.putNumber("shooter P", slot0Configs.kP);
      SmartDashboard.putNumber("shooter I", slot0Configs.kI);
      SmartDashboard.putNumber("shooter D", slot0Configs.kD);
      SmartDashboard.putNumber("shooter V", slot0Configs.kV);
      }

    public void toSetPoint(int setPoint) 
    {
      SmartDashboard.putNumber("shooter setpoint", setPoint);

      var motorPosSignal = shooterMotor.getRotorPosition();
      var motorPos = motorPosSignal.getValue();

   //   shooterMotor.get
      //inal MotionMagicExpoVoltage m_PIDRequest = new MotionMagicExpoVoltage(0).withSlot(0);
      if (setPoint == 0) 
      {

        shooterMotor.setControl(M_MMREQ.withPosition(m_setPoints[setPoint]).withSlot(1));
      }
      else
      {
        shooterMotor.setControl(M_MMREQ.withPosition(m_setPoints[setPoint]).withSlot(1));
      }

      SmartDashboard.putNumber("supposed setpoint", m_setPoints[setPoint]);
      SmartDashboard.putNumber("supposed output", shooterMotor.get());
      SmartDashboard.putNumber("supposed error", shooterMotor.getClosedLoopError().getValueAsDouble());

  
    }
    public void manualControl()
    {
      shooterMotor.set(0.05);
      SmartDashboard.putNumber("supposed output", shooterMotor.get());

    }
    public void stopControl()
    {
      shooterMotor.set(0);
      SmartDashboard.putNumber("supposed output", shooterMotor.get());
    }

    /*public boolean atSetPoint(int SETPOINT)
    {
      return shooterMotor.get
    }*/
    public void toCustomSetpoint(double degrees)
    {
      SmartDashboard.putNumber("shooter degree setpoint", degrees);

      double toTicks = degreesToRotations(degrees);
      SmartDashboard.putString("DB/String 2", Double.toString(degrees));

     // final MotionMagicExpoVoltage m_PIDRequest = new MotionMagicExpoVoltage(0);
      shooterMotor.setControl(M_MMREQ.withPosition(toTicks).withSlot(1));
    }

    public double degreesToRotations(double degrees)
    {
      return (degrees / 360. * 218.75);
    }
    
    public double getEncoder()
    {
      return shooterMotor.getPosition().getValueAsDouble();
    }
    public void runFlyWheels(double OutputPercent)
    {

      SmartDashboard.putNumber("fly wheels", OutputPercent);

      OutputPercent /= 100.;
      SmartDashboard.putString("DB/String 9", Double.toString(OutputPercent));
      flyWheelOne.set(OutputPercent);
    }

    public void runFeederWheels(double OutputPercent)
    {
      SmartDashboard.putNumber("feeder wheels", OutputPercent);

      OutputPercent /= 100.;
      SmartDashboard.putString("DB/String 0", Double.toString(OutputPercent));
      feederWheel.set(OutputPercent);
    }  
    public double getV()
    {
      return shooterMotor.getVelocity().getValueAsDouble();
    }
    /**
     * An example method querying a boolean state of the subsystem (for example, a digital sensor).
     *
     * @return value of some boolean subsystem state, such as a digital sensor.
     */
    @Override
    public void periodic() 
    {
    // This method will be called once per scheduler run
    }

    @Override
    public void simulationPeriodic() 
    {
      // This method will be called once per scheduler run during simulation
    }

    public void print(String message)
    {
      SmartDashboard.putString("DB/String 3", message);
    }
}

