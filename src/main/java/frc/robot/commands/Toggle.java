package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;

//changing the closed loop shoot offset from controller buttons
public class Toggle extends Command{
    //declaring local variables
    int setpoint;
    double offset;
    double new_offset;
    Shooter shooter;


    public Toggle(int Setpoint, Shooter m_Shooter) {
        //local setpoint set to setpoint passed thru to use in later local functions
        this.setpoint = Setpoint;
        this.shooter = m_Shooter;

        addRequirements(m_Shooter);
    }   

    public void initialize()
    {
        //if the offset does not need to be reset to its original
        if (setpoint == 2)
        {
            shooter.toCustomSetpoint(18);
            shooter.runFlyWheels(-95);
            shooter.runFeederWheels(85);

        } 
        else 
        {
            shooter.toCustomSetpoint(0);
            shooter.runFlyWheels(0);
            shooter.runFeederWheels(0);

        }
        //ending command
        isFinished();
    }

    //nomenclature essential
    public boolean isFinished()
    {
        //ends the command
        return true;
    }

}
