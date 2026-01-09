package org.firstinspires.ftc.teamcode.commands.drivetrain;

import com.arcrobotics.ftclib.command.CommandBase;
import com.bylazar.configurables.annotations.Configurable;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

@Configurable
public class RedFarTip extends CommandBase {
    public static Pose tip = new Pose(67, 26, Math.toRadians(150));

    private final Drivetrain drivetrain;
    private final Follower follower;

    private boolean done;

    public RedFarTip(final Drivetrain drivetrain) {
        this.drivetrain = drivetrain;

        this.done = false;
        this.follower = drivetrain.getFollower();
        super.addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
//        Path traj = AutonomousHelpers.buildLine(this.drivetrain.getFollower().getPose(), tip,
//                AutonomousHelpers.HeadingInterpolation.LINEAR);
//
//        new PathCommand(this.drivetrain, traj).schedule();
        follower.followPath(
                    follower.pathBuilder()
                            .addPath(new BezierCurve(follower.getPose(), tip))
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
        return (Math.abs(follower.getPose().getY() - tip.getY()) < 6) && (Math.abs(follower.getPose().getX() - tip.getX()) < 6) && (Math.abs(Math.toDegrees(follower.getPose().getHeading()) - Math.toDegrees(tip.getHeading())) < 3);
    }
}
