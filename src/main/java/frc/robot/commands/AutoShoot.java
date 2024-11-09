package frc.robot.commands;

import frc.robot.subsystems.GroundIntake;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Limelight;




public class AutoShoot extends Command
{
   
    //declaring local variables and subsystems
    Timer timer = new Timer();
    double offset = SmartDashboard.getNumber("offset", 0.40);
    double distance;
    double degrees;
    int note;

    Climber climber;
    double elevator_point;
    Shooter shooter;
    GroundIntake intake;
    Limelight limelight;

    public AutoShoot(Shooter m_Shooter, GroundIntake m_Intake, Climber m_Climber, Limelight m_Limelight, int Note) 
    {
        //sets the subsystems to go to position for the shoot
        //dont run the feeder wheels until shooter gets to correct degree
        this.note = Note;
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

                if (note == 3)
                {
                    degrees = 27.25;
                }
                else if (note == -1)
                {
                    degrees = 23.7;
                }
                else if (note == 2)
                {
                    degrees = 27.5;
                }
                else
                {
                    degrees = 27.5;
                }

                if (degrees < 50 && degrees >= 0)
                {
                    SmartDashboard.putNumber("degrees", degrees);

                    shooter.toCustomSetpoint(degrees);
                    shooter.runFlyWheels(-100);
                    shooter.runFeederWheels(0);
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
            shooter.encoderPrint();

            isFinished();
        }

        @Override
        public boolean isFinished() 
        {
            if (climber.getStage() == 0)
            {
                if (timer.get() > 0.1) 
                {
                    if (shooter.getV() == 0)
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


