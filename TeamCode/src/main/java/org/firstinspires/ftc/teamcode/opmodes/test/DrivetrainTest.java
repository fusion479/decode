package org.firstinspires.ftc.teamcode.opmodes.test;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.utils.commands.OpModeCore;

@TeleOp(name = "Drivetrain Test")
public class DrivetrainTest extends OpModeCore {
    private Drivetrain drive;
    private GamepadEx gamepad;

    @Override
    public void initialize() {
        super.initialize();

        this.gamepad = new GamepadEx(super.gamepad1);
        this.drive = new Drivetrain(super.hardwareMap, new Pose(0, 0, 0), this.gamepad);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        this.initialize();

        super.waitForStart();

        this.drive.getFollower().update();
        this.drive.getFollower().startTeleopDrive();

        this.drive.startThread(this.gamepad, this);
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