package frc.robot.commands;

import frc.robot.subsystems.GroundIntake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;


public class Handoff extends Command{

    Timer timer = new Timer();
    GroundIntake intake;
    Shooter shooter; 
    Climber climber;

    public Handoff(Shooter m_Shooter, GroundIntake m_Intake, Climber m_Climber) 
    {
        this.intake = m_Intake;
        this.shooter = m_Shooter;
        this.climber = m_Climber;

        addRequirements(m_Shooter);
        addRequirements(m_Intake);
        addRequirements(m_Climber);
    }

    public void initialize()
    {
        if (climber.getStage() != 2)
        {
            intake.setRollers(-90);
            shooter.runFeederWheels(10);
            timer.restart();       
            intake.toSetPoint(0);
            shooter.toSetPoint(0);
            shooter.runFlyWheels(0);
        }
      //  climber.toSetPoint(0);
 
        
    }

    public void execute()
    {
        if (climber.getStage() != 2)
        {
            if (timer.get() > 2)
            {
                intake.setRollers(0);
                shooter.runFeederWheels(0);

                
               
            }
        }
        else
        {
            shooter.runFlyWheels(-10);
            shooter.runFeederWheels(10);
        }
    }
}
    
    