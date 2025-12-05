package org.firstinspires.ftc.teamcode.opmodes.auton.trajectories;

import static org.firstinspires.ftc.teamcode.utils.AutonomousHelpers.buildCurve;
import static org.firstinspires.ftc.teamcode.utils.AutonomousHelpers.buildLine;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.utils.AutonomousHelpers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

@Configurable
public class BlueCloseTrajectories {
    private final HashMap<String, Pose> poses;

    public Path scorePreload, intakeSecond, scoreSecond, intakeThird, scoreThird, intakeFourth, scoreFourth, intakeFifth, scoreFifth, park, setupTop, strafeTop, pushTop, setupMid, strafeMid, pushMid, setupBottom, strafeBottom, pushBottom;

    public BlueCloseTrajectories() {
        this.poses = AutonomousHelpers.getPosesByName(new File("").getAbsolutePath().concat("/sdcard/FIRST/positions/spec5.pp"));

        this.scorePreload = buildLine(
                poses.get("startPoint"),
                poses.get("Path 1"),
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );

    }

    public Pose getStart() {
        return this.poses.get(0);
    }
}