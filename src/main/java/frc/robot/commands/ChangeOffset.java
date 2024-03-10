package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class ChangeOffset extends Command{
    int setpoint;

    double offset;

    public ChangeOffset(int Setpoint) {
        this.setpoint = Setpoint;
    }

    public void initialize()
    {
        if (setpoint != 2)
        {
            offset = SmartDashboard.getNumber("offset", 0.40);
            SmartDashboard.putNumber("offset", offset + 0.01 * setpoint);
            isFinished();
        } else {
            SmartDashboard.putNumber("offset", 0.40);
        }
    }

    public boolean isFinished()
    {
        return true;
    }

}
