package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.utils.commands.PathCommand;

import java.io.PrintWriter;
import java.io.StringWriter;

@Configurable
public class Drivetrain extends SubsystemBase {
    public static double MAX_ACCEL = 0.3;
    public static double MAX_ANGULAR_ACCEL = 0.2;

    public static double MAX_DEACCEL = 0.5;
    public static double MAX_ANGULAR_DEACCEL = 0.5;

    public static double MAX_VEL = 0.75;
    public static double MAX_ANGULAR_VEL = 0.6;

    public static boolean ROBOT_CENTRIC = true;
    private final Follower follower;

    private final Limelight3A limelight;
    private final GamepadEx gamepad;

    private double xPower = 0.0;
    private double angPower = 0.0;
    private double yPower = 0.0;

    public Drivetrain(final HardwareMap hwMap, Pose startPose, GamepadEx gamepad) {
        this.follower = Constants.createFollower(hwMap);
        this.follower.setStartingPose(startPose);
        this.follower.update();

        this.limelight = hwMap.get(Limelight3A.class, "limelight");
        this.limelight.pipelineSwitch(0);
        this.limelight.start();

        this.gamepad = gamepad;
    }

    private static double calculateAccel(double accel, double deaccel, double prevPower, double check) {
        double rel;

        if (Math.abs(prevPower) > Math.abs(check))
            rel = Math.min(deaccel, Math.abs(check - prevPower));
        else rel = Math.min(accel, Math.abs(check - prevPower));

        return check - prevPower >= 0 ? rel : -rel;
    }

//    public void startThread(final GamepadEx gamepad, CommandOpMode opMode) {
//        new Thread(() -> {
//            while (opMode.opModeIsActive())
//                try {
//                    synchronized (this.follower) {
//                        this.yPower += Drivetrain.calculateAccel(MAX_ACCEL, MAX_DEACCEL, this.yPower, gamepad.getLeftY());
//                        this.xPower += Drivetrain.calculateAccel(MAX_ACCEL, MAX_DEACCEL, this.xPower, gamepad.getLeftX());
//                        this.angPower += Drivetrain.calculateAccel(MAX_ANGULAR_ACCEL, MAX_ANGULAR_DEACCEL, this.angPower, -gamepad.getRightX());
//
//                        this.follower.update();
//                        this.follower.setTeleOpDrive(yPower * MAX_VEL, xPower * MAX_VEL, angPower * MAX_ANGULAR_VEL, ROBOT_CENTRIC);
//                        this.follower.update();
//                    }
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    StringWriter errors = new StringWriter();
//                    e.printStackTrace(new PrintWriter(errors));
//                    PanelsTelemetry.INSTANCE.getTelemetry().addLine(errors.toString());
//                }
//        }).start();
//    }

    @Override
    public void periodic() {
        this.yPower += Drivetrain.calculateAccel(MAX_ACCEL, MAX_DEACCEL, this.yPower, gamepad.getLeftY());
        this.xPower += Drivetrain.calculateAccel(MAX_ACCEL, MAX_DEACCEL, this.xPower, -gamepad.getLeftX());
        this.angPower += Drivetrain.calculateAccel(MAX_ANGULAR_ACCEL, MAX_ANGULAR_DEACCEL, this.angPower, -gamepad.getRightX());

        this.follower.update();
        this.follower.setTeleOpDrive(yPower * MAX_VEL, xPower * MAX_VEL, angPower * MAX_ANGULAR_VEL, ROBOT_CENTRIC);
        this.follower.update();

        this.relocalize();
    }


    public void relocalize() {
        LLResult llResult = limelight.getLatestResult();

        if (llResult != null && llResult.isValid()) {
            Pose3D botpose = llResult.getBotpose();

            this.follower.setPose(
                    new Pose(
                            botpose.getPosition().x,
                            botpose.getPosition().y,
                            botpose.getOrientation().getYaw()
                    )
            );

            PanelsTelemetry.INSTANCE.getTelemetry().addData("Target X", llResult.getTx());
            PanelsTelemetry.INSTANCE.getTelemetry().addData("Target Y", llResult.getTy());
            PanelsTelemetry.INSTANCE.getTelemetry().addData("Target Area", llResult.getTa());
            PanelsTelemetry.INSTANCE.getTelemetry().addData("Bot Pose", botpose.toString());
            PanelsTelemetry.INSTANCE.getTelemetry().addData("Yaw", botpose.getOrientation().getYaw());
        }
    }

    public Follower getFollower() {
        return this.follower;
    }
}