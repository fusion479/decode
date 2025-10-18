package org.firstinspires.ftc.teamcode.commands.drivetrain;

import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.utils.AutonomousHelpers;
import org.firstinspires.ftc.teamcode.utils.commands.PathCommand;

public class BlueTip extends CommandBase {
    private final Drivetrain drivetrain;

    public BlueTip(final Drivetrain drivetrain) {
        this.drivetrain = drivetrain;

        super.addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        Path traj = AutonomousHelpers.buildLine(this.drivetrain.getFollower().getPose(), new Pose(36, -12, Math.toRadians(0)),
                AutonomousHelpers.HeadingInterpolation.LINEAR);

        new PathCommand(this.drivetrain, traj).schedule();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
