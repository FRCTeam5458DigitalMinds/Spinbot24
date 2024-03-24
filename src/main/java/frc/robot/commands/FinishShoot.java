package frc.robot.commands;

import frc.robot.subsystems.Climber;
import frc.robot.subsystems.GroundIntake;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;

public class FinishShoot extends Command
{
   
    Timer timer = new Timer();
    
    Climber climber;
    Shooter shooter;
    GroundIntake intake;


    //commented out bc no reasonable way to determine if we are there without doing math we would be "skipping"
    //  private double podium_degrees = 33.333;
    //  private double subwoofer_degrees = 65.662787;

    public FinishShoot(Shooter m_Shooter, GroundIntake m_Intake, Climber Elevator) 
    {
        this.shooter = m_Shooter;
        this.intake = m_Intake;
        this.climber = Elevator;

        addRequirements(m_Shooter);
        addRequirements(m_Intake);  
        addRequirements(Elevator);
    }
    public void initialize()
    {
        if (climber.getStage() != 2)
        {
            shooter.toSetPoint(0);
        }
        shooter.runFeederWheels(0);
        shooter.runFlyWheels(0);
        intake.setRollers(0);

        isFinished();
    }
    public boolean isFinished()
    {
        return true;
    }
}