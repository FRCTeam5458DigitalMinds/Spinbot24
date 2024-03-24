package frc.robot.commands;

import frc.robot.subsystems.GroundIntake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj2.command.Command;

public class EndHandoff extends Command{

    GroundIntake intake;
    Shooter shooter; 
    Climber climber;

    public EndHandoff(Shooter m_Shooter, GroundIntake m_Intake, Climber m_Climber) 
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
        
        intake.setRollers(0);
        shooter.runFeederWheels(0);
        shooter.runFlyWheels(0);
      //  climber.toSetPoint(0);

        
    }

    public boolean isFinished()
    {
        return true;
    }
}
    
    