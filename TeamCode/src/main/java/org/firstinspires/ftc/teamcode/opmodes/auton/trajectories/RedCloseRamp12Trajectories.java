package org.firstinspires.ftc.teamcode.opmodes.auton.trajectories;

import static org.firstinspires.ftc.teamcode.utils.AutonomousHelpers.buildCurve;
import static org.firstinspires.ftc.teamcode.utils.AutonomousHelpers.buildLine;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.utils.AutonomousHelpers;

import java.io.File;
import java.util.HashMap;

@Configurable
public class RedCloseRamp12Trajectories {
    private final HashMap<String, Pose> poses;

    public Path shootFirst, setupSecond, intakeSecond, shootSecond, setupThird, intakeThird, shootThird, park, setupFourth, intakeFourth, shootFourth;

    public RedCloseRamp12Trajectories() {
        this.poses = AutonomousHelpers.getPosesByName(new File("").getAbsolutePath().concat("/sdcard/FIRST/positions/RedCloseRamp12.pp"));

        this.shootFirst = buildLine(
                poses.get("startPoint"),
                poses.get("shootFirst"),
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );

        this.setupSecond = buildLine(
                poses.get("shootFirst"),
                poses.get("setupSecond"),
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );

        this.intakeSecond = buildLine(
                poses.get("setupSecond"),
                poses.get("intakeSecond"),
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );

        this.shootSecond = buildCurve(
                poses.get("intakeSecond"),
                poses.get("shootSecondControl0"),
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

        this.setupFourth = buildLine(
                poses.get("shootThird"),
                poses.get("setupFourth"),
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );

        this.intakeFourth = buildLine(
                poses.get("setupFourth"),
                poses.get("intakeFourth"),
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );

        this.shootFourth = buildLine(
                poses.get("intakeFourth"),
                poses.get("shootFourth"),
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );

        this.park = buildLine(
                poses.get("shootFourth"),
                poses.get("park"),
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );
    }

    public Pose getStart() {
        return this.poses.get("startPoint");
    }
}
