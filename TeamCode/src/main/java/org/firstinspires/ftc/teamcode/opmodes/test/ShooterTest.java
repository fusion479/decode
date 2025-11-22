package org.firstinspires.ftc.teamcode.opmodes.test;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.shooter.ShooterCloseTip;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterFarTip;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.utils.commands.OpModeCore;

@TeleOp(name = "Shooter Test")
public class ShooterTest extends OpModeCore {
    private Shooter shooter;
    private GamepadEx gamepad;

    @Override
    public void initialize() {
        super.initialize();

        this.gamepad = new GamepadEx(super.gamepad1);
        this.shooter = new Shooter(super.hardwareMap);

        this.gamepad.getGamepadButton(GamepadKeys.Button.A).whenPressed(new ShooterCloseTip(this.shooter));
        this.gamepad.getGamepadButton(GamepadKeys.Button.B).whenPressed(new ShooterFarTip(this.shooter));
    }

    @Override
    public void runOpMode() throws InterruptedException {
        this.initialize();
        CommandScheduler.getInstance().enable();

        super.waitForStart();

        while (opModeIsActive()) {
            super.resetCycle();
            CommandScheduler.getInstance().run();

            PanelsTelemetry.INSTANCE.getTelemetry().debug("Target", this.shooter.getTarget());
            PanelsTelemetry.INSTANCE.getTelemetry().debug("Velocity", this.shooter.getVelocity());
            PanelsTelemetry.INSTANCE.getTelemetry().debug("Error", this.shooter.getError());
            PanelsTelemetry.INSTANCE.getTelemetry().update();

            super.logCycles();
        }

        super.end();
    }
}