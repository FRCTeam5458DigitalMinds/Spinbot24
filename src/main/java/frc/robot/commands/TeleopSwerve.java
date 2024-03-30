// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.lang.Math;

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
  private Limelight m_Limelight;
  private double m_tagHeightInches = 57.4166666;

  private final XboxController m_DriveController = new XboxController(0);

  private SlewRateLimiter translationLimiter = new SlewRateLimiter(3.0); //can only change by 3 m/s in the span of 1 s
  private SlewRateLimiter strafeLimiter = new SlewRateLimiter(3.0);
  private SlewRateLimiter rotationLimiter = new SlewRateLimiter(3.0);
  private double rotationVal;
  private double intakeVal;

  private double distance;
  
  private double strafeVal;

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
        //1.5, 0, 0.5
                new ProfiledPIDController(
                        1.1,
                        0,
                        .05,
                      constraints);
        // 10, 3, 0
        holdController = 
          new PIDController(0.7, 0, 0.05);

      controller.enableContinuousInput(-Math.PI, Math.PI);
      controller.setTolerance(0.05*Math.PI / 180);

      holdController.enableContinuousInput(-Math.PI, Math.PI);
      holdController.setTolerance(0.05 *Math.PI / 180);
    // Use addRequirements() here to declare subsystem dependencies.
    this.m_SwerveSubsystem = SwerveSubsystem;
    this.m_Limelight = Limelight;
    this.m_SnapPressed = rotationSnapPressed;
    this.m_strafeSnapPressed = strafeSnapPressed;
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
    double tx = (m_Limelight.findXOffset());

    if (SmartDashboard.getBoolean("RUMBLE RUMBLE", false) == true)
    {
      m_DriveController.setRumble(RumbleType.kBothRumble, 0.1);
    } 
    else 
    {
      m_DriveController.setRumble(RumbleType.kBothRumble, 0.0);

    }
    if (distance < 1.25 && distance > .7)
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

      SmartDashboard.putNumber("swerve rotation in rads", m_SwerveSubsystem.rotationRads());


    }
    else 
    {
      double x_offset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);

      if (cur_id == 4 || cur_id == 7)
      {
        SmartDashboard.putNumber("x offset", x_offset);
        //rotationVal =  rotationLimiter.calculate(
         // MathUtil.applyDeadband((-x_offset / 27), 0.1));
         if (Math.abs(x_offset) > 1.5)
         {
            rotationVal = calculate(m_SwerveSubsystem.rotationRads() + (-(x_offset) / 180 * Math.PI));
         }
         else
         {
          rotationVal = 0;
         }
      }
      else 
       {
         rotationVal = 0;
      }
      SmartDashboard.putNumber("roation vaule", rotationVal);
    }
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
    SmartDashboard.putNumber("swerve rotation in rads", m_SwerveSubsystem.rotationRads());
    SmartDashboard.putNumber("goal in rads", goalRadians);

      double calculatedValue =
              controller.calculate(m_SwerveSubsystem.rotationRads(), goalRadians);
      SmartDashboard.putNumber("caculated value", calculatedValue);

      if (atSetpoint()) {
          SmartDashboard.putBoolean("rot at setpoint", true);

          return calculateHold(goalRadians);
      } else {
          SmartDashboard.putBoolean("rot at setpoint", false);
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
