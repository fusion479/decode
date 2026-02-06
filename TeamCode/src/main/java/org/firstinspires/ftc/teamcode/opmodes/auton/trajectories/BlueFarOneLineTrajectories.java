package org.firstinspires.ftc.teamcode.opmodes.auton.trajectories;

import static org.firstinspires.ftc.teamcode.utils.AutonomousHelpers.buildLine;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.utils.AutonomousHelpers;

import java.io.File;
import java.util.HashMap;

@Configurable
public class BlueFarOneLineTrajectories {
    private final HashMap<String, Pose> poses;

    public Path shootFirst, intakeSecond, shootSecond, setupThird, intakeThird, shootThird, park;

    public BlueFarOneLineTrajectories() {
        this.poses = AutonomousHelpers.getPosesByName(new File("").getAbsolutePath().concat("/sdcard/FIRST/positions/BlueFarOneLine.pp"));

        this.shootFirst = buildLine(
                poses.get("startPoint"),
                poses.get("shootFirst"),
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );

        this.intakeSecond = buildLine(
                poses.get("shootFirst"),
                poses.get("intakeHP"),
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );

        this.shootSecond = buildLine(
                poses.get("intakeHP"),
                poses.get("shootSecond"),
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );

        this.setupThird = buildLine(
                poses.get("shootSecond"),
                poses.get("setupThird"),
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );

        this.intakeThird = buildLine(
                poses.get("setupThird"),
                poses.get("intakeThird"),
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );

        this.shootThird = buildLine(
                poses.get("intakeThird"),
                poses.get("shootThird"),
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );

        this.park = buildLine(
                poses.get("shootThird"),
                poses.get("park"),
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );
    }

    public Pose getStart() {
        return this.poses.get("startPoint");
    }
}
