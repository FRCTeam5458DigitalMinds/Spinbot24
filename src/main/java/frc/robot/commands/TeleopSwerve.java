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
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.SwerveSubsystem;

public class TeleopSwerve extends Command {
  private SwerveSubsystem m_SwerveSubsystem;

  private DoubleSupplier m_translationSupplier;
  private DoubleSupplier m_strafeSupplier;
  private DoubleSupplier m_rotationSupplier;
  private DoubleSupplier m_intakeSupplier;

  private BooleanSupplier m_robotCentricSupplier;
  private BooleanSupplier m_SnapPressed;
  private BooleanSupplier m_strafeSnapPressed;
  private BooleanSupplier m_blueOrNot;
  private Limelight m_Limelight;
  private double m_tagHeightInches = 57.4166666;
  private double m_LimelightId;
  private double m_currentId;

  private final XboxController m_DriveController = new XboxController(0);

  private int[] arraychosen;
  private double move_to_yaw;

  private SlewRateLimiter translationLimiter = new SlewRateLimiter(3.0); //can only change by 3 m/s in the span of 1 s
  private SlewRateLimiter strafeLimiter = new SlewRateLimiter(3.0);
  private SlewRateLimiter rotationLimiter = new SlewRateLimiter(3.0);
  private double rotationVal;
  private double intakeVal;

  private double distance;
  private double translationVal;
  private double current_yaw;
  private double final_yaw;
  
  private double strafeVal;
  private boolean blueOrNot;

  ProfiledPIDController controller;
  PIDController holdController;
  Constraints constraints;


  /** Creates a new TeleopSwerve. */
    public TeleopSwerve(SwerveSubsystem SwerveSubsystem,
        Limelight Limelight,     
        DoubleSupplier translationSupplier,
        DoubleSupplier strafeSupplier,
        DoubleSupplier rotationSupplier,
        DoubleSupplier intakeSupplier,
        BooleanSupplier robotCentricSupplier,
        BooleanSupplier rotationSnapPressed,
        BooleanSupplier strafeSnapPressed,
        BooleanSupplier m_blueOrNot) {


      constraints = new Constraints(Constants.SwerveConstants.m_maxAngularVelocity, Constants.SwerveConstants.m_maxAngularAcceleation);
        controller =
                new ProfiledPIDController(
                        0.1,
                        0,
                        0,
                        constraints);

      controller.enableContinuousInput(-Math.PI, Math.PI);
      controller.setTolerance(Math.PI / 180);
    // Use addRequirements() here to declare subsystem dependencies.
    this.m_SwerveSubsystem = SwerveSubsystem;
    this.m_Limelight = Limelight;
    this.m_SnapPressed = rotationSnapPressed;
    this.m_strafeSnapPressed = strafeSnapPressed;
    this.m_blueOrNot = m_blueOrNot;
    this.m_intakeSupplier = intakeSupplier;
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
    intakeVal = m_intakeSupplier.getAsDouble();
    SmartDashboard.putNumber("intake value", intakeVal);
    int cur_id = m_Limelight.getID();
    distance = m_Limelight.find_Tag_Y_Distance(m_Limelight.findTagHeightFromID(m_Limelight.check_eligible_id(cur_id)));
    double tx = Math.abs(m_Limelight.findXOffset());

    if (SmartDashboard.getBoolean("RUMBLE RUMBLE", false) == true)
    {
      m_DriveController.setRumble(RumbleType.kBothRumble, 0.1);
    } 
    else 
    {
      m_DriveController.setRumble(RumbleType.kBothRumble, 0.0);

    }
    if (distance < 1.5 && distance > 0 && tx < 20)
    {
      m_SwerveSubsystem.LEDon();
    }
    else
    {
      m_SwerveSubsystem.LEDoff(); 
    }
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
          MathUtil.applyDeadband(m_rotationSupplier.getAsDouble(), 0.1));

      SmartDashboard.putString("DB/String 9", Double.toString(move_to_yaw));
    }
    
  // rotation snapping, restore soon and configure to a seperate button

    else 
    {
      double x_offset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);

      if (cur_id == 4 || cur_id == 7)
      {
        rotationVal = calculate(-x_offset / 180 * 3.1415962);
      
      }
  
    }
        SmartDashboard.putString("DB/String 8", Double.toString(move_to_yaw));
    

        move_to_yaw = move_to_yaw / 60;
          SmartDashboard.putString("DB/String 9", Double.toString(move_to_yaw));
          rotationVal = rotationLimiter.calculate(
            MathUtil.applyDeadband(move_to_yaw, 0));
    
    /* Drive */
    m_SwerveSubsystem.drive(

        new Translation2d(translationVal, strafeVal).times(Constants.SwerveConstants.maxSpeed),
        //rotation value times max spin speed
        rotationVal * Constants.SwerveConstants.maxAngularVelocity,
        //whether or not in field centric mode
        !m_robotCentricSupplier.getAsBoolean(),
        //open loop control
        true);
    }

  public double calculate(double goalRadians) {
      double calculatedValue =
              controller.calculate(m_SwerveSubsystem.rotationRads(), goalRadians);
      if (atSetpoint()) {
          return calculateHold(goalRadians);
      } else {
          return calculatedValue;
      }
  }

  public double calculateHold(double goalRadians) {
      double calculatedValue =
              holdController.calculate(m_SwerveSubsystem.rotationRads(), goalRadians);
      return calculatedValue;
  }

  public boolean atSetpoint() {
      return controller.atSetpoint();
  }

  public boolean atHoldSetpoint() {
      return holdController.atSetpoint();
  }

  public void reset() {
    controller.reset(m_SwerveSubsystem.rotationRads());
    holdController.reset();
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
