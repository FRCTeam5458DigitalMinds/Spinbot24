package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.GroundIntake;
import frc.robot.subsystems.Shooter;

public class ZeroShooter extends Command
{
    Shooter shoot;
    GroundIntake take;

    public ZeroShooter(Shooter shooter, GroundIntake intake) 
    {
        this.shoot = shooter;
        this.take = intake;
        addRequirements(shooter);
        addRequirements(intake);
    }

    public void initialize()
    {
        take.setZero();
        shoot.setZero();

        isFinished();
    }

    public boolean isFinished()
    {
        return true;
    }

}
