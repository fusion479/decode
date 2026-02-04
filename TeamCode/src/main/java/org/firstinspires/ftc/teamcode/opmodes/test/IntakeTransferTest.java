package org.firstinspires.ftc.teamcode.opmodes.test;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.transfer.TransferAccept;
import org.firstinspires.ftc.teamcode.commands.transfer.TransferAllow;
import org.firstinspires.ftc.teamcode.commands.transfer.TransferStop;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.utils.commands.GamepadTrigger;
import org.firstinspires.ftc.teamcode.utils.commands.OpModeCore;

@TeleOp(name = "Intake Transfer Test")
public class IntakeTransferTest extends OpModeCore {
    private Intake intake;
    private Transfer transfer;
    private GamepadEx gamepad;

    private GamepadTrigger intakeAccept, intakeReject;

    @Override
    public void initialize() {
        super.initialize();
        this.gamepad = new GamepadEx(gamepad1);

        this.gamepad = new GamepadEx(super.gamepad1);
        this.intake = new Intake(super.hardwareMap);
        this.transfer = new Transfer(hardwareMap);

        this.intakeAccept = new GamepadTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER, d -> {this.intake.setIntakePower(-d); this.transfer.setPower(d, true);}, this.gamepad);
        this.intakeReject = new GamepadTrigger(GamepadKeys.Trigger.LEFT_TRIGGER, d -> { this.intake.setOuttakePower(d); this.transfer.setPower(-d, true);}, this.gamepad);
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

            this.intakeAccept.update();
            this.intakeReject.update();

            super.logCycles();
            PanelsTelemetry.INSTANCE.getTelemetry().update();
        }

        super.end();
    }
}
