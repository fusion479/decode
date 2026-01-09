package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.commands.drivetrain.BlueCloseTip;
import org.firstinspires.ftc.teamcode.commands.drivetrain.BlueFarTip;
import org.firstinspires.ftc.teamcode.commands.drivetrain.RedCloseTip;
import org.firstinspires.ftc.teamcode.commands.drivetrain.RedFarTip;
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
    private String color;

    private Intake intake;
    private Drivetrain drive;
    private Shooter shooter;
    private Transfer transfer;

    private GamepadEx gamepad1, gamepad2;
    private GamepadTrigger intakeAccept, intakeReject;

    public CommandRobot(HardwareMap hwMap, Gamepad gamepad1, Gamepad gamepad2, String color) {
        this.color = color;

        this.gamepad1 = new GamepadEx(gamepad1);
        this.gamepad2 = new GamepadEx(gamepad2);

        this.drive = new Drivetrain(hwMap, new Pose(72, 72, 0), this.gamepad1);

        this.intake = new Intake(hwMap);
        this.shooter = new Shooter(hwMap);
        this.transfer = new Transfer(hwMap);

        this.intakeAccept = new GamepadTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER, d -> {this.intake.setIntakePower(-d); this.transfer.setPower(d);}, this.gamepad1);
        this.intakeReject = new GamepadTrigger(GamepadKeys.Trigger.LEFT_TRIGGER, d -> this.intake.setOuttakePower(d), this.gamepad1);

        this.configureControls();
    }

    public CommandRobot(HardwareMap hwMap, Gamepad gamepad1, Pose startPose) {
        this.color = color;

        this.gamepad1 = new GamepadEx(gamepad1);

        this.drive = new Drivetrain(hwMap, startPose, this.gamepad1);
        this.intake = new Intake(hwMap);
        this.shooter = new Shooter(hwMap);
        this.transfer = new Transfer(hwMap);

        this.configureControls();
    }

    public void updateTriggers() {
        this.intakeAccept.update();
        this.intakeReject.update();

    }

    public void update(){
        this.updateTriggers();
    }

    public void configureControls() {
        this.gamepad1.getGamepadButton(GamepadKeys.Button.Y)
                .whenPressed(this.shoot());
        this.gamepad1.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(this.ready());
        this.gamepad1.getGamepadButton(GamepadKeys.Button.A)
                .whileHeld(this.goClose());
        this.gamepad1.getGamepadButton(GamepadKeys.Button.B)
                .whileHeld(this.goFar());
    }

    public Command ready(){
        return new SequentialCommandGroup(
                new InstantCommand(() -> this.shooter.setTarget(Shooter.ROAM_VELOCITY)),
          new TransferStop(this.transfer)
        );
    }

    public Command shoot(){
        return new SequentialCommandGroup(
                new TransferAllow(this.transfer)
        );
    }

    public Command goClose(){
        return new SequentialCommandGroup(
                new ShooterCloseTip(this.shooter),
                this.color.equals("blue") ? new BlueCloseTip(this.drive) : new RedCloseTip(this.drive),
                new WaitCommand(1000),
                new TransferAllow(this.transfer)

                );
    }

    public Command goFar(){
        return new SequentialCommandGroup(
                new ShooterFarTip(this.shooter),
                this.color.equals("blue") ? new BlueFarTip(this.drive) : new RedFarTip(this.drive),
                new TransferAllow(this.transfer)
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
}
