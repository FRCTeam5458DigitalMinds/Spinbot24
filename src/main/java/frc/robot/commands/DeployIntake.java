package frc.robot.commands;

import frc.robot.subsystems.Climber;
import frc.robot.subsystems.GroundIntake;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;



//ACCOUNT FOR TIME OF FLIGHT WHEN WE GET/FINISH IMPORTANT STUFF
public class DeployIntake extends Command {

    GroundIntake intake;
    Shooter shooter;
    Climber elevator;
    Double intake_val;
    boolean finished;


    public DeployIntake(GroundIntake Intake, Shooter m_Shooter, Climber m_Climber) 
    {
        this.intake = Intake;
        this.shooter = m_Shooter;
        this.elevator = m_Climber;
        
        addRequirements(Intake);
        addRequirements(m_Shooter);
        addRequirements(m_Climber);
    }

    public void initialize() 
    {
        finished = false;
        intake.toSetPoint(1);
        intake.setRollers(80);
        
        shooter.runFeederWheels(0);
        shooter.runFlyWheels(0);
        shooter.toSetPoint(1);
        
        SmartDashboard.putString("deploy state", "init");

        SmartDashboard.putNumber("voltage output", intake.voltageOutput());
        SmartDashboard.putBoolean("deploy done", false);

    }

    public void execute() 
    {
       // SmartDashboard.putString("INTAKE STATE", "EXECUTING BEEP BEEP");

        if (intake.voltageOutput() >= 37)
        {
            shooter.toSetPoint(1);

            if (intake.getPos() < -10) 
            {

                SmartDashboard.putBoolean("deploy done", true);
                intake.setRollers(0);
                intake.toSetPoint(0);
                shooter.runFeederWheels(0);
                shooter.runFlyWheels(0);

            }
            if(SmartDashboard.getBoolean("deploy done", false) == true)
            {
                isFinished();
            }
        }
    }

    public boolean isFinished()
    {
        if (SmartDashboard.getBoolean("deploy done", false) == true && intake.getPos() > -6)
        {
            shooter.toSetPoint(0);
            return true;
        }
        return false;
    }
}