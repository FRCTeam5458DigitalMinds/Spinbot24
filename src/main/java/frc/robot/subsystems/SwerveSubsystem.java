// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.BooleanSupplier;

import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;
import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class SwerveSubsystem extends SubsystemBase {
  private final Pigeon2 pigeon;

  private PowerDistribution sparkPDH = new PowerDistribution(1, ModuleType.kRev);
  private SwerveDriveOdometry swerveOdometry;
  private SwerveModule[] mSwerveMods;

  private Field2d field;
  private BooleanSupplier sideChosen = () -> (SmartDashboard.getBoolean("Red?", false));

  /** Creates a new SwerveSubsystem. */
  public SwerveSubsystem() 
  {

    //instantiates new pigeon gyro, wipes it, and zeros it
    pigeon = new Pigeon2(Constants.SwerveConstants.PIGEON_ID);
    pigeon.getConfigurator().apply(new Pigeon2Configuration());
    zeroGyro();

    //Creates all four swerve modules into a swerve drive
    mSwerveMods =
    new SwerveModule[] 
    {
      new SwerveModule(0, Constants.SwerveConstants.Mod0.constants),
      new SwerveModule(1, Constants.SwerveConstants.Mod1.constants),
      new SwerveModule(2, Constants.SwerveConstants.Mod2.constants),
      new SwerveModule(3, Constants.SwerveConstants.Mod3.constants)
    };

    //creates new swerve odometry (odometry is where the robot is on the field)
    swerveOdometry = new SwerveDriveOdometry(Constants.SwerveConstants.swerveKinematics, getYaw(), getPositions());

    //puts out the field
    field = new Field2d();
    //SmartDashboard.putData("Field", field);
    //auto and path planner trajectory init
    
    // Reference to this subsystem to set requirements ); */
    AutoBuilder.configureHolonomic(this::getPose, 
      this::resetOdometry, 
      this::getChassisSpeeds, 
      this::driveRobotRelative,
     new HolonomicPathFollowerConfig(  
      new PIDConstants(10.0, 0.00001, 0.1),
      new PIDConstants(2.0, 0.0005, 0.001),
      
      4.5,
      .33655, 
      new ReplanningConfig()),
    sideChosen, this); 
  }

  public ChassisSpeeds getChassisSpeeds()
  {
    SmartDashboard.putString("DB/String 1", Double.toString(pigeon.getAngle()));  
    return Constants.SwerveConstants.swerveKinematics.toChassisSpeeds(getStates());

  }
  public SwerveDriveKinematics getKinematics()
  {
    return Constants.SwerveConstants.swerveKinematics;
  }

  public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop)
  //takes the coordinate on field wants to go to, the rotation of it, whether or not in field relative mode, and if in open loop control
  {
      SwerveModuleState[] swerveModuleStates =
      Constants.SwerveConstants.swerveKinematics.toSwerveModuleStates(
          //fancy way to do an if else statement 
          //if field relative == true, use field relative stuff, otherwise use robot centric
          fieldRelative
              ? ChassisSpeeds.fromFieldRelativeSpeeds(
                  translation.getX(), translation.getY(), rotation, getDriveYaw())
              : new ChassisSpeeds(translation.getX(), translation.getY(), rotation));
  //sets to top speed if above top speed
  SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.SwerveConstants.maxSpeed);

  //set states for all 4 modules
  for (SwerveModule mod : mSwerveMods) 
  {
    mod.setDesiredState(swerveModuleStates[mod.moduleNumber], isOpenLoop);
  }
  }
  public void LEDon()
  {
    sparkPDH.setSwitchableChannel(true);
  }
  
  public void LEDoff()
  {
    sparkPDH.setSwitchableChannel(false);
  }

  public void driveRobotRelative(ChassisSpeeds chassis)
  //takes the coordinate on field wants to go to, the rotation of it, whether or not in field relative mode, and if in open loop control
  {
    SwerveModuleState[] swerveModuleStates =
      Constants.SwerveConstants.swerveKinematics.toSwerveModuleStates(chassis);
    //sets to top speed if above top speed
    SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.SwerveConstants.maxSpeed);
  
    SmartDashboard.putString("DB/String 1", Double.toString(pigeon.getYaw().getValueAsDouble()));  
    //set states for all 4 modules
    for (SwerveModule mod : mSwerveMods) 
    {
      mod.setDesiredState(swerveModuleStates[mod.moduleNumber], true);
    }
  }

 
  public double rotationRads()
  {
    return getPose().getRotation().getRadians();
  }
  /* Used by SwerveControllerCommand in Auto */
  public void setModuleStates(SwerveModuleState[] desiredStates)
  {
    SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, Constants.SwerveConstants.maxSpeed);

    for (SwerveModule mod : mSwerveMods) 
    {
      mod.setDesiredState(desiredStates[mod.moduleNumber], false);
    }
  }

  public Pose2d getPose() 
  {
    return swerveOdometry.getPoseMeters();
  }

  public void resetOdometry(Pose2d pose) 
  {
    swerveOdometry.resetPosition(getYaw(), getPositions(), pose);
  }

  public void setWheelsToX() 
  {
    setModuleStates(new SwerveModuleState[] 
    {
      // front left
      new SwerveModuleState(0.0, Rotation2d.fromDegrees(45.0)),
      // front right
      new SwerveModuleState(0.0, Rotation2d.fromDegrees(-45.0)),
      // back left
      new SwerveModuleState(0.0, Rotation2d.fromDegrees(135.0)),
      // back right
      new SwerveModuleState(0.0, Rotation2d.fromDegrees(-135.0))
    });
  }


  public SwerveModuleState[] getStates() {
    SwerveModuleState[] states = new SwerveModuleState[4];
    for (SwerveModule mod : mSwerveMods) {
      states[mod.moduleNumber] = mod.getState();
    }
    return states;
  }

  public SwerveModulePosition[] getPositions(){
    SwerveModulePosition[] positions = new SwerveModulePosition[4];
    for(SwerveModule mod : mSwerveMods){
        positions[mod.moduleNumber] = mod.getPosition();
    }
    return positions;
}


  public void zeroGyro() {
    pigeon.setYaw(0);
  }




  // public double closestAngle(double a, double b)
  //       {
  //       // get direction
  //       double dir = (b % 360.0) - (a % 360.0);

  //       // convert from -360 to 360 to -180 to 180
  //       if (Math.abs(dir) > 180.0)
  //       {
  //               dir = -(Math.signum(dir) * 360.0) + dir;
  //       }
  //       return dir;
  //       }
        
  public Rotation2d getYaw() {
    //fancy if else loop again
    return (Constants.SwerveConstants.invertPigeon)
        ? Rotation2d.fromDegrees(360 - pigeon.getYaw().getValueAsDouble())
        : Rotation2d.fromDegrees(-pigeon.getAngle());
  }
  public Rotation2d getDriveYaw() {
    //fancy if else loop again
    return (Constants.SwerveConstants.invertPigeon)
        ? Rotation2d.fromDegrees(360 - pigeon.getYaw().getValueAsDouble())
        : Rotation2d.fromDegrees(pigeon.getYaw().getValueAsDouble());
  }
  public double getYawDegrees()
  {
    return (Constants.SwerveConstants.invertPigeon)
        ? (360 - pigeon.getYaw().getValueAsDouble())
        : (pigeon.getYaw().getValueAsDouble());
  }
  

  public boolean AutoBalance(){
    double roll_error = pigeon.getPitch().getValueAsDouble();//the angle of the robot
    double balance_kp = -.005;//Variable muliplied by roll_error
    double position_adjust = 0.0;
    double min_command = 0.0;//adds a minimum input to the motors to overcome friction if the position adjust isn't enough
    if (roll_error > 6.0)
    {
      position_adjust = balance_kp * roll_error + min_command;//equation that figures out how fast it should go to adjust
      //position_adjust = Math.max(Math.min(position_adjust,.15), -.15);  this gets the same thing done in one line
      if (position_adjust > .1){position_adjust = .1;}
      if (position_adjust < -.1){position_adjust = -.1;}
      drive(new Translation2d(position_adjust, 0), 0.0, true, false);
      double m_x_AngleOffset = 1;
      drive(new Translation2d(0.0, 0.0) , m_x_AngleOffset/1000, true, true);
      
      return false;
    }
    else if (roll_error < -6.0)
    {
      position_adjust = balance_kp * roll_error - min_command;
      drive(new Translation2d(position_adjust, 0), 0.0, true, false);
      if (position_adjust > .3){position_adjust = .3;}
      if (position_adjust < -.3){position_adjust = -.3;}
      return false;
    }
    else{
      drive(new Translation2d(0, 0), 0.0, true, false);
      return true;}
    
  }



  @Override
  public void periodic() {
    swerveOdometry.update(getYaw(), getPositions());
    field.setRobotPose(getPose());

    SmartDashboard.putNumber("Pigeon Roll",  pigeon.getPitch().getValueAsDouble());

    //commented this out because it is cluttering my dashboard lol!
    /* for (SwerveModule mod : mSwerveMods) {
      SmartDashboard.putNumber(
          "Mod " + mod.moduleNumber + " Cancoder", mod.getCanCoder().getDegrees());
      SmartDashboard.putNumber(
          "Mod " + mod.moduleNumber + " Integrated", mod.getState().angle.getDegrees());
      SmartDashboard.putNumber(
          "Mod " + mod.moduleNumber + " Velocity", mod.getState().speedMetersPerSecond);
      }*/
  
}

}