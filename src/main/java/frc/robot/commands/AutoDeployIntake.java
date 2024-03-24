package frc.robot.commands;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.GroundIntake;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.Command;

//use for auto as deploy intake relies on conditions that cannot be 100% in auto
public class AutoDeployIntake extends Command {
    //declaring local subsystem variables
    GroundIntake intake;
    Shooter shooter;
    Climber elevator;

    public AutoDeployIntake(GroundIntake Intake, Shooter m_Shooter, Climber m_Climber) 
    {
        //assign the local subsystem variables the global ones, NECESSARY
        this.intake = Intake;
        this.shooter = m_Shooter;
        this.elevator = m_Climber;

        //must add requirements for every subsystem or code breaks with no error lol
        addRequirements(Intake);
        addRequirements(m_Shooter);
        addRequirements(m_Climber);
    }

    public void initialize() {

        //deploys the intake and runs/stops the complementary motors
        shooter.runFeederWheels(0);
        shooter.runFlyWheels(0);
        shooter.toSetPoint(1);
        
        intake.setRollers(80);
        intake.toSetPoint(1);

        
        isFinished();
    }

    @Override

    //nomenclature essential to actually ending the command
    public boolean isFinished()
    {
        return true;
    }
}