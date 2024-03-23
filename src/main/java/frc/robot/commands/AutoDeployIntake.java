package frc.robot.commands;

import java.util.function.BooleanSupplier;

import frc.robot.subsystems.Climber;
import frc.robot.subsystems.GroundIntake;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import com.playingwithfusion.TimeOfFlight;
import com.playingwithfusion.TimeOfFlight.RangingMode;

//ACCOUNT FOR TIME OF FLIGHT WHEN WE GET/FINISH IMPORTANT STUFF
public class AutoDeployIntake extends Command {
    GroundIntake intake;
    Shooter shooter;
    Climber elevator;
    private final TimeOfFlight m_rangeSensor = new TimeOfFlight(0);


    public AutoDeployIntake(GroundIntake Intake, Shooter m_Shooter, Climber m_Climber) 
    {
        this.intake = Intake;
        this.shooter = m_Shooter;
        this.elevator = m_Climber;

        addRequirements(Intake);
        addRequirements(m_Shooter);
        addRequirements(m_Climber);
    }

    public void initialize() {
        //ADD OR STATEMENT FOR FLIGHT SENSOR
       // elevator.toSetPoint(0);
        shooter.runFeederWheels(0);
        shooter.runFlyWheels(0);
        shooter.toSetPoint(1);
        
        intake.setRollers(80);
        intake.toSetPoint(1);

        //m_rangeSensor.setRangingMode(RangingMode.Short, 40);    
        SmartDashboard.putNumber("Intake Rollers", 80);
        SmartDashboard.putNumber("Intake Setpoint", 1);

        SmartDashboard.putNumber("hehehe", 1);
    }

    public void execute() {
        SmartDashboard.putNumber("Intake Distance", intake.intakedistance());
        if (intake.intakedistance() / 10 < 5)
        {
            SmartDashboard.putNumber("hehehe", 0);

           new RetractIntake(intake, shooter, elevator);
           isFinished();
        }
    }

    @Override
    public boolean isFinished()
    {
        return true;
    }
}