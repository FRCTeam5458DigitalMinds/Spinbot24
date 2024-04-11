package frc.robot.commands;

import frc.robot.subsystems.Climber;
import frc.robot.subsystems.GroundIntake;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class RetractIntake extends Command {
    GroundIntake intake;
    Shooter shooter;
    Climber elevator;
    Timer timer = new Timer();

    public RetractIntake(GroundIntake Intake, Shooter m_Shooter, Climber Elevator) 
    {
        this.intake = Intake;
        this.shooter = m_Shooter;
        this.elevator = Elevator;

        addRequirements(Intake);
        addRequirements(Elevator);
        addRequirements(m_Shooter);
    }

    public void initialize() {
        //ADD OR STATEMENT FOR FLIGHT SENSOR
        intake.toSetPoint(0);
        intake.setRollers(65);
      //  elevator.toSetPoint(0)
        shooter.runFeederWheels(0);
        shooter.runFlyWheels(0);

        timer.restart();
    }
    public void execute()
    {
        isFinished();
    }


    public boolean isFinished()
    {
        SmartDashboard.putNumber("timer", timer.get());
        SmartDashboard.putNumber("intake V", intake.getPos());


        if (intake.getPos() > -8)
        {
            shooter.toSetPoint(0);
            intake.setRollers(0);

            return true;
        }

        return false;
    }
}