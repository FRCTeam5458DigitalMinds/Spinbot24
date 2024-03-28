package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

//changing the closed loop shoot offset from controller buttons
public class ChangeOffset extends Command{
    //declaring local variables
    int setpoint;
    double offset;

    public ChangeOffset(int Setpoint) {
        //local setpoint set to setpoint passed thru to use in later local functions
        this.setpoint = Setpoint;
    }

    public void initialize()
    {
        //if the offset does not need to be reset to its original
        if (setpoint != 2)
        {
            //reading the current offset from our smart dash and replacing with the addition/subtraction
            offset = SmartDashboard.getNumber("offset", 0.31);
            SmartDashboard.putNumber("offset", offset + 0.01 * setpoint);

        } 
        else 
        {
            //resetting the offset
            SmartDashboard.putNumber("offset", 0.31);
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
