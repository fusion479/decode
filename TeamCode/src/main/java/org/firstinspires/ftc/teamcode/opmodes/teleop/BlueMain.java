package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.CommandRobot;
import org.firstinspires.ftc.teamcode.commands.drivetrain.BlueCloseTip;
import org.firstinspires.ftc.teamcode.commands.drivetrain.RedCloseTip;
import org.firstinspires.ftc.teamcode.commands.drivetrain.RedFarTip;
import org.firstinspires.ftc.teamcode.utils.commands.OpModeCore;

@TeleOp(name = "BlueMain")
public class BlueMain extends OpModeCore {
    private CommandRobot robot;

    public void initialize() {
        super.initialize();

        this.robot = new CommandRobot(
                super.hardwareMap,
                super.gamepad1,
                super.gamepad2,
                "blue"
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

            PanelsTelemetry.INSTANCE.getTelemetry().addData("Y", Math.abs(this.robot.getDrivetrain().getFollower().getPose().getY() - BlueCloseTip.tip.getY()) < 6);
            PanelsTelemetry.INSTANCE.getTelemetry().addData("X",Math.abs(this.robot.getDrivetrain().getFollower().getPose().getX() - BlueCloseTip.tip.getX()) < 6);
            PanelsTelemetry.INSTANCE.getTelemetry().addData("Curren Heading", Math.toDegrees(
                    this.robot.getDrivetrain().getFollower().getPose().getHeading()
            )
                    + (Math.toDegrees(
                    this.robot.getDrivetrain().getFollower().getPose().getHeading()
            ) < 0 ? 360 : 0));
            PanelsTelemetry.INSTANCE.getTelemetry().addData("Blue Close Heading", Math.toDegrees(BlueCloseTip.tip.getHeading()));
            PanelsTelemetry.INSTANCE.getTelemetry().addData(
                    "Heading",
                    Math.abs(Math.toDegrees(
                            this.robot.getDrivetrain().getFollower().getPose().getHeading()
                    )
                            + (Math.toDegrees(
                            this.robot.getDrivetrain().getFollower().getPose().getHeading()
                    ) < 0 ? 360 : 0)
                            - Math.toDegrees(BlueCloseTip.tip.getHeading()))
                            < 3
            );

            super.logCycles();
            super.telemetry.update();
        }

        super.end();
    }
}