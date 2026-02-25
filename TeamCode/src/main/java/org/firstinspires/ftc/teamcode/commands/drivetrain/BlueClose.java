package org.firstinspires.ftc.teamcode.commands.drivetrain;

import com.arcrobotics.ftclib.command.CommandBase;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

@Configurable
public class BlueClose extends CommandBase {
    public static boolean finished = false;
    public static int duration = 1000;
    public static Pose close = new Pose(52, 93.5, Math.toRadians(226.9));
    public static ElapsedTime timer;

    private final Drivetrain drivetrain;
    private final Follower follower;

    public BlueClose(final Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        timer = new ElapsedTime();
        timer.reset();

        this.follower = drivetrain.getFollower();
        super.addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        if (!finished) {
//        Path traj = AutonomousHelpers.buildLine(this.drivetrain.getFollower().getPose(), close,
//                AutonomousHelpers.HeadingInterpolation.LINEAR);
//
//        new PathCommand(this.drivetrain, traj).schedule();
            follower.followPath(
                    follower.pathBuilder()
                            .addPath(new BezierLine(follower.getPose(), close))
                            .setLinearHeadingInterpolation(follower.getHeading(), close.getHeading())
                            .build()
            );
        }
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            follower.breakFollowing();
        }
        follower.startTeleopDrive();
    }

    @Override
    public boolean isFinished() {
        if (finished && timer.milliseconds() > duration)
            return true;
        else if (!finished) {
            if ((Math.abs(follower.getPose().getY() - close.getY()) < 6) && (Math.abs(follower.getPose().getX() - close.getX()) < 6) && (Math.abs(Math.toDegrees(
                    follower.getPose().getHeading()
            )
                    + (Math.toDegrees(
                    follower.getPose().getHeading()
            ) < 0 ? 360 : 0)
                    - Math.toDegrees(close.getHeading()))
                    < 3)) {
                timer.reset();
                finished = true;
            }
            return false;
        } else return false;
    }
}

