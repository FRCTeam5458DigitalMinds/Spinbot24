package frc.robot.subsystems;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.SwerveSubsystem;
import java.lang.Math;

public class Limelight 
{
    private SwerveSubsystem m_swerveSubsystem;
    //String m_Side = Robot.;
    double[] m_tagHeights = {};
    double m_hasValidTarget = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    double m_x_AngleOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    double m_y_AngleOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    double m_areaDetected = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);

    double m_tagHeightInches = 57.4166666;
    //26 1/6 31 1/4
    public void updateLimelightTracking() 
    {
        m_hasValidTarget = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
        m_x_AngleOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
        m_y_AngleOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
        m_areaDetected = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
    }

    //the Y and X distance is from an overhead view!
    public double find_Tag_Y_Distance() 
    {
        double m_y_angleToTagDegrees = Constants.LimelightConstants.m_limelightMountAngleDegree + m_y_AngleOffset;
        double m_y_angleToTagRadians = m_y_angleToTagDegrees * (3.14159 / 180.);

        double m_limelightToTagInches = ((m_tagHeightInches - Constants.LimelightConstants.m_limelightLensHeightInches) / Math.tan(m_y_angleToTagRadians)) - Constants.LimelightConstants.m_limelightToFrontOfRobot;

        return m_limelightToTagInches;
    }
   
    public void LimeToDrive() 
    {
        updateLimelightTracking();
        double Y_Distance = find_Tag_Y_Distance();

        SmartDashboard.putString("DB/String 1", Double.toString(Y_Distance));
        SmartDashboard.putString("DB/String 2", Double.toString(m_y_AngleOffset));
        SmartDashboard.putString("DB/String 2", Double.toString(m_x_AngleOffset));
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