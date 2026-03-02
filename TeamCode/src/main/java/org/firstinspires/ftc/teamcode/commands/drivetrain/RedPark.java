package org.firstinspires.ftc.teamcode.commands.drivetrain;

import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

public class RedPark extends CommandBase {
    public static Pose tip = new Pose(39, 32, Math.toRadians(180));
    private final Drivetrain drivetrain;
    private final Follower follower;

    public RedPark(final Drivetrain drivetrain) {
        this.drivetrain = drivetrain;

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
                        .addPath(new BezierLine(follower.getPose(), tip))
                        .setLinearHeadingInterpolation(follower.getHeading(), tip.getHeading())
                        .build()
        );
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

        return (Math.abs(follower.getPose().getY() - tip.getY()) < 1) && (Math.abs(follower.getPose().getX() - tip.getX()) < 1) && (Math.abs(Math.toDegrees(follower.getPose().getHeading()) - Math.toDegrees(tip.getHeading())) < 1);
    }
}

