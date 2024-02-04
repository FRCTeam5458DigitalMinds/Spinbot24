package frc.robot.commands;

import java.util.function.BooleanSupplier;
import frc.robot.subsystems.GroundIntake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climber;


public class MoveClimber extends Command {

    GroundIntake intake;
    Shooter shooter;
    Climber elevator; 
    int setPoint;


    public MoveClimber(GroundIntake Intake, Shooter m_Shooter, Climber m_Climber, int setPoint) 
    {
        this.intake = Intake;
        this.shooter = m_Shooter;
        this.elevator = m_Climber;
        this.setPoint = setPoint; 

        addRequirements(Intake);
        addRequirements(m_Shooter);
        addRequirements(m_Climber);
    }
    public void initialize()
    {

        if (setPoint == 0) 
        //down
        {
            shooter.toSetPoint(0);
        } 
        else if (setPoint == 1)
        //amp score
        {   
            shooter.toSetPoint(3);
        }
        //climbing
        else if (setPoint == 2) {
            shooter.toSetPoint(2);
            intake.toSetPoint(2);
        }

        intake.setRollers(0);
        shooter.runFeederWheels(0);
        shooter.runFlyWheels(0);
        elevator.toSetPoint(setPoint);
        isFinished();
        
    }
    public boolean isFinished()
    {
        return true;
    }
}