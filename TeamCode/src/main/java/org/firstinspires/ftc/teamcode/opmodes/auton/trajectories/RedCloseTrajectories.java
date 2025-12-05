package org.firstinspires.ftc.teamcode.opmodes.auton.trajectories;

import static org.firstinspires.ftc.teamcode.utils.AutonomousHelpers.buildCurve;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.utils.AutonomousHelpers;

import java.io.File;
import java.util.HashMap;

@Configurable
public class RedCloseTrajectories {
    private final HashMap<String, Pose> poses;


    public RedCloseTrajectories() {
        this.poses = AutonomousHelpers.getPosesByName(new File("").getAbsolutePath().concat("/sdcard/FIRST/positions/spec5.pp"));

    }

    public Pose getStart() {
        return this.poses.get(0);
    }
}