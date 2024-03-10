package frc.robot.commands;

import java.util.function.BooleanSupplier;

import javax.lang.model.util.ElementScanner14;

import frc.robot.subsystems.Climber;
import frc.robot.subsystems.GroundIntake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.Limelight;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;
import java.util.HashMap;

public class AutoFinish extends Command
{
   
    Timer timer = new Timer();
    
    Climber climber;
    Shooter shooter;
    GroundIntake intake;


    //commented out bc no reasonable way to determine if we are there without doing math we would be "skipping"
    //  private double podium_degrees = 33.333;
    //  private double subwoofer_degrees = 65.662787;

    public AutoFinish(Shooter m_Shooter, GroundIntake m_Intake, Climber Elevator) 
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
        intake.setRollers(0);

        isFinished();
    }
    public boolean isFinished()
    {
        return true;
    }
}