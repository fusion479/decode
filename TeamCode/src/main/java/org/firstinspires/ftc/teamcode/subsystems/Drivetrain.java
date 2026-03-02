package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.panels.Panels;
import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.utils.Drawing;

import java.util.ArrayList;

@Configurable
public class Drivetrain extends SubsystemBase {
    public static double MAX_ACCEL = 1;
    public static double MAX_ANGULAR_ACCEL = 0.3;

    public static double MAX_DEACCEL = 0.5;
    public static double MAX_ANGULAR_DEACCEL = 0.6;

    public static double MAX_VEL = 1;
    public static double MAX_ANGULAR_VEL = 0.7;

    public static boolean ROBOT_CENTRIC = false;

    public static double VEL_THRESHOLD = 18;
    public static double DIST_THRESHOLD = 20;
    public static double ANG_VEL_THRESHOLD = 0.75;
    public static double ANG_THRESHOLD = 25;
    public static double TAG_DIST_THRESHOLD = 2.6;
    public static double YAW_OFFSET = 0;

    private final Follower follower;

    private final Limelight3A limelight;
    private final GamepadEx gamepad;

    private final String color;
    private final String OpMode;
    private double xPower = 0.0;
    private double angPower = 0.0;
    private double yPower = 0.0;

    private ArrayList<Pose> poses = new ArrayList<Pose>();
    private int posesI = 0;
    private int relocalizes = 0;

    public Drivetrain(final HardwareMap hwMap, Pose startPose, GamepadEx gamepad, String OpMode, String color) {
        Drawing.init();

        this.color = color;
        this.OpMode = OpMode;
        this.follower = Constants.createFollower(hwMap);

        this.follower.setStartingPose(startPose);
        this.follower.update();

        this.limelight = hwMap.get(Limelight3A.class, "limelight");
        this.limelight.pipelineSwitch(0);
        this.limelight.setPollRateHz(100);
        this.limelight.start();

        drawOnlyCurrent();

        this.gamepad = gamepad;
    }

    private static double calculateAccel(double accel, double deaccel, double prevPower, double check) {
        double rel;

        if (Math.abs(prevPower) > Math.abs(check))
            rel = Math.min(deaccel, Math.abs(check - prevPower));
        else rel = Math.min(accel, Math.abs(check - prevPower));

        return check - prevPower >= 0 ? rel : -rel;
    }

    @Override
    public void periodic() {
        if (OpMode.equals("teleop")) {
            if (color.equals("red")) {
                this.yPower += Drivetrain.calculateAccel(MAX_ACCEL, MAX_DEACCEL, this.yPower, gamepad.getLeftY());
                this.xPower += Drivetrain.calculateAccel(MAX_ACCEL, MAX_DEACCEL, this.xPower, -gamepad.getLeftX());
                this.angPower += Drivetrain.calculateAccel(MAX_ANGULAR_ACCEL, MAX_ANGULAR_DEACCEL, this.angPower, -gamepad.getRightX());
            } else {
                this.yPower += Drivetrain.calculateAccel(MAX_ACCEL, MAX_DEACCEL, this.yPower, -gamepad.getLeftY());
                this.xPower += Drivetrain.calculateAccel(MAX_ACCEL, MAX_DEACCEL, this.xPower, gamepad.getLeftX());
                this.angPower += Drivetrain.calculateAccel(MAX_ANGULAR_ACCEL, MAX_ANGULAR_DEACCEL, this.angPower, -gamepad.getRightX());
            }

            this.follower.update();
            this.follower.setTeleOpDrive(yPower * MAX_VEL, xPower * MAX_VEL, angPower * MAX_ANGULAR_VEL, ROBOT_CENTRIC);
            this.follower.update();

            PanelsTelemetry.INSTANCE.getTelemetry().addData("X: ", this.follower.getPose().getX());
            PanelsTelemetry.INSTANCE.getTelemetry().addData("Y: ", this.follower.getPose().getY());
            PanelsTelemetry.INSTANCE.getTelemetry().addData("Heading: ", this.follower.getPose().getHeading());

            draw();

            this.relocalize();
        }
    }

//    public void startThread(final GamepadEx gamepad, CommandOpMode opMode) {
//        new Thread(() -> {
//            this.follower.startTeleopDrive();
//
//            while (opMode.opModeIsActive())
//                try {
//                    synchronized (this.follower) {
//                        this.yPower += Drivetrain.calculateAccel(MAX_ACCEL, MAX_DEACCEL, this.yPower, gamepad.getLeftY());
//                        this.xPower += Drivetrain.calculateAccel(MAX_ACCEL, MAX_DEACCEL, this.xPower, -gamepad.getLeftX());
//                        this.angPower += Drivetrain.calculateAccel(MAX_ANGULAR_ACCEL, MAX_ANGULAR_DEACCEL, this.angPower, gamepad.getRightX());
//
//                        this.follower.update();
//                        this.follower.setTeleOpDrive(yPower * MAX_VEL, xPower * MAX_VEL, angPower * MAX_ANGULAR_VEL, ROBOT_CENTRIC);
//                        this.follower.update();
//
//                        PanelsTelemetry.INSTANCE.getTelemetry().addData("X: ", this.follower.getPose().getX());
//                        PanelsTelemetry.INSTANCE.getTelemetry().addData("Y: ", this.follower.getPose().getY());
//                        PanelsTelemetry.INSTANCE.getTelemetry().addData("Heading: ", this.follower.getPose().getHeading());
//
//                        draw();
//
//                        this.relocalize();
//                    }
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    StringWriter errors = new StringWriter();
//                    e.printStackTrace(new PrintWriter(errors));
//                    PanelsTelemetry.INSTANCE.getTelemetry().addData("Error", errors.toString());
//                }
//        }).start();
//    }

    private Pose weightedPose() {
        int n = poses.size();
        int N = Math.min(n, 7);
        double sumX = 0.0, sumY = 0.0, yaw = 0.0;

        for (int i = n - N; i < n; i++) {
            Pose p = poses.get(i);
            sumX += p.getX();
            sumY += p.getY();
            yaw += p.getHeading();
        }

        return new Pose(sumX / N, sumY / N, yaw / N); // in limelight coords
    }

    public void relocalize() {
        if (Math.abs(follower.getAngularVelocity()) < ANG_VEL_THRESHOLD && Math.abs(follower.getVelocity().getMagnitude()) < VEL_THRESHOLD) {
            LLResult llResult = limelight.getLatestResult();
            this.limelight.updateRobotOrientation(Math.toDegrees(this.follower.getHeading()));

            if (llResult != null && llResult.isValid()) {
                Pose3D botpose = llResult.getBotpose_MT2();
                double yaw = llResult.getBotpose().getOrientation().getYaw();

                if (poses.size() > 12) {
                    Pose weighted = weightedPose();
                    double x = weighted.getY() * 39.37 + 72; // y and x are flipped
                    double y = weighted.getX() * -39.37 + 72;
                    double dist = Math.hypot(follower.getPose().getX() - x, follower.getPose().getY() - y);

                    if (dist < DIST_THRESHOLD && Math.abs(Math.abs(weightedPose().getHeading()) - this.follower.getHeading()) < ANG_THRESHOLD) {
                        this.follower.setX(x);
                        this.follower.setY(y);
                        if (llResult.getBotposeAvgDist() < TAG_DIST_THRESHOLD)
                            this.follower.setHeading(weightedPose().getHeading() + YAW_OFFSET) ;
                        this.follower.updatePose();

                        relocalizes++;
                        PanelsTelemetry.INSTANCE.getTelemetry().addData("RELOCALIZING", relocalizes);
                    } else {
                        PanelsTelemetry.INSTANCE.getTelemetry().addLine("DIST THRESHOLD");
                    }
                }

//                if (poses.size() != 100)  poses.add(new Pose(botpose.getPosition().x, botpose.getPosition().y));
//                else {
//                    poses.set(posesI, new Pose(botpose.getPosition().x, botpose.getPosition().y));
//                    posesI = (posesI + 1) % 100;
//                }

                poses.add(new Pose(botpose.getPosition().x, botpose.getPosition().y, Math.toRadians(yaw)));
            } else {
                PanelsTelemetry.INSTANCE.getTelemetry().addLine("ANG THRESHOLD OR VEL THRESHOLD");
            }
        }

        else {
            poses.clear();
            posesI = 0;
        }
    }

    public void drawOnlyCurrent() {
        try {
            Drawing.drawRobot(follower.getPose());
            Drawing.sendPacket();
        } catch (Exception e) {
            throw new RuntimeException("Drawing failed " + e);
        }
    }

    public void draw() {
        Drawing.drawDebug(follower);
    }

    public Follower getFollower() {
        return this.follower;
    }

    public Limelight3A getLimelight() {
        return limelight;
    }
}