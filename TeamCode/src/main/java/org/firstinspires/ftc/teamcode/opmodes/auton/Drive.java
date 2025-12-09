package org.firstinspires.ftc.teamcode.opmodes.auton;

import static org.firstinspires.ftc.teamcode.utils.AutonomousHelpers.buildLine;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.CommandRobot;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeAccept;
import org.firstinspires.ftc.teamcode.commands.transfer.TransferAccept;
import org.firstinspires.ftc.teamcode.opmodes.auton.trajectories.BlueFarTrajectories;
import org.firstinspires.ftc.teamcode.utils.AutonomousHelpers;
import org.firstinspires.ftc.teamcode.utils.commands.OpModeCore;
import org.firstinspires.ftc.teamcode.utils.commands.PathCommand;

@Autonomous(name = "Drive", preselectTeleOp = "BlueMain")
public class Drive extends OpModeCore {

    public CommandRobot robot;
    public Path drive;
    public void initialize() {
        this.drive = buildLine(
                new Pose(0, 0, 90),
                new Pose(0, 10, 90),
                AutonomousHelpers.HeadingInterpolation.LINEAR
        );

        this.robot = new CommandRobot(super.hardwareMap, super.gamepad1, new Pose(0, 0, 90));
    }

    @Override
    public void runOpMode() {
        CommandScheduler.getInstance().enable();
        this.initialize();

        super.waitForStart();

        CommandScheduler.getInstance().schedule(
                new PathCommand(this.robot, drive)
        );

        while (opModeIsActive()) {
            CommandScheduler.getInstance().run();
        }

        super.end();
    }


}
