package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.ParallelRaceGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

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
    private GamepadTrigger intakeAccept, intakeReject, transferAccept, transferReject;

    public CommandRobot(HardwareMap hwMap, Gamepad gamepad1, Gamepad gamepad2, String color) {
        this.color = color;

        this.gamepad1 = new GamepadEx(gamepad1);
        this.gamepad2 = new GamepadEx(gamepad2);

        this.drive = new Drivetrain(hwMap, new Pose(72, 72, Math.toRadians(-90)), this.gamepad1);
        this.intake = new Intake(hwMap);
        this.shooter = new Shooter(hwMap);
        this.transfer = new Transfer(hwMap);

        this.intakeAccept = new GamepadTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER, d -> this.intake.setPower(-d), this.gamepad1);
        this.intakeReject = new GamepadTrigger(GamepadKeys.Trigger.LEFT_TRIGGER, this.intake::setPower, this.gamepad1);

        this.transferAccept = new GamepadTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER, d -> this.transfer.setPower(-d), this.gamepad2);
        this.transferReject = new GamepadTrigger(GamepadKeys.Trigger.LEFT_TRIGGER, this.transfer::setPower, this.gamepad2);

        this.configureControls();
    }

    public void updateTriggers() {
        this.intakeAccept.update();
        this.intakeReject.update();

        this.transferAccept.update();
        this.transferReject.update();
    }

    public void update(){
        this.updateTriggers();
    }

    public void configureControls() {
        this.gamepad2.getGamepadButton(GamepadKeys.Button.A)
                .whileHeld(this.shoot());
        this.gamepad2.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(this.ready());
        this.gamepad1.getGamepadButton(GamepadKeys.Button.A)
                .whileHeld(this.goClose());
        this.gamepad1.getGamepadButton(GamepadKeys.Button.B)
                .whileHeld(this.goFar());
    }

    public Command ready(){
        return new SequentialCommandGroup(
          new ShooterFarTip(this.shooter),
          new TransferStop(this.transfer)
        );
    }

    public Command shoot(){
        return new SequentialCommandGroup(
                new TransferAllow(this.transfer),
                new InstantCommand(() -> this.transfer.setPower(1))
        );
    }

    public Command goClose(){
        return new SequentialCommandGroup(
                this.color.equals("blue") ? new BlueCloseTip(this.drive) : new RedCloseTip(this.drive),
                new ShooterCloseTip(this.shooter),
                new TransferStop(this.transfer)
        );
    }

    public Command goFar(){
        return new SequentialCommandGroup(
                this.color.equals("blue") ? new BlueFarTip(this.drive) : new RedFarTip(this.drive),
                new ShooterCloseTip(this.shooter),
                new TransferStop(this.transfer)
        );
    }

    public Drivetrain getDrivetrain() {
        return this.drive;
    }

    public Follower getFollower() {
        return this.drive.getFollower();
    }
}
