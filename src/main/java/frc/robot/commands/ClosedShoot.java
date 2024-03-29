package frc.robot.commands;

import frc.robot.subsystems.GroundIntake;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Limelight;




public class ClosedShoot extends Command
{
   
    //declaring local variables and subsystems
    Timer timer = new Timer();
    double offset = SmartDashboard.getNumber("offset", 0.40);
    double distance;
    double degrees;

    Climber climber;
    double elevator_point;
    Shooter shooter;
    GroundIntake intake;
    Limelight limelight;

    public ClosedShoot(Shooter m_Shooter, GroundIntake m_Intake, Climber m_Climber, Limelight m_Limelight) 
    {
        //sets the subsystems to go to position for the shoot
        //dont run the feeder wheels until shooter gets to correct degree
        this.climber = m_Climber;
        this.shooter = m_Shooter;
        this.intake = m_Intake;
        this.limelight = m_Limelight;

        addRequirements(m_Shooter);
        addRequirements(m_Intake);

    }

    public void initialize()
    {   

            if (climber.getStage() == 0)
            {
                int cur_id = limelight.getID();
                SmartDashboard.putNumber("cur ID", cur_id);
                distance = limelight.find_Tag_Y_Distance(limelight.findTagHeightFromID(limelight.check_eligible_id(cur_id)));
                SmartDashboard.putString("distance", Double.toString(distance));

                if (distance >= 0) 
                {
                    offset = SmartDashboard.getNumber("offset", 0.31);
                    degrees = (distance - offset)*(1.5/.05);

                    if (distance > 1)
                    {
                        if (distance < 1.1 && distance > 1.0)
                        {
                            degrees += 2.5;        
                        }      
                        if (distance > 1.2)
                        {
                            degrees -= 0.5;        
                        } 
                        if (distance > 1.28)
                        {
                            degrees -= 0.5;        
                        } 
                        if (distance > 1.3)
                        {
                            degrees -= 0.5;        
                        } 
                        if (distance > 1.33)
                        {
                            degrees -= .4;        
                        }          
                    }
                    else
                    {
                        degrees += 1;
                    }
                    if (degrees < 50 && degrees >= 0)
                    {
                        SmartDashboard.putNumber("degrees", degrees);

                        shooter.toCustomSetpoint(degrees);
                        shooter.runFlyWheels(-95);
                        shooter.runFeederWheels(0);
                    }
                }
            } 
            else if (climber.getStage() == 1)
            {
                intake.setRollers(0);
                shooter.runFlyWheels(-95);
                shooter.runFeederWheels(85);
            }
            else 
            {
                intake.setRollers(0);
                shooter.runFlyWheels(15);
                shooter.runFeederWheels(-15);
            }
            timer.restart();
        }

        public void execute()
        {
            isFinished();
        }

        @Override
        public boolean isFinished() 
        {
            if (climber.getStage() == 0)
            {
                if (timer.get() > 0.1) 
                {
                    if (shooter.getV() == 0 && distance > -1)
                    {
                        shooter.runFeederWheels(85);
                        intake.setRollers(-50);
                        
                        return true;
                    }
                }
            }
            else 
            {
                return true;
            }

            return false;
        }
    }


