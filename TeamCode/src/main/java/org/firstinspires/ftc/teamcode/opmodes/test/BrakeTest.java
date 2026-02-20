package org.firstinspires.ftc.teamcode.opmodes.test;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.Brake;
import org.firstinspires.ftc.teamcode.utils.commands.OpModeCore;

@TeleOp(name = "Brake Test")
public class BrakeTest extends OpModeCore {
    private Brake brake;
    private GamepadEx gamepad;

    @Override
    public void initialize() {
        super.initialize();

        this.gamepad = new GamepadEx(super.gamepad1);
        this.brake = new Brake(super.hardwareMap);

        this.gamepad.getGamepadButton(GamepadKeys.Button.X).whenPressed(new InstantCommand(() -> this.brake.setBrakePosition(Brake.BRAKE_POSITION)));
        this.gamepad.getGamepadButton(GamepadKeys.Button.A).whenPressed(new InstantCommand(() -> this.brake.setBrakePosition(Brake.UNBRAKE_POSITION)));

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
