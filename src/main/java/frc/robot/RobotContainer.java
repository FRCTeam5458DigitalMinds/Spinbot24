// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;



import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.TeleopSwerve;
import frc.robot.commands.Toggle;
import frc.robot.commands.ZeroShooter;
import frc.robot.commands.DeployIntake;
import frc.robot.commands.Eject;
import frc.robot.commands.EndHandoff;
import frc.robot.commands.FinishShoot;
import frc.robot.commands.Handoff;
import frc.robot.commands.MoveClimber;
import frc.robot.commands.OpenShoot;
import frc.robot.commands.RetractIntake;
import frc.robot.commands.AutoDeployIntake;
import frc.robot.commands.AutoShoot;
import frc.robot.commands.ChangeOffset;
import frc.robot.commands.ClosedShoot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.GroundIntake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Limelight;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import frc.robot.subsystems.Climber;


public class RobotContainer {

  //declaring controllers
  //operator controller can be plugged in, but is not currently used
  private final CommandXboxController m_DriveController = new CommandXboxController(0);


  //options for side sendable chooser
  public static final String m_blue = "Blue";
  public static final String m_red = "Red";

  //variable affected by sendable choosers from smart dash
  public String m_autoSelected;
  public String m_sideChosen;

  //declaration of sendable choosers
  //m_auto_chooser's options are prebuilt with path planner
  //do not try to add yourself or it breaks
  public final SendableChooser<String> m_side_chooser = new SendableChooser<>();
  public final SendableChooser<Command> m_auto_chooser;

  // declaring the axes for the drivers controller that affect teleop swerve command
  private final int translationAxis = XboxController.Axis.kLeftY.value;
  private final int strafeAxis = XboxController.Axis.kLeftX.value;
  private final int rotationAxis = XboxController.Axis.kRightX.value;
  
  //triggers that affect modes in teleop swerve command
  private final Trigger robotCentric =
  new Trigger(m_DriveController.leftBumper());

  private final Trigger rotation_snap_pressed =
  new Trigger(m_DriveController.rightBumper());  

  private final Trigger strafe_snap_pressed =
  new Trigger(m_DriveController.a());

  //for smart dash, affects the flipping of the path
  public final boolean blueOrNot = true;
  
  //Subsystems 
  private final SwerveSubsystem m_SwerveSubsystem = new SwerveSubsystem();
  private final Limelight m_Limelight = new Limelight();
  private final GroundIntake m_GroundIntake = new GroundIntake();
  private final Shooter m_Shooter = new Shooter();
  private final Climber m_Climber = new Climber(); 

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    m_SwerveSubsystem.LEDoff();

    //named commands for autos, make sure the name aligns with pathplanner named command
    NamedCommands.registerCommand("FarShoot", new ClosedShoot(m_Shooter, m_GroundIntake, m_Climber, m_Limelight));
    NamedCommands.registerCommand("FarShootB", new AutoShoot(m_Shooter, m_GroundIntake, m_Climber, m_Limelight, 4));
    NamedCommands.registerCommand("4Note A", new AutoShoot(m_Shooter, m_GroundIntake, m_Climber, m_Limelight, 1));
    NamedCommands.registerCommand("4Note B", new AutoShoot(m_Shooter, m_GroundIntake, m_Climber, m_Limelight, 2));
    NamedCommands.registerCommand("4Note C", new AutoShoot(m_Shooter, m_GroundIntake, m_Climber, m_Limelight, 3));
    NamedCommands.registerCommand("FarShootC", new AutoShoot(m_Shooter, m_GroundIntake, m_Climber, m_Limelight, -1));
    NamedCommands.registerCommand("SubShoot", new OpenShoot(m_Shooter, m_GroundIntake));
  

    NamedCommands.registerCommand("ShootFinish", new FinishShoot(m_Shooter, m_GroundIntake, m_Climber));
    NamedCommands.registerCommand("StopIntake", new RetractIntake(m_GroundIntake, m_Shooter, m_Climber));
    NamedCommands.registerCommand("Intake", new AutoDeployIntake(m_GroundIntake, m_Shooter, m_Climber));
    NamedCommands.registerCommand("RaiseElevator", new MoveClimber(m_GroundIntake, m_Shooter, m_Climber, 1));
    NamedCommands.registerCommand("LowerElevator", new MoveClimber(m_GroundIntake, m_Shooter, m_Climber, 0));

    
    //adding the option to the choosers
    m_side_chooser.setDefaultOption("Blue", m_blue);
    m_side_chooser.addOption("Red", m_red);

    m_auto_chooser = AutoBuilder.buildAutoChooser();

    //displaying choosers on smart dash
    SmartDashboard.putData("Side", m_side_chooser);
    SmartDashboard.putData("Auto Mode", m_auto_chooser);
    //swerve command to drive during tele-op
    m_SwerveSubsystem.setDefaultCommand(
      new TeleopSwerve(
          m_SwerveSubsystem, m_Limelight,
          () -> -m_DriveController.getRawAxis(translationAxis),
          () -> -m_DriveController.getRawAxis(strafeAxis),
          () -> -m_DriveController.getRawAxis(rotationAxis),
          () -> m_DriveController.getRawAxis(XboxController.Axis.kLeftTrigger.value),
          () -> robotCentric.getAsBoolean(),
          () -> rotation_snap_pressed.getAsBoolean(),
          () -> strafe_snap_pressed.getAsBoolean(),
          () -> blueOrNot));

    //configure the controller bindings
    configureBindings();
  }

  private void configureBindings() {

    //declaring controller button binds
    m_DriveController.button(Button.kX.value).whileTrue(new Handoff(m_Shooter, m_GroundIntake, m_Climber));
    m_DriveController.button(Button.kX.value).onFalse(new EndHandoff(m_Shooter, m_GroundIntake, m_Climber));
    
    m_DriveController.button(Button.kBack.value).onTrue(new ChangeOffset(-1));
    m_DriveController.button(Button.kStart.value).onTrue(new ChangeOffset(1));
    m_DriveController.button(Button.kRightStick.value).onTrue(new ChangeOffset(2));

    m_DriveController.button(Button.kLeftStick.value).onTrue(new ZeroShooter(m_Shooter, m_GroundIntake));

    m_DriveController.button(Button.kY.value).onTrue(new InstantCommand(() -> m_SwerveSubsystem.zeroGyro()));
    
    m_DriveController.povDown().onTrue(new MoveClimber(m_GroundIntake, m_Shooter, m_Climber, 0));
    m_DriveController.povLeft().onTrue(new MoveClimber(m_GroundIntake, m_Shooter, m_Climber, 1));
    m_DriveController.povUp().onTrue(new MoveClimber(m_GroundIntake, m_Shooter, m_Climber, 2));

    m_DriveController.button(Button.kB.value).onTrue(new Eject(m_GroundIntake, 0));
    m_DriveController.button(Button.kB.value).onFalse(new Eject(m_GroundIntake, 1));

    //m_DriveController.button(Button.kA.value).onTrue(new Toggle(2, m_Shooter));
    //m_DriveController.button(Button.kB.value).onTrue(new Toggle(1, m_Shooter));

    m_DriveController.button(Button.kRightBumper.value).onTrue(new ClosedShoot(m_Shooter, m_GroundIntake, m_Climber, m_Limelight));
    m_DriveController.button(Button.kRightBumper.value).onFalse(new FinishShoot(m_Shooter, m_GroundIntake, m_Climber));
    m_DriveController.axisGreaterThan(3, 0).onTrue(new OpenShoot( m_Shooter, m_GroundIntake));
    m_DriveController.axisGreaterThan(3, 0).onFalse(new FinishShoot(m_Shooter, m_GroundIntake, m_Climber));
    
    m_DriveController.axisGreaterThan(2, 0.05).onTrue(new DeployIntake(m_GroundIntake, m_Shooter, m_Climber).until(m_DriveController.axisLessThan(2, 0.02)));
    m_DriveController.axisGreaterThan(2, 0.02).onFalse(new RetractIntake(m_GroundIntake, m_Shooter, m_Climber));
    
  }

  //turns off robot LEDs
  public void turnOffLEDS()
  {
    m_SwerveSubsystem.LEDoff();
  }

  //moves wheels to their straightforward positions
  public void resetWheels()
  {
    m_SwerveSubsystem.setWheelsToX();
  }

  //returns the side chosen for auto in smart dash
  public String getSide() {
    m_sideChosen = m_side_chooser.getSelected();
    return m_sideChosen;
  }

  //returns the command chosen for auto in smart dash
  public Command getAutonomousCommand() {
    return m_auto_chooser.getSelected();
  }
}

