package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;

public class ZeroShooter extends Command
{
    Shooter shoot;
    public ZeroShooter(Shooter shooter) 
    {
        this.shoot = shooter;
        addRequirements(shooter);
    }

    public void initialize()
    {
        shoot.setZero();
        isFinished();
    }

    public boolean isFinished()
    {
        return true;
    }

}
