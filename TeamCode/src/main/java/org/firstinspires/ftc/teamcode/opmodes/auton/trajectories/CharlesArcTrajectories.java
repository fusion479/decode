package org.firstinspires.ftc.teamcode.opmodes.auton.trajectories;

import static org.firstinspires.ftc.teamcode.utils.AutonomousHelpers.buildCurve;
import static org.firstinspires.ftc.teamcode.utils.AutonomousHelpers.buildLine;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
//localization.Pose is used but its unused
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.utils.AutonomousHelpers;

import java.io.File;
import java.util.HashMap;


public class CharlesArcTrajectories {
    private final HashMap<String, com.pedropathing.localization.Pose> poses;

    public Path arc1;

    public CharlesArcTrajectories() {
        this.poses = AutonomousHelpers.getPosesByName(new File("").getAbsolutePath().concat("/sdcard/FIRST/positions/CharlesArc.pp"));

        //buildLine or buildCurve ?
        this.arc1 = buildLine(
                poses.get("startPoint");
                poses.get("coolArc");
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );
    }

    public com.pedropathing.localization.Pose getStart() {
        return this.poses.get("startPoint");
    }

}
