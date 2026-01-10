package org.firstinspires.ftc.teamcode.opmodes.test;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.commands.drivetrain.RedCloseTip;
import org.firstinspires.ftc.teamcode.commands.drivetrain.RedFarTip;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.utils.commands.OpModeCore;

@TeleOp(name = "Drivetrain Test")
public class DrivetrainTest extends OpModeCore {
    private Drivetrain drive;
    private GamepadEx gamepad;

    private DcMotorEx rightFront, leftFront, rightRear, leftRear;

    @Override
    public void initialize() {
        super.initialize();

        this.gamepad = new GamepadEx(super.gamepad1);
        this.drive = new Drivetrain(super.hardwareMap, new Pose(72, 72, 0), this.gamepad);

        this.rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        this.leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        this.rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");
        this.leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");

        this.gamepad.getGamepadButton(GamepadKeys.Button.A).whileHeld(new RedCloseTip(this.drive));
        this.gamepad.getGamepadButton(GamepadKeys.Button.X).whileHeld(new RedFarTip(this.drive));

        this.gamepad.getGamepadButton(GamepadKeys.Button.DPAD_UP).whileHeld(new InstantCommand(() -> this.rightFront.setPower(1)));
        this.gamepad.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whileHeld(new InstantCommand(() -> this.leftFront.setPower(1)));
        this.gamepad.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whileHeld(new InstantCommand(() -> this.rightRear.setPower(1)));
        this.gamepad.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whileHeld(new InstantCommand(() -> this.leftRear.setPower(1)));
    }

    @Override
    public void runOpMode() throws InterruptedException {
        this.initialize();

        super.waitForStart();

        this.drive.getFollower().update();
        this.drive.getFollower().startTeleopDrive();

        while (opModeIsActive()) {
            super.resetCycle();
            CommandScheduler.getInstance().run();

            PanelsTelemetry.INSTANCE.getTelemetry().addData("Gamepad Left Y", gamepad.getLeftY());
            PanelsTelemetry.INSTANCE.getTelemetry().addData("Gamepad Left X", gamepad.getLeftX());
            PanelsTelemetry.INSTANCE.getTelemetry().addData("Gamepad Right X", gamepad.getRightX());

            super.logCycles();
            PanelsTelemetry.INSTANCE.getTelemetry().update();
        }

        super.end();
    }
}