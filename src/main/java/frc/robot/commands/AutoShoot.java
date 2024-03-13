package frc.robot.commands;

import java.util.function.BooleanSupplier;

import javax.lang.model.util.ElementScanner14;

import frc.robot.subsystems.GroundIntake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;
import java.util.HashMap;

public class AutoShoot extends Command
{
   
    Timer timer = new Timer();
    
    Shooter shooter;
    GroundIntake intake;
    int setpoint;
    //commented out bc no reasonable way to determine if we are there without doing math we would be "skipping"
    //  private double podium_degrees = 33.333;
    //  private double subwoofer_degrees = 65.662787;

    public AutoShoot(Shooter m_Shooter, GroundIntake m_Intake, int SETPOINT) 
    {
        this.shooter = m_Shooter;
        this.intake = m_Intake;
        this.setpoint = SETPOINT;
        addRequirements(m_Shooter);
        addRequirements(m_Intake);

    }
    public void initialize()
    {            

        intake.toSetPoint(0);
        shooter.runFlyWheels(-95);
        shooter.toAutoSetpoint(setpoint);
        shooter.runFeederWheels(0);
        intake.setRollers(0);

        timer.restart();
        SmartDashboard.putString("DB/String 1", "open shoot");
    }
    
    public void execute() 
    {
        
        SmartDashboard.putNumber("time", timer.get());
        isFinished();
        
    }

    public boolean isFinished()
    {
        if (timer.get() > 0.1) {
            if (shooter.getV() == 0)
            {

                shooter.runFeederWheels(85);
                intake.setRollers(-50);
                
                return true;
            }
        }
        return false;
    }
}