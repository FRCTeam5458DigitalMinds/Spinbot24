package frc.robot.commands;

import frc.robot.subsystems.GroundIntake;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;

public class AutoShoot extends Command
{
   //declaring local subsystems/variables
    Timer timer = new Timer();
    
    Shooter shooter;
    GroundIntake intake;
    int setpoint;

    public AutoShoot(Shooter m_Shooter, GroundIntake m_Intake, int SETPOINT) 
    {
        //assign the local subsystem variables the global ones, NECESSARY
        this.shooter = m_Shooter;
        this.intake = m_Intake;
        this.setpoint = SETPOINT;

        //must add requirements for every subsystem or code breaks with no error lol
        addRequirements(m_Shooter);
        addRequirements(m_Intake);

    }

    public void initialize()
    {            

        //sets the subsystems to go to position for the shoot
        //dont run the feeder wheels until shooter gets to correct degree
        intake.toSetPoint(0);
        shooter.runFlyWheels(-95);
        shooter.toAutoSetpoint(setpoint);
        shooter.runFeederWheels(0);
        intake.setRollers(0);

        timer.restart();
    }
    
    public void execute() 
    {
        //repeatedly checks to see if can feed note and end command
        isFinished();
    }

    public boolean isFinished()
    {
        //dont run the feeder wheels until shooter gets to correct degree
        //timer to make sure shooter has time to take off first
        if (timer.get() > 0.1) {
            //shooter.getV() will be zero when shooter slows down to reach setpoint
            if (shooter.getV() == 0)
            {
                //run feeders as shooter reaches it degree
                shooter.runFeederWheels(85);
                intake.setRollers(-50);
                
                //finish command after we shoot note
                return true;
            }
        }
        //if shooter not at 0 v or has yet to take off we return false
        return false;
    }
}