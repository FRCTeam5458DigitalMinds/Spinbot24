package frc.robot.commands;


import java.util.function.BooleanSupplier;
import frc.robot.subsystems.GroundIntake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


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
        intake.toSetPoint(0);
        shooter.toSetPoint(0);
        intake.setRollers(-90);
        shooter.runFeederWheels(10);
        shooter.runFlyWheels(0);
      //  climber.toSetPoint(0);

        timer.restart();
    }

    public void execute()
    {

        isFinished();
    }
    
    public boolean isFinished()
    {
        if (timer.get() > 2)
        {
           intake.setRollers(0);
           shooter.runFeederWheels(0);

            
            return true;
        }
        return false;
    }

    
}
