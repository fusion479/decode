package org.firstinspires.ftc.teamcode.commands.drivetrain;

import com.arcrobotics.ftclib.command.CommandBase;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.utils.AutonomousHelpers;
import org.firstinspires.ftc.teamcode.utils.commands.PathCommand;

@Configurable
public class RedTip extends CommandBase {
    public static Pose tip = new Pose(94, 93, Math.toRadians(45));

    private final Drivetrain drivetrain;

    public RedTip(final Drivetrain drivetrain) {
        this.drivetrain = drivetrain;

        super.addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        Path traj = AutonomousHelpers.buildLine(this.drivetrain.getFollower().getPose(), tip,
                AutonomousHelpers.HeadingInterpolation.LINEAR);

        new PathCommand(this.drivetrain, traj).schedule();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
