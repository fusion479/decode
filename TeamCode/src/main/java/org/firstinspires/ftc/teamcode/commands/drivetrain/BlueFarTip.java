package org.firstinspires.ftc.teamcode.commands.drivetrain;

import com.arcrobotics.ftclib.command.CommandBase;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.utils.AutonomousHelpers;

@Configurable
public class BlueFarTip extends CommandBase {
    public static Pose tip = new Pose(94, 93, Math.toRadians(45));

    private final Drivetrain drivetrain;
    private final Follower follower;

    private boolean done;

    public BlueFarTip(final Drivetrain drivetrain) {
        this.drivetrain = drivetrain;

        this.done = false;
        this.follower = drivetrain.getFollower();
        super.addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        Path traj = AutonomousHelpers.buildLine(this.drivetrain.getFollower().getPose(), tip,
                AutonomousHelpers.HeadingInterpolation.LINEAR);
//
//        new PathCommand(this.drivetrain, traj).schedule();
        follower.followPath(
                follower.pathBuilder()
                        .addPath(new BezierLine(follower.getPose(), tip))
                        .setLinearHeadingInterpolation(follower.getHeading(), tip.getHeading())
                        .build()
        );

        this.done = true;
    }

    @Override
    public void end(boolean interrupted){
        if (interrupted){
            follower.breakFollowing();
        }
        follower.startTeleopDrive();
    }

    @Override
    public boolean isFinished() {
        return done && !follower.isBusy();
    }
}

