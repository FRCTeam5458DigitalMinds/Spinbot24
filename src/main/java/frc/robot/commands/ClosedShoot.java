package frc.robot.commands;

import java.util.function.BooleanSupplier;

import javax.lang.model.util.ElementScanner14;

import frc.robot.subsystems.GroundIntake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;
import java.util.HashMap;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Limelight;




public class ClosedShoot extends Command
{
   
    Timer timer = new Timer();
    
    double distance;
    double degrees;

    Climber climber;
    double elevator_point;
    Shooter shooter;
    GroundIntake intake;
    Limelight limelight;
    
    
    //commented out bc no reasonable way to determine if we are there without doing math we would be "skipping"
    //  private double podium_degrees = 33.333;
    //  private double subwoofer_degrees = 65.662787;

    public ClosedShoot(Shooter m_Shooter, GroundIntake m_Intake, Climber m_Climber, Limelight m_Limelight) 
    {
        this.climber = m_Climber;
        this.shooter = m_Shooter;
        this.intake = m_Intake;
        this.limelight = m_Limelight;

        addRequirements(m_Shooter);
        addRequirements(m_Intake);

    }

    public void initialize()
    {   
        elevator_point = climber.getInches();
            if (elevator_point <= 1)
            {
                int cur_id = limelight.getID();
                SmartDashboard.putNumber("cur ID", cur_id);
                distance = limelight.find_Tag_Y_Distance(limelight.findTagHeightFromID(limelight.check_eligible_id(cur_id)));
                SmartDashboard.putString("DB/String 1", Double.toString(distance));
                SmartDashboard.putString("DB/String 8", "auto");

                if (distance >= 0) 
                {
                    degrees = 3.5;
                  //  degrees = (73.5 - (Math.atan(2.0447/distance) * (180/3.14159)));
                    if (degrees < 60 && degrees >= 0)
                    {
                        SmartDashboard.putNumber("degrees", degrees);
                        SmartDashboard.putNumber("Command finished", degrees / 360. * 218.75);
                        shooter.toCustomSetpoint(degrees);
                        shooter.runFlyWheels(-95);
                        shooter.runFeederWheels(0);
                    }
                    //intake.setRollers(-50);

                }
                else
                {
                    shooter.runFlyWheels(-95);
                    shooter.toSetPoint(0);
                    shooter.runFeederWheels(0);
                    
            
                    intake.setRollers(0);
                }
                
                //CALL AUTOMATIC LIMELIGHTSHOOTING HERE!!!!
            } 
            else if (elevator_point <= 7)
            {
                intake.setRollers(0);
                shooter.runFlyWheels(95);
                shooter.runFeederWheels(80);
            }
            else 
            {
                intake.setRollers(0);
                shooter.runFlyWheels(-50);
                shooter.runFeederWheels(-50);
            }
        }
        public void execute()
        {
            isFinished();
        }

        @Override
        public boolean isFinished() {
            if (timer.get() > 1 && shooter.getV() == 0)
            {
                shooter.runFeederWheels(85);
                intake.setRollers(-50);

                return true;
            }
            return false;
        }
    }


