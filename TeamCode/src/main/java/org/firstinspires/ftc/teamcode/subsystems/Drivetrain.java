package org.firstinspires.ftc.teamcode.subsystems;

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
import org.firstinspires.ftc.teamcode.utils.Drawing;

@Configurable
public class Drivetrain extends SubsystemBase {
    public static double MAX_ACCEL = 0.3;
    public static double MAX_ANGULAR_ACCEL = 0.2;

    public static double BLUE_X_OFFSET = -44;
    public static double BLUE_Y_OFFSET = 72;

    public static double RED_X_OFFSET = 66;
    public static double RED_Y_OFFSET = 189;

    public static double MAX_DEACCEL = 0.5;
    public static double MAX_ANGULAR_DEACCEL = 0.5;

    public static double MAX_VEL = 0.75;
    public static double MAX_ANGULAR_VEL = 0.6;

    public static boolean ROBOT_CENTRIC = false;

    public static double AVG_THRESHOLD = 2.3;
    public static double DIST_THRESHOLD = 20;
    public static double ANG_THRESHOLD = 0.5;

    private final Follower follower;

    private final Limelight3A limelight;
    private final GamepadEx gamepad;

    private final String color;
    private final String OpMode;
    private double xPower = 0.0;
    private double angPower = 0.0;
    private double yPower = 0.0;

    public Drivetrain(final HardwareMap hwMap, Pose startPose, GamepadEx gamepad, String OpMode, String color) {
        Drawing.init();

        this.color = color;
        this.OpMode = OpMode;
        this.follower = Constants.createFollower(hwMap);

        this.follower.setStartingPose(startPose);
        this.follower.update();

        this.limelight = hwMap.get(Limelight3A.class, "limelight");
        this.limelight.pipelineSwitch(0);
        this.limelight.setPollRateHz(50);
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
                this.yPower += Drivetrain.calculateAccel(MAX_ACCEL, MAX_DEACCEL, this.yPower, -gamepad.getLeftY());
                this.xPower += Drivetrain.calculateAccel(MAX_ACCEL, MAX_DEACCEL, this.xPower, gamepad.getLeftX());
                this.angPower += Drivetrain.calculateAccel(MAX_ANGULAR_ACCEL, MAX_ANGULAR_DEACCEL, this.angPower, -gamepad.getRightX());
            } else {
                this.yPower += Drivetrain.calculateAccel(MAX_ACCEL, MAX_DEACCEL, this.yPower, gamepad.getLeftY());
                this.xPower += Drivetrain.calculateAccel(MAX_ACCEL, MAX_DEACCEL, this.xPower, -gamepad.getLeftX());
                this.angPower += Drivetrain.calculateAccel(MAX_ANGULAR_ACCEL, MAX_ANGULAR_DEACCEL, this.angPower, -gamepad.getRightX());
            }

            this.follower.update();
            this.follower.setTeleOpDrive(yPower * MAX_VEL, xPower * MAX_VEL, angPower * MAX_ANGULAR_VEL, ROBOT_CENTRIC);
            this.follower.update();

            PanelsTelemetry.INSTANCE.getTelemetry().addData("X: ", this.follower.getPose().getX());
            PanelsTelemetry.INSTANCE.getTelemetry().addData("Y: ", this.follower.getPose().getY());
            PanelsTelemetry.INSTANCE.getTelemetry().addData("Heading: ", this.follower.getPose().getHeading());

            draw();
            if (Math.abs(follower.getAngularVelocity()) < ANG_THRESHOLD) {
                this.relocalize();
            }
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

    public void relocalize() {

        LLResult llResult = limelight.getLatestResult();
        this.limelight.updateRobotOrientation(Math.toDegrees(this.follower.getHeading()));
        if (llResult != null && !follower.isBusy() && llResult.isValid()) {

            int tagId = llResult.getFiducialResults().get(0).getFiducialId();
            if (tagId == 20) {
                Pose3D botpose = llResult.getBotpose_MT2();

                double dX = follower.getPose().getX() - (botpose.getPosition().x * -39.37 + BLUE_X_OFFSET);
                double dY = follower.getPose().getY() - (botpose.getPosition().y * -39.37 + BLUE_Y_OFFSET);
                double dist = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));

                if (dist < DIST_THRESHOLD) {
                    this.follower.setY(botpose.getPosition().y * -39.37 + BLUE_Y_OFFSET);
                    this.follower.setX(botpose.getPosition().x * -39.37 + BLUE_X_OFFSET);
                    this.follower.updatePose();
                }

                PanelsTelemetry.INSTANCE.getTelemetry().addData("Target X", llResult.getTx());
                PanelsTelemetry.INSTANCE.getTelemetry().addData("Target Y", llResult.getTy());
                PanelsTelemetry.INSTANCE.getTelemetry().addData("Target Area", llResult.getTa());
                PanelsTelemetry.INSTANCE.getTelemetry().addData("Bot Pose Avg Dist", llResult.getBotposeAvgDist());
                PanelsTelemetry.INSTANCE.getTelemetry().addData("Bot Pose", botpose.toString());
                PanelsTelemetry.INSTANCE.getTelemetry().addData("Yaw", botpose.getOrientation().getYaw());

                PanelsTelemetry.INSTANCE.getTelemetry().update();
            }

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