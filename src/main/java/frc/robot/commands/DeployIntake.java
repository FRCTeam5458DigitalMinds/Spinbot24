package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import frc.robot.subsystems.Climber;
import frc.robot.subsystems.GroundIntake;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;



//ACCOUNT FOR TIME OF FLIGHT WHEN WE GET/FINISH IMPORTANT STUFF
public class DeployIntake extends Command {

    GroundIntake intake;
    Shooter shooter;
    Climber elevator;
    Double intake_val;



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
        intake.toSetPoint(1);

        SmartDashboard.putString("INTAKE STATE", "INITIALIZING BEEP BEEP");
        intake.setRollers(80);
        
        shooter.runFeederWheels(0);
        shooter.runFlyWheels(0);
        shooter.toSetPoint(1);
        
        

    }

    public void execute() {
       // SmartDashboard.putString("INTAKE STATE", "EXECUTING BEEP BEEP");
        if (intake.voltageOutput() >= 35)
        {
            SmartDashboard.putBoolean("RUMBLE RUMBLE", true);
            intake.setRollers(0);
            intake.toSetPoint(0);
            shooter.runFeederWheels(0);
            shooter.runFlyWheels(0);

            isFinished();
        }
        else
        {
            shooter.toSetPoint(1);
        }
    }

    @Override
    public boolean isFinished()
    {
        SmartDashboard.putBoolean("RUMBLE RUMBLE", false);

        if (intake.getPos() > -2.5)
        {
            //shooter.toSetPoint(0);
            return true;
        }

        return false;
    }
}