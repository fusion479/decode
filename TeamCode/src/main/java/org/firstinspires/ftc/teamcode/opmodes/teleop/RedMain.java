package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.CommandRobot;
import org.firstinspires.ftc.teamcode.utils.commands.OpModeCore;

@TeleOp(name = "RedMain")
public class RedMain extends OpModeCore {
    private CommandRobot robot;

    public void initialize() {
        super.initialize();

        this.robot = new CommandRobot(
                super.hardwareMap,
                super.gamepad1,
                super.gamepad2,
                "red"
        );
    }

    @Override
    public void runOpMode() {
        this.initialize();
        super.waitForStart();

        this.robot.getDrivetrain().getFollower().update();
        this.robot.getDrivetrain().getFollower().startTeleopDrive();
        while (!isStopRequested() && opModeIsActive()) {
            super.resetCycle();
            CommandScheduler.getInstance().run();

            this.robot.update();

            super.logCycles();
            super.telemetry.update();
        }

        super.end();
    }
}
