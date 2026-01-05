package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.Drivetrain;
import org.firstinspires.ftc.teamcode.pedroPathing.Follower;
import com.pedropathing.follower.FollowerConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.Mecanum;
import org.firstinspires.ftc.teamcode.pedroPathing.MecanumConstants;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
import com.pedropathing.ftc.localization.constants.OTOSConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.ftc.localization.constants.ThreeWheelConstants;
import com.pedropathing.ftc.localization.constants.ThreeWheelIMUConstants;
import com.pedropathing.ftc.localization.constants.TwoWheelConstants;
import com.pedropathing.ftc.localization.localizers.DriveEncoderLocalizer;
import com.pedropathing.ftc.localization.localizers.OTOSLocalizer;
import com.pedropathing.ftc.localization.localizers.PinpointLocalizer;
import com.pedropathing.ftc.localization.localizers.ThreeWheelIMULocalizer;
import com.pedropathing.ftc.localization.localizers.ThreeWheelLocalizer;
import com.pedropathing.ftc.localization.localizers.TwoWheelLocalizer;
import com.pedropathing.localization.Localizer;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.ArrayList;

/** This is the FollowerBuilder.
 * It is used to create Followers with a specific drivetrain + localizer without having to use a full constructor
 *
 * @author Baron Henderson - 20077 The Indubitables
 */
public class FollowerBuilder {
    private final FollowerConstants constants;
    private PathConstraints constraints;
    private final HardwareMap hardwareMap;
    private Localizer localizer;
    private Drivetrain drivetrain;

    public FollowerBuilder(FollowerConstants constants, HardwareMap hardwareMap) {
        this.constants = constants;
        this.hardwareMap = hardwareMap;
        constraints = PathConstraints.defaultConstraints;
    }

    public org.firstinspires.ftc.teamcode.pedroPathing.FollowerBuilder setLocalizer(Localizer localizer) {
        this.localizer = localizer;
        return this;
    }

    public org.firstinspires.ftc.teamcode.pedroPathing.FollowerBuilder driveEncoderLocalizer(DriveEncoderConstants lConstants) {
        return setLocalizer(new DriveEncoderLocalizer(hardwareMap, lConstants));
    }

    public org.firstinspires.ftc.teamcode.pedroPathing.FollowerBuilder OTOSLocalizer(OTOSConstants lConstants) {
        return setLocalizer(new OTOSLocalizer(hardwareMap, lConstants));
    }

    public org.firstinspires.ftc.teamcode.pedroPathing.FollowerBuilder pinpointLocalizer(PinpointConstants lConstants) {
        return setLocalizer(new PinpointLocalizer(hardwareMap, lConstants));
    }

    public org.firstinspires.ftc.teamcode.pedroPathing.FollowerBuilder threeWheelIMULocalizer(ThreeWheelIMUConstants lConstants) {
        return setLocalizer(new ThreeWheelIMULocalizer(hardwareMap, lConstants));
    }

    public org.firstinspires.ftc.teamcode.pedroPathing.FollowerBuilder threeWheelLocalizer(ThreeWheelConstants lConstants) {
        return setLocalizer(new ThreeWheelLocalizer(hardwareMap, lConstants));
    }

    public org.firstinspires.ftc.teamcode.pedroPathing.FollowerBuilder twoWheelLocalizer(TwoWheelConstants lConstants) {
        return setLocalizer(new TwoWheelLocalizer(hardwareMap, lConstants));
    }

    public org.firstinspires.ftc.teamcode.pedroPathing.FollowerBuilder setDrivetrain(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        return this;
    }

    public org.firstinspires.ftc.teamcode.pedroPathing.FollowerBuilder mecanumDrivetrain(MecanumConstants mecanumConstants) {
        return setDrivetrain(new Mecanum(hardwareMap, mecanumConstants));
    }

    public org.firstinspires.ftc.teamcode.pedroPathing.FollowerBuilder pathConstraints(PathConstraints pathConstraints) {
        this.constraints = pathConstraints;
        PathConstraints.setDefaultConstraints(pathConstraints);
        return this;
    }

    public Follower build() {
        return new Follower(constants, localizer, drivetrain, constraints);
    }
}
