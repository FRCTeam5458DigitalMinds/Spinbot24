
package frc.robot.commands;

import frc.robot.subsystems.GroundIntake;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;

public class Eject extends Command
{
    Timer timer = new Timer();
    GroundIntake intake;
    int mode;

    public Eject(GroundIntake m_Intake, int MODE) 
    {
        this.intake = m_Intake;

        addRequirements(m_Intake);
        this.mode = MODE;
    }

    public void initialize()
    {      
        SmartDashboard.putString("DB/String 7", Double.toString(mode));
        if (mode == 0)
        {     
            intake.toSetPoint(3);

        }
        else
        {
            intake.setRollers(0);
            intake.toSetPoint(0);
        }
        timer.restart();
    }
    public void execute()
    {
        isFinished();
    }

    @Override
    public boolean isFinished() 
    {
        if (timer.get() > 1)
        {
            if (mode == 0)
            {
                intake.setRollers(-80);
            }
            return true;
        }
        if (mode == 0)
        {
            return false;
        }
        else 
        {
           return true;
        }
    }
}    
