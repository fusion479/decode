package org.firstinspires.ftc.teamcode.opmodes.test;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.transfer.TransferAccept;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.utils.commands.OpModeCore;

@TeleOp(name = "Transfer Test")
public class TransferTest extends OpModeCore {
    private Transfer transfer;
    private GamepadEx gamepad;

    @Override
    public void initialize() {
        super.initialize();

        this.gamepad = new GamepadEx(super.gamepad1);
        this.transfer = new Transfer(super.hardwareMap);

        this.gamepad.getGamepadButton(GamepadKeys.Button.B).whileHeld(new TransferAccept(this.transfer));
    }

    @Override
    public void runOpMode() throws InterruptedException {
        this.initialize();
        CommandScheduler.getInstance().enable();

        super.waitForStart();

        while (opModeIsActive()) {
            super.resetCycle();
            CommandScheduler.getInstance().run();

            super.logCycles();
            PanelsTelemetry.INSTANCE.getTelemetry().update();
        }

        super.end();
    }
}
