package frc.robot.commands;

import java.util.function.BooleanSupplier;

import frc.robot.subsystems.Climber;
import frc.robot.subsystems.GroundIntake;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

//ACCOUNT FOR TIME OF FLIGHT WHEN WE GET/FINISH IMPORTANT STUFF
public class RetractIntake extends Command {
    GroundIntake intake;
    Shooter shooter;
    Climber elevator;

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
        intake.setRollers(0);
        intake.toSetPoint(0);
      //  elevator.toSetPoint(0);
        shooter.toSetPoint(0);
        shooter.runFeederWheels(0);
        shooter.runFlyWheels(0);

        SmartDashboard.putNumber("Intake Rollers", 0);
        SmartDashboard.putNumber("Intake Setpoint", 0);



       isFinished();
    }


    public boolean isFinished()
    {
        return true;
    }
}