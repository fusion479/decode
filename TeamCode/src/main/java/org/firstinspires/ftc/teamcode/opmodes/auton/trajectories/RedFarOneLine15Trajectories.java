package org.firstinspires.ftc.teamcode.opmodes.auton.trajectories;

import static org.firstinspires.ftc.teamcode.utils.AutonomousHelpers.buildLine;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.utils.AutonomousHelpers;

import java.io.File;
import java.util.HashMap;

@Configurable
public class RedFarOneLine15Trajectories {
    private final HashMap<String, Pose> poses;

    public Path shootFirst, intakeSecond, shootSecond, setupThird, intakeThird, shootThird, intakeFourth, shootFourth, intakeFifth, shootFifth,  park;

    public RedFarOneLine15Trajectories() {
        this.poses = AutonomousHelpers.getPosesByName(new File("").getAbsolutePath().concat("/sdcard/FIRST/positions/RedFarOneLine15.pp"));

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

        this.intakeFourth = buildLine(
                poses.get("shootThird"),
                poses.get("intakeFourth"),
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );

        this.shootFourth = buildLine(
                poses.get("intakeFourth"),
                poses.get("shootFourth"),
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );

        this.intakeFifth = buildLine(
                poses.get("shootFourth"),
                poses.get("intakeFifth"),
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );

        this.shootFifth = buildLine(
                poses.get("intakeFifth"),
                poses.get("shootFifth"),
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );

        this.park = buildLine(
                poses.get("shootFifth"),
                poses.get("park"),
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );
    }

    public Pose getStart() {
        return this.poses.get("startPoint");
    }
}
