package frc.robot.subsystems;
import frc.robot.Constants;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.lang.Math;

public class Limelight extends SubsystemBase
{
    //String m_Side = Robot.;
    double[] tagHeights = {1.22, 1.22, 1.32, 1.32, 1.22, 1.22, 1.32, 1.32, 1.22, 1.22, 1.21, 1.21, 1.21, 1.21, 1.21, 1.21};
    double m_hasValidTarget = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    double m_x_AngleOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    double m_y_AngleOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    double m_areaDetected = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);

    long tag_ID =  NetworkTableInstance.getDefault().getTable("limelight").getEntry("tid").getInteger(Long.valueOf(0));
    double m_tagHeightInches = 57.4166666;

    public Limelight() 
    {

    }
    
    public void updateLimelightTracking() 
    {
        m_hasValidTarget = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
        m_x_AngleOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
        m_y_AngleOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
        m_areaDetected = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
        tag_ID =  NetworkTableInstance.getDefault().getTable("limelight").getEntry("tid").getInteger((0));

    }

    //the Y and X distance is from an overhead view!
    public double find_Tag_Y_Distance(double tagHeight) 
    {
        tagHeight *= 39.37;

        if (tagHeight > -1)
        {
            double m_y_angleToTagDegrees = Constants.LimelightConstants.m_limelightMountAngleDegree + m_y_AngleOffset;
            double m_y_angleToTagRadians = m_y_angleToTagDegrees * (3.14159 / 180.);

            double m_limelightToTagInches = ((tagHeight - Constants.LimelightConstants.m_limelightLensHeightInches) / Math.tan(m_y_angleToTagRadians));

            
            return Math.abs(m_limelightToTagInches /= 39.37);
        }
        return -1;
    }
    public int getID()
    {
        
        int ID = Math.toIntExact(NetworkTableInstance.getDefault().getTable("limelight").getEntry("tid").getInteger(Long.valueOf(0)));
        if (ID > -1 && ID < 17)
        {
            return ID;
        }
        return -1;
    }

    public double findTagHeightFromID(int id)
    {
        updateLimelightTracking();
        if (id != -1)
        {
            return tagHeights[Math.toIntExact(id) - 1];
        }
        return -1;
    }
    
    public int check_eligible_id(int id)
    {
        if (id == 3 || id == 4 || id == 7 || id == 8)
        {
            return id;
        }
        else
        {
            return -1;
        }
    }
   /*public void Rotation_Snap()
    {
        m_swerveSubsystem.drive(new Translation2d(0.0, 0.0) , m_x_AngleOffset/70, true, true);
        
    } */
   
    public double detectTarget() 
    {
        return m_hasValidTarget;
    }
   // public double findXOffset() 
    {
      //  return;
    }
    public boolean rightOfTag() 
    {
        //need to check if pos or not
        if (m_x_AngleOffset < 0) 
        {
            return true;
        }
        return false;
    }
    public double findYOffset() 
    {
        return m_y_AngleOffset;
    }
    
    public double findXOffset() 
    {
        updateLimelightTracking();
        return m_x_AngleOffset;
    }
    public double findAreaDetected() 
    {
        return m_areaDetected;
    }
}