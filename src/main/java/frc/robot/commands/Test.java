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


public class Test extends Command
{
    Shooter shooter;
    int SETPOINT;
    //commented out bc no reasonable way to determine if we are there without doing math we would be "skipping"
    //  private double podium_degrees = 33.333;
    //  private double subwoofer_degrees = 65.662787;

    public Test(Shooter m_Shooter, int setpoint) 
    {
        this.shooter = m_Shooter;
        this.SETPOINT = setpoint;
        addRequirements(m_Shooter);
    }
    
    public void initialize()
    {  
        if (SETPOINT == 0)
        {
            shooter.toCustomSetpoint(45);
        }
        else
        {        
            shooter.runFlyWheels(95);
        }

    
        isFinished();
    }


    public boolean isFinished()
    {

        return true;
    }
}