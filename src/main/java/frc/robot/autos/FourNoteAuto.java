
package frc.robot.autos;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import frc.robot.Constants;
import frc.robot.subsystems.SwerveSubsystem;

import com.pathplanner.lib.path.PathPlannerTrajectory;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.*;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.PathConstraints;

public class FourNoteAuto extends SequentialCommandGroup {
  public FourNoteAuto(SwerveSubsystem m_SwerveSubsystem) {

    // An example trajectory to follow.  All units in meters.
    //PathPlannerTrajectory testPath = PathPlanner.loadPath("testPath", new PathConstraints(4, 3));
    PathPlannerPath FourNoteAuto = PathPlannerPath.fromPathFile("4Note_Auto");
    

    AutoBuilder.followPath(FourNoteAuto).schedule();
  //  PathPlannerTrajectory testTrajectory = testPath.getTrajectory(null, null);

    /*var thetaController =
        new ProfiledPIDController(
            Constants.AutoConstants.kPThetaController,
            0,
            0,
            Constants.AutoConstants.kThetaControllerConstraints);
    thetaController.enableContinuousInput(-Math.PI, Math.PI);
    */

    /*addCommands(
        new InstantCommand(() -> m_SwerveSubsystem.resetOdometry(testPath.getInitialPose())),
        swerveControllerCommand);
  } */
  }
}