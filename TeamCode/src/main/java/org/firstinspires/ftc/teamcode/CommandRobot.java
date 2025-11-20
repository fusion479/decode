package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.utils.commands.GamepadTrigger;

public class CommandRobot {
    private Intake intake;
    private Drivetrain drive;
    private Shooter shooter;
    private Transfer transfer;

    private GamepadEx gamepad1, gamepad2;;
    private GamepadTrigger intakeAccept, intakeReject;

    public CommandRobot(HardwareMap hwMap, Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1 = new GamepadEx(gamepad1);
        this.gamepad2 = new GamepadEx(gamepad2);

        this.intakeAccept = new GamepadTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER, d -> this.intake.setPower(-d), this.gamepad2);
        this.intakeReject = new GamepadTrigger(GamepadKeys.Trigger.LEFT_TRIGGER, this.intake::setPower, this.gamepad2);

        this.drive = new Drivetrain(hwMap, new Pose(0, 0, 0), this.gamepad1);
        this.intake = new Intake(hwMap);
        this.shooter = new Shooter(hwMap);
        this.transfer = new Transfer(hwMap);

        this.configureControls();
    }

    public void configureControls() {
        // ...
    }

    public Drivetrain getDrivetrain() {
        return this.drive;
    }
}
