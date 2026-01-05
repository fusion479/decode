package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import org.firstinspires.ftc.teamcode.pedroPathing.Follower;
import com.pedropathing.follower.FollowerConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.FollowerBuilder;
import org.firstinspires.ftc.teamcode.pedroPathing.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


public class Constants {
    public static double compensationFactor = 0.79;

    public static FollowerConstants followerConstants = new FollowerConstants()
            .forwardZeroPowerAcceleration(-43.26)
            .lateralZeroPowerAcceleration(-61.22)
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.005, 0, 0.00001,0.6, 0.01 ))
            .mass(12.8); // adjust number later

    public static MecanumConstants driveConstants = new MecanumConstants()
            .yVelocity(47.65)
            .xVelocity(57.06)
            .maxPower(1)
            .rightFrontMotorName("rightFront")
            .rightRearMotorName("rightRear")
            .leftFrontMotorName("leftFront")
            .leftRearMotorName("leftRear")
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightSideCompensationFactor(compensationFactor);

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(5.1614173)
            .strafePodX(-3.11024)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("odo")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD);

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        driveConstants.setUseBrakeModeInTeleOp(true);

        return new FollowerBuilder(followerConstants, hardwareMap)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .build();
    }
}
