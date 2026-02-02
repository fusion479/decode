package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.commands.drivetrain.BlueCloseTip;
import org.firstinspires.ftc.teamcode.commands.drivetrain.BlueFarTip;
import org.firstinspires.ftc.teamcode.commands.drivetrain.BluePark;
import org.firstinspires.ftc.teamcode.commands.drivetrain.RedCloseTip;
import org.firstinspires.ftc.teamcode.commands.drivetrain.RedFarTip;
import org.firstinspires.ftc.teamcode.commands.drivetrain.RedPark;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterCloseTip;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterFarTip;
import org.firstinspires.ftc.teamcode.commands.transfer.TransferAllow;
import org.firstinspires.ftc.teamcode.commands.transfer.TransferStop;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.utils.commands.GamepadTrigger;

public class CommandRobot {
    public static long SHOOT_WAIT = 500;
    private final String color;
    private final Intake intake;
    private final Drivetrain drive;
    private final Shooter shooter;
    private final Transfer transfer;
    private final GamepadEx gamepad1;
    private GamepadEx gamepad2;
    private GamepadTrigger intakeAccept, intakeReject;

    public CommandRobot(HardwareMap hwMap, Gamepad gamepad1, Gamepad gamepad2, String color) {
        this.color = color;

        this.gamepad1 = new GamepadEx(gamepad1);
        this.gamepad2 = new GamepadEx(gamepad2);

        this.drive = new Drivetrain(hwMap, new Pose(72, 72, Math.toRadians(180)), this.gamepad1, "teleop", color);

        this.intake = new Intake(hwMap);
        this.shooter = new Shooter(hwMap);
        this.transfer = new Transfer(hwMap);

        this.intakeAccept = new GamepadTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER, d -> {
            this.intake.setIntakePower(-d * 0.75);
            this.transfer.setPower(d);
            (new TransferStop(transfer)).schedule();
        }, this.gamepad1);

        this.intakeReject = new GamepadTrigger(GamepadKeys.Trigger.LEFT_TRIGGER, d -> {
            this.intake.setOuttakePower(d * 0.75);
            this.transfer.setPower(-d);
            (new TransferStop(transfer)).schedule();
        }, this.gamepad1);

        this.configureControls();
    }

    public CommandRobot(HardwareMap hwMap, Gamepad gamepad1, Pose startPose, String color) {
        this.color = color;

        this.gamepad1 = new GamepadEx(gamepad1);

        this.drive = new Drivetrain(hwMap, startPose, this.gamepad1, "auton", color);
        this.intake = new Intake(hwMap);
        this.shooter = new Shooter(hwMap);
        this.transfer = new Transfer(hwMap);

        this.configureControls();
    }

    public void updateTriggers() {
        this.intakeAccept.update();
        this.intakeReject.update();

    }

    public void update() {
        this.updateTriggers();
    }

    public void configureControls() {
//        this.gamepad1.getGamepadButton(GamepadKeys.Button.Y)
//                .whileHeld(this.holdShoot())
//                .whenReleased(this.releaseShoot());
        this.gamepad1.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(this.ready());
        this.gamepad1.getGamepadButton(GamepadKeys.Button.A)
                .whileHeld(this.goClose())
                .whenReleased(this.releaseShoot());
        this.gamepad1.getGamepadButton(GamepadKeys.Button.B)
                .whileHeld(this.goFar())
                .whenReleased(this.releaseShoot());
        this.gamepad1.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(relocalizeBlueCorner());
        this.gamepad1.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(relocalizeRedCorner());
        this.gamepad1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(relocalizeMid());
        this.gamepad1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whileHeld(this.park());
    }

    public Command relocalizeBlueCorner() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> this.getFollower().setHeading(Math.toRadians(180))),
                new InstantCommand(() -> this.getDrivetrain().getLimelight().updateRobotOrientation(180)),
                new InstantCommand(() -> this.getFollower().setY(3.9)),
                new InstantCommand(() -> this.getFollower().setX(141.4))
        );
    }

    public Command relocalizeRedCorner() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> this.getFollower().setHeading(Math.toRadians(180))),
                new InstantCommand(() -> this.getDrivetrain().getLimelight().updateRobotOrientation(180)),
                new InstantCommand(() -> this.getFollower().setY(3.9)),
                new InstantCommand(() -> this.getFollower().setX(2.6))
        );
    }

    public Command relocalizeMid() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> this.getFollower().setHeading(Math.toRadians(180))),
                new InstantCommand(() -> this.getDrivetrain().getLimelight().updateRobotOrientation(180)),
                new InstantCommand(() -> this.getFollower().setY(3.9)),
                new InstantCommand(() -> this.getFollower().setX(72))
        );
    }


    public Command ready() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> this.shooter.setTarget(Shooter.ROAM_VELOCITY)),
                new TransferStop(this.transfer)
        );
    }

    public Command shoot() {
        return new SequentialCommandGroup(
                new TransferAllow(this.transfer)
        );
    }

    public Command holdShoot() {
        return new SequentialCommandGroup(
                new TransferAllow(this.transfer),
                new InstantCommand(() -> this.transfer.setPower(1)),
                new InstantCommand(() -> this.intake.setIntakePower(-1))
        );
    }

    public Command releaseShoot() {
        return new SequentialCommandGroup(
                new TransferStop(this.transfer),
                new InstantCommand(() -> this.transfer.setPower(0)),
                new InstantCommand(() -> this.intake.setIntakePower(0)),
                new InstantCommand(() -> {
                    if (color.equals("blue")){
                        BlueFarTip.finished = false;
                        BlueCloseTip.finished = false;
                    }
                    else {
                        RedFarTip.finished = false;
                        RedCloseTip.finished = false;
                    }
                })
        );
    }

    public Command goClose() {
        return new SequentialCommandGroup(
                new ShooterCloseTip(this.shooter),
                this.color.equals("blue") ? new BlueCloseTip(this.drive) : new RedCloseTip(this.drive),
                holdShoot()
        );
    }

    public Command autonClose() {
        return new SequentialCommandGroup(
                new ShooterCloseTip(this.shooter)
        );
    }

    public Command autonFar() {
        return new SequentialCommandGroup(
                new ShooterFarTip(this.shooter)
        );
    }

    public Command goFar() {
        return new SequentialCommandGroup(
                new ShooterFarTip(this.shooter),
                this.color.equals("blue") ? new BlueFarTip(this.drive) : new RedFarTip(this.drive),
                holdShoot()
        );
    }

    public Command park() {
        return new SequentialCommandGroup(
                this.color.equals("blue") ? new BluePark(this.drive) : new RedPark(this.drive)
        );
    }

    public Drivetrain getDrivetrain() {
        return this.drive;
    }

    public Transfer getTransfer() {
        return this.transfer;
    }

    public Intake getIntake() {
        return this.intake;
    }

    public Follower getFollower() {
        return this.drive.getFollower();
    }

    public Limelight3A getLimelight() {
        return this.drive.getLimelight();
    }
}
