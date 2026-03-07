package org.firstinspires.ftc.teamcode.commands.drivetrain;


import com.arcrobotics.ftclib.command.CommandBase;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

@Configurable
public class BlueCloseTip extends CommandBase {
    public static boolean finished = false;
    public static int duration = 1000;
    public static Pose tip = new Pose(73, 76, Math.toRadians(226.8));

    private final Drivetrain drivetrain;
    private final Follower follower;
    public static ElapsedTime timer;

    public BlueCloseTip(final Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        timer = new ElapsedTime();
        timer.reset();

        this.follower = drivetrain.getFollower();
        super.addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        if (!finished) {
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
            if ((Math.abs(follower.getPose().getY() - tip.getY()) < 3) && (Math.abs(follower.getPose().getX() - tip.getX()) < 3) && (Math.abs(Math.toDegrees(
                    follower.getPose().getHeading()
            )
                    + (Math.toDegrees(
                    follower.getPose().getHeading()
            ) < 0 ? 360 : 0)
                    - Math.toDegrees(tip.getHeading()))
                    < 1)) {
                timer.reset();
                finished = true;
            }
            return false;
        } else return false;
    }
}
