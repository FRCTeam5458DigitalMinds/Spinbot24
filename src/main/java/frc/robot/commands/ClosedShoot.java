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
    double offset = SmartDashboard.getNumber("offset", 0.40);
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

            if (climber.getStage() == 0)
            {
                int cur_id = limelight.getID();
                SmartDashboard.putNumber("cur ID", cur_id);
                distance = limelight.find_Tag_Y_Distance(limelight.findTagHeightFromID(limelight.check_eligible_id(cur_id)));
               // SmartDashboard.putNumber("x distance", 1.32)
                SmartDashboard.putString("distance", Double.toString(distance));
                SmartDashboard.putString("DB/String 8", "auto");
               // String off_string = SmartDashboard.get("offset", );

                
                if (distance >= 0) 
                {
                    //B2 .645
                    //
                   
                    offset = SmartDashboard.getNumber("offset", 0.40);
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
                        if (distance > 1.38)
                        {
                            degrees -= 0.5;  
                        }
                          
                    }
                    else
                    {
                        degrees += 1;
                    }      // degrees = 15;
                    //degrees = 3.5;
                  //  degrees = (73.5 - (Math.atan(2.0447/distance) * (180/3.14159)));
                    if (degrees < 50 && degrees >= 0)
                    {
                        SmartDashboard.putNumber("degrees", degrees);
                        SmartDashboard.putNumber("Command finished", degrees / 360. * 218.75);
                        shooter.toCustomSetpoint(degrees);
                        shooter.runFlyWheels(-95);
                        shooter.runFeederWheels(0);
                    }
                    //intake.setRollers(-50); 
                }
                
                //CALL AUTOMATIC LIMELIGHTSHOOTING HERE!!!!
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
            SmartDashboard.putString("DB/String 1", "closed shoot");

            timer.restart();
        }
        public void execute()
        {
            isFinished();
            SmartDashboard.putNumber("elevator stage", climber.getStage());
        }

        @Override
        public boolean isFinished() {
            if (climber.getStage() == 0)
            {
                if (timer.get() > 0.1) {
                    
                    SmartDashboard.putString("DB/String 1", "timer good, >:(");
                    if (shooter.getV() == 0 && distance > -1)
                    {
                        
                        SmartDashboard.putString("DB/String 1", "good!");

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


