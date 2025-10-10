package org.firstinspires.ftc.teamcode.opmodes.test;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.utils.commands.OpModeCore;
import org.firstinspires.ftc.teamcode.commands.intake.intakeACCEPT;

@TeleOp(name = "Intake Test")
public class IntakeTest extends OpModeCore {
    private Intake intake;
    private GamepadEx gamepad;

    @Override
    public void initialize() {
        super.initialize();

        this.gamepad = new GamepadEx(super.gamepad1);
        this.intake = new Intake(super.hardwareMap);

        this.gamepad.getGamepadButton(GamepadKeys.Button.A).whileHeld(new intakeACCEPT(this.intake));
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
        }

        super.end();
    }
}
