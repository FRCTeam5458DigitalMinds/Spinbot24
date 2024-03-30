package frc.robot.commands;

import frc.robot.subsystems.GroundIntake;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;

public class OpenShoot extends Command
{
   
    Timer timer = new Timer();
    
    Shooter shooter;
    GroundIntake intake;
    
    //commented out bc no reasonable way to determine if we are there without doing math we would be "skipping"
    //  private double podium_degrees = 33.333;
    //  private double subwoofer_degrees = 65.662787;

    public OpenShoot(Shooter m_Shooter, GroundIntake m_Intake) 
    {
        this.shooter = m_Shooter;
        this.intake = m_Intake;

        addRequirements(m_Shooter);
        addRequirements(m_Intake);

    }
    public void initialize()
    {            

        intake.toSetPoint(0);
        shooter.runFlyWheels(-95);
        shooter.toSetPoint(0);
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
        SmartDashboard.putString("DB/String 2", Double.toString(timer.get()));

        if (timer.get() > 0.3)
        {
        intake.setRollers(-50);
        shooter.runFeederWheels(85);

        return true;
        }
        return false;
    }
}