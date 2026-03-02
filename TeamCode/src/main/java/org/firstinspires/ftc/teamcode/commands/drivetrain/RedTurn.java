package org.firstinspires.ftc.teamcode.commands.drivetrain;

import com.arcrobotics.ftclib.command.CommandBase;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

@Configurable
public class RedTurn extends CommandBase {
    public static boolean finished = false;
    public static int duration = 1000;
    public static Pose goal = new Pose(132, 135);
    public static ElapsedTime timer;

    private final Drivetrain drivetrain;
    private final Follower follower;
    private Pose target = new Pose(0, 0, 0);

    public RedTurn(final Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        timer = new ElapsedTime();
        timer.reset();

        this.follower = drivetrain.getFollower();
        super.addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        double angle = Math.atan2(goal.getY() - follower.getPose().getY(), goal.getX() - follower.getPose().getX());
        target = new Pose(follower.getPose().getX(), follower.getPose().getY(), angle);
        PanelsTelemetry.INSTANCE.getTelemetry().addData("ANGLE", angle);
        PanelsTelemetry.INSTANCE.getTelemetry().addData("TARGET POSE", target);
        PanelsTelemetry.INSTANCE.getTelemetry().update();

        if (!finished) {
//        Path traj = AutonomousHelpers.buildLine(this.drivetrain.getFollower().getPose(), tip,
//                AutonomousHelpers.HeadingInterpolation.LINEAR);
//
//        new PathCommand(this.drivetrain, traj).schedule();
            follower.followPath(
                    follower.pathBuilder()
                            .addPath(new BezierLine(follower.getPose(), target))
                            .setLinearHeadingInterpolation(follower.getHeading(), target.getHeading())
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
            if ((Math.abs(follower.getPose().getY() - target.getY()) < 3) && (Math.abs(follower.getPose().getX() - target.getX()) < 3) && (Math.abs(Math.toDegrees(
                    follower.getPose().getHeading()
            )
                    + (Math.toDegrees(
                    follower.getPose().getHeading()
            ) < 0 ? 360 : 0)
                    - Math.toDegrees(target.getHeading()))
                    < 1)) {
                timer.reset();
                finished = true;
            }
            return false;
        } else return false;
    }
}

