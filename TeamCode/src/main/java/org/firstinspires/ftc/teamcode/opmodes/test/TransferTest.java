package org.firstinspires.ftc.teamcode.opmodes.test;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.transfer.TransferAccept;
import org.firstinspires.ftc.teamcode.commands.transfer.TransferAllow;
import org.firstinspires.ftc.teamcode.commands.transfer.TransferStop;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.utils.commands.GamepadTrigger;
import org.firstinspires.ftc.teamcode.utils.commands.OpModeCore;

@TeleOp(name = "Transfer Test")
public class TransferTest extends OpModeCore {
    private Transfer transfer;

    private GamepadTrigger transferAccept, transferReject;

    private GamepadEx gamepad;

    @Override
    public void initialize() {
        super.initialize();

        this.gamepad = new GamepadEx(super.gamepad1);
        this.transfer = new Transfer(super.hardwareMap);

        this.transferAccept = new GamepadTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER, d -> this.transfer.setPower(-d), this.gamepad);
        this.transferReject = new GamepadTrigger(GamepadKeys.Trigger.LEFT_TRIGGER, this.transfer::setPower, this.gamepad);
        this.gamepad.getGamepadButton(GamepadKeys.Button.X).whenPressed(new TransferAllow(this.transfer));
        this.gamepad.getGamepadButton(GamepadKeys.Button.A).whenPressed(new TransferStop(this.transfer));

    }

    @Override
    public void runOpMode() throws InterruptedException {
        this.initialize();
        CommandScheduler.getInstance().enable();

        super.waitForStart();

        while (opModeIsActive()) {
            super.resetCycle();
            CommandScheduler.getInstance().run();

            this.transferAccept.update();
            this.transferReject.update();

            PanelsTelemetry.INSTANCE.getTelemetry().addData("Left Voltage", this.transfer.getleftVoltage());

            super.logCycles();
            PanelsTelemetry.INSTANCE.getTelemetry().update();

        }

        super.end();
    }
}
