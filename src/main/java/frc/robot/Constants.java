// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

//https://pro.docs.ctr-electronics.com/_/downloads/en/latest/pdf/
package frc.robot;

import com.revrobotics.CANSparkBase.IdleMode;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import frc.lib.SwerveModuleConstants;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */


public final class Constants {
  public static final class SwerveConstants{
    public static final double inputDeadband = .2;
    public static final int PIGEON_ID = 9; //tochange
    public static final boolean invertPigeon = false;
    public static final int[] apTagGyrosRed = {0, 0, 180, 180, -90, 0, 0, 0, 0, 0, -60, 60, 180, 0, 0, 0};
    public static final int[] apTagGyrosBlue = {0, 0, 0, 0, 90, 90, 180, 180, 0, 0, 0, 0, 0, 180, 60, -60};
        /* Drivetrain Constants */
    public static final double trackWidth = Units.inchesToMeters(20.5);//to find
    public static final double wheelBase = Units.inchesToMeters(20.5);//to find
    public static final double wheelDiameter = Units.inchesToMeters(4.0);
    public static final double wheelCircumference = wheelDiameter * Math.PI;

    public static final double openLoopRamp = 0.25;
    public static final double closedLoopRamp = 0.0;

    public static final double driveGearRatio = (6.75 / 1.0); // 6.75:1 L2 Mk4 Modules
    //L1 is 8.14:1, L2 is 6.75:1, L3 is 6.12:1, L4 is 5.14:1
    public static final double angleGearRatio = (21.42 / 1.0); // 12.8:1 MK4 SDS Modules

    public static final SwerveDriveKinematics swerveKinematics =
    new SwerveDriveKinematics(
        new Translation2d(wheelBase / 2.0, trackWidth / 2.0), //translation 2d locates the swerve module in cords
        new Translation2d(wheelBase / 2.0, -trackWidth / 2.0),
        new Translation2d(-wheelBase / 2.0, trackWidth / 2.0),
        new Translation2d(-wheelBase / 2.0, -trackWidth / 2.0));
    //https://docs.wpilib.org/en/stable/docs/software/kinematics-and-odometry/swerve-drive-kinematics.html
    //SwerveDrive Kinematics converts between a ChassisSpeeds object and several SwerveModuleState objects, 
    //which contains velocities and angles for each swerve module of a swerve drive robot.
        
    /* Swerve Voltage Compensation */
    public static final double voltageComp = 12.0;
       
    //Swerve Current Limiting for neos
    public static final int angleContinuousCurrentLimit = 20; //limits current draw of turning motor
    public static final int driveContinuousCurrentLimit = 37; //limits current draw of drive motor
  


    /* Drive Motor PID Values */
    public static final double driveKP = 0.3; //to tune
    public static final double driveKI = 0.0; //to tune
    public static final double driveKD = 0.05; //to tune
   public static final double driveKFF = 0.0; //to tune

    /* Drive Motor Characterization Values */
    //values to calculate the drive feedforward (KFF)
    public static final double driveKS = 0.667; //to calculate
    public static final double driveKV = 2.44; //to calculate
    public static final double driveKA = 0.27; //to calculate

    /* Drive Motor Conversion Factors */
    public static final double driveConversionPositionFactor =
    (wheelDiameter * Math.PI) / driveGearRatio;
    public static final double driveConversionVelocityFactor = driveConversionPositionFactor / 60.0;
    public static final double angleConversionFactor = 360.0 / angleGearRatio;

    /* Swerve Profiling Values */
    public static final double maxSpeed = 15.1; // meters per second
    public static final double automaxspeed = 4.5;
    public static final double autoacceleration = 3;
    public static final double maxAngularVelocity = 15.7; //

    /* Neutral Modes */
    public static final IdleMode angleNeutralMode = IdleMode.kBrake;
    public static final IdleMode driveNeutralMode = IdleMode.kBrake;

    /* Motor Inverts */
    public static final boolean driveInvert = false;
    public static final boolean angleInvert = true;

    /* Angle Encoder Invert */
    public static final boolean canCoderInvert = false;
    

        /* Module Specific Constants */
    /* Front Left Module - Module 0 */
    public static final class Mod0 {
      public static final int driveMotorID = 6; 
      public static final int angleMotorID = 5; 
      public static final int canCoderID = 34;
      public static final Rotation2d angleOffset = Rotation2d.fromRotations(0.846680);
    /* Angle Motor PID Values */
      public static final double angleKP = 0.01; //to tune
      public static final double angleKI = 0.0; //to tune
      public static final double angleKD = 0.0; //to tune
      public static final double angleKFF = 0.0; //to tune
    
      public static final SwerveModuleConstants constants =
          new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset, angleKP, angleKI, angleKD, angleKFF);
          //creates a constant with all info from swerve module
    }

    /* Front Right Module - Module 1 */
    public static final class Mod1 {
      public static final int driveMotorID = 8;
      public static final int angleMotorID = 7;
      public static final int canCoderID = 32;
      public static final Rotation2d angleOffset =  Rotation2d.fromRotations(0.812012);
      /* Angle Motor PID Values */
      public static final double angleKP = 0.01; //to tune
      public static final double angleKI = 0.0; //to tune
      public static final double angleKD = 0.0; //to tune
      public static final double angleKFF = 0.0; //to tune
        
      public static final SwerveModuleConstants constants =
          new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset, angleKP, angleKI, angleKD, angleKFF);
          //creates a constant with all info from swerve module
    }

    /* Back Left Module - Module 2 */
    public static final class Mod2 {
      public static final int driveMotorID = 4;
      public static final int angleMotorID = 3;
      public static final int canCoderID = 38;
      public static final Rotation2d angleOffset = Rotation2d.fromRotations(0.213379);
      /* Angle Motor PID Values */
      public static final double angleKP = 0.01; //to tune
      public static final double angleKI = 0.0; //to tune
      public static final double angleKD = 0.0; //to tune
      public static final double angleKFF = 0.0; //to tune
  
      public static final SwerveModuleConstants constants =
        new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset, angleKP, angleKI, angleKD, angleKFF);
        //creates a constant with all info from swerve module
    }

    /* Back Right Module - Module 3 */
    public static final class Mod3 {
      public static final int driveMotorID = 2;
      public static final int angleMotorID = 1;
      public static final int canCoderID = 36;
      public static final Rotation2d angleOffset = Rotation2d.fromRotations(0.822754);
        /* Angle Motor PID Values */
      public static final double angleKP = 0.01; //to tune
      public static final double angleKI = 0.0; //to tune
      public static final double angleKD = 0.0; //to tune
      public static final double angleKFF = 0.0; //to tune
    
      public static final SwerveModuleConstants constants =
          new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset, angleKP, angleKI, angleKD, angleKFF);
          //creates a constant with all info from swerve module
    }
  

    public static final boolean angleMotorInvert = false;
    public static final boolean driveMotorInvert = true;

    



  }

  public static final class AutoConstants {
    public static final double kMaxSpeedMetersPerSecond = 1.75;
    public static final double kMaxAccelerationMetersPerSecondSquared = 2;
    public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
    public static final double kMaxAngularSpeedRadiansPerSecondSquared = Math.PI;

    public static final double kPXController = 1;
    public static final double kPYController = 1;
    public static final double kPThetaController = 1;

    // Constraint for the motion profilied robot angle controller
    public static final TrapezoidProfile.Constraints kThetaControllerConstraints =
        new TrapezoidProfile.Constraints(
            kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);
  }

  public static final class LimelightConstants {
    public static final double m_limelightMountAngleDegree = 50.25;
    public static final double m_limelightLensHeightInches = 10;
    public static final double m_limelightToFrontOfRobot = 0;
  }
  public static final class IntakeConstants {
    public static final int intake_ID = 10;
    public static final int roller_ID = 11;

    //PID values for the controller, to tune look up methods but start with 0
    //largely ignore the integral value as it can quickly cause problems

    //targets the errors over time
    public static final double kI = 0.000075;

    //targets the errors in positioning
    public static final double kP = 0.9;
    //targets the errors in velocity
    public static final double kD = 0.9;

    public static final double FF = 0.0009;

    public static final double max_vel = 1500;
    public static final double min_vel = 0;
    public static final double max_accel = 1000;

    public static final double allowed_error = 0.23;
  }
  public static final class ShooterConstants {
    public static final int Shooter_ID = 12;

    public static final int FlyWheelOne_ID = 13;
    public static final int FlyWheelTwo_ID = 14;
    public static final int FeederWheel_ID = 15;

    public static final double kI = 0.00;
    public static final double kP = 20;
    public static final double kD = 0.00;
    public static final double kV = 0.0;
  }
  public static final class ClimbConstants {
    public static final int climb_ID1 = 16;
    public static final int climb_ID2 = 17;
    //targets the errors over time
    public static final double kI = 0.000;

    //targets the errors in positioning
    public static final double kP = 0.0075;
    //targets the errors in velocity
    public static final double kD = 0.0005;

    public static final double FF = 0.00055;

    public static final double max_vel = 2000;
    public static final double min_vel = 0;
    public static final double max_accel = 1500;

    public static final double allowed_error = 0.015;
  }
}
