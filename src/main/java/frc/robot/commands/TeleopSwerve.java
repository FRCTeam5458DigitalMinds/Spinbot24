// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import frc.robot.RobotContainer;
import java.lang.Math;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.SwerveSubsystem;

public class TeleopSwerve extends Command {
  private SwerveSubsystem m_SwerveSubsystem;

  private DoubleSupplier m_translationSupplier;
  private DoubleSupplier m_strafeSupplier;
  private DoubleSupplier m_rotationSupplier;
  private BooleanSupplier m_robotCentricSupplier;
  private BooleanSupplier m_SnapPressed;
  private BooleanSupplier m_strafeSnapPressed;
  private BooleanSupplier m_blueOrNot;
  private Limelight m_Limelight;
  private double m_tagHeightInches = 57.4166666;
  private double m_LimelightId;
  private double m_currentId;

  private int[] arraychosen;
  private double move_to_yaw;

  private SlewRateLimiter translationLimiter = new SlewRateLimiter(3.0); //can only change by 3 m/s in the span of 1 s
  private SlewRateLimiter strafeLimiter = new SlewRateLimiter(3.0);
  private SlewRateLimiter rotationLimiter = new SlewRateLimiter(3.0);
  private double rotationVal;
  private double translationVal;
  private double current_yaw;
  private double final_yaw;
  
  private double strafeVal;
  private boolean blueOrNot;

  /** Creates a new TeleopSwerve. */
    public TeleopSwerve(SwerveSubsystem SwerveSubsystem,
        Limelight Limelight,     
        DoubleSupplier translationSupplier,
        DoubleSupplier strafeSupplier,
        DoubleSupplier rotationSupplier,
        BooleanSupplier robotCentricSupplier,
        BooleanSupplier rotationSnapPressed,
        BooleanSupplier strafeSnapPressed,
        BooleanSupplier m_blueOrNot) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.m_SwerveSubsystem = SwerveSubsystem;
    this.m_Limelight = Limelight;
    this.m_SnapPressed = rotationSnapPressed;
    this.m_strafeSnapPressed = strafeSnapPressed;
    this.m_blueOrNot = m_blueOrNot;
    addRequirements(m_SwerveSubsystem);
    this.m_translationSupplier = translationSupplier;
    this.m_strafeSupplier = strafeSupplier;
    this.m_rotationSupplier = rotationSupplier;
    this.m_robotCentricSupplier = robotCentricSupplier;
    
  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      /* Get Values, applies Deadband, (doesnt do anything if stick is less than a value)*/
      if (m_strafeSnapPressed.getAsBoolean() == false) {
        
      strafeVal =
          strafeLimiter.calculate(
              MathUtil.applyDeadband(m_strafeSupplier.getAsDouble(), Constants.SwerveConstants.inputDeadband));
      }
      else {
        double m_y_angleToTagDegrees = Constants.LimelightConstants.m_limelightMountAngleDegree +  NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
        double m_y_angleToTagRadians = m_y_angleToTagDegrees * (3.14159 / 180.);

        double m_limelightToTagInches = Units.inchesToMeters(((m_tagHeightInches - Constants.LimelightConstants.m_limelightLensHeightInches) / Math.tan(m_y_angleToTagRadians)) - Constants.LimelightConstants.m_limelightToFrontOfRobot)*.07;

        strafeVal = m_limelightToTagInches*Math.tan(NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0));
        strafeVal = strafeLimiter.calculate(
        MathUtil.applyDeadband((strafeVal), Constants.SwerveConstants.inputDeadband));
      }

      double translationVal =
        translationLimiter.calculate(
            MathUtil.applyDeadband(m_translationSupplier.getAsDouble(), Constants.SwerveConstants.inputDeadband));

    if (m_SnapPressed.getAsBoolean() == false) 
    {
      SmartDashboard.putString("DB/String 5", Double.toString(m_rotationSupplier.getAsDouble()));
      rotationVal =
      rotationLimiter.calculate(
          MathUtil.applyDeadband(m_rotationSupplier.getAsDouble(), 0.7));

      SmartDashboard.putString("DB/String 9", Double.toString(move_to_yaw));
    }
    
  // rotation snapping, restore soon and configure to a seperate button
    /*
    else 
    {
      double x_offset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
      rotationVal = rotationLimiter.calculate(
           MathUtil.applyDeadband((x_offset/-27), Constants.SwerveConstants.inputDeadband));
      }
      */
      else {
        current_yaw = m_SwerveSubsystem.getYawDegrees();
      //  SmartDashboard.putString("DB/String 6", Double.toString(current_yaw));
        //SmartDashboard.putString("DB/String 7", Double.toString(final_yaw));
       /// SmartDashboard.putString("DB/String 8", Double.toString(m_LimelightId));

        m_currentId = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tid").getDouble(0);

        if (m_currentId > 0 && m_currentId < 17) 
        {
          m_LimelightId = m_currentId;
        }

        if (m_LimelightId > 0 && m_LimelightId < 17)
        {
          if (m_blueOrNot.getAsBoolean() == true) 
          {
            final_yaw = Constants.SwerveConstants.apTagGyrosBlue[((int)m_LimelightId - 1)];
          }
          else {
            final_yaw = Constants.SwerveConstants.apTagGyrosRed[((int)m_LimelightId - 1)];
          }
        }
        if (current_yaw > 360)
        {
          current_yaw -= (Math.ceil(current_yaw / 360)) * 360;
        }
        if (current_yaw < 0)
        {
          current_yaw += (Math.floor(-current_yaw / 360)) * 360;
        }
        move_to_yaw = (final_yaw - current_yaw);

        if (move_to_yaw > 180)
        {
          move_to_yaw -= 360;
        }
        
          //move_to_yaw = m_SwerveSubsystem.closestAngle(current_yaw, final_yaw);
        SmartDashboard.putString("DB/String 8", Double.toString(move_to_yaw));
        /*
          if (Math.abs(final_yaw - current_yaw) > 90 && Math.abs(final_yaw - current_yaw) < 270) {
            final_yaw = ((int)final_yaw + 180) % 360;
           Constants.SwerveConstants.maxSpeed = -Constant.SwerveConstants.maxSpeed;
        */

        move_to_yaw = move_to_yaw / 60;
            //move_to_yaw = m_SwerveSubsystem.closestAngle(final_yaw, current_yaw);

          //SmartDashboard.putString("DB/String 6", Double.toString(current_yaw));

        

          SmartDashboard.putString("DB/String 9", Double.toString(move_to_yaw));
          rotationVal = rotationLimiter.calculate(
            MathUtil.applyDeadband(move_to_yaw, 0));
    }
    /* Drive */
    SmartDashboard.putString("DB/String 0 ", Double.toString(translationVal));

    m_SwerveSubsystem.drive(
        //the joystick values (-1 to 1) multiplied by the max speed of the drivetrai

        new Translation2d(translationVal, strafeVal).times(Constants.SwerveConstants.maxSpeed),
        //rotation value times max spin speed
        rotationVal * Constants.SwerveConstants.maxAngularVelocity,
        //whether or not in field centric mode
        !m_robotCentricSupplier.getAsBoolean(),
        //open loop control
        true);
     }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
