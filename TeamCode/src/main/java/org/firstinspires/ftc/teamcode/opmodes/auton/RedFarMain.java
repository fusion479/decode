package org.firstinspires.ftc.teamcode.opmodes.auton;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.CommandRobot;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeAccept;
import org.firstinspires.ftc.teamcode.commands.transfer.TransferAccept;
import org.firstinspires.ftc.teamcode.opmodes.auton.trajectories.RedFarTrajectories;
import org.firstinspires.ftc.teamcode.utils.commands.OpModeCore;
import org.firstinspires.ftc.teamcode.utils.commands.PathCommand;

@Configurable
@Autonomous(name = "RedFarMain", preselectTeleOp = "RedMain")
public class RedFarMain extends OpModeCore {
    private CommandRobot robot;
    private RedFarTrajectories trajectories;

    public static double SCORE_SPEED = 1;
    public static double NORMAL_SPEED = 1;

    @Override
    public void initialize() {
        this.trajectories = new RedFarTrajectories();

        this.robot = new CommandRobot(super.hardwareMap, super.gamepad1, this.trajectories.getStart());
    }

    @Override
    public void runOpMode() {
        CommandScheduler.getInstance().enable();
        this.initialize();

        super.waitForStart();

        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new PathCommand(this.robot, this.trajectories.shootFirst, SCORE_SPEED),
                        robot.shoot(),
                        new TransferAccept(this.robot.getTransfer(), 1000),
                        robot.ready(),

                        new PathCommand(this.robot, this.trajectories.setupSecond, SCORE_SPEED),
                        new ParallelCommandGroup(
                                new IntakeAccept(this.robot.getIntake(), 5000),
                                new PathCommand(this.robot, this.trajectories.intakeSecond, NORMAL_SPEED)
                        ),

                        new PathCommand(this.robot, this.trajectories.shootSecond, SCORE_SPEED),
                        robot.shoot(),
                        new TransferAccept(this.robot.getTransfer(), 1000),
                        robot.ready(),

                        new PathCommand(this.robot, this.trajectories.setupThird, NORMAL_SPEED),
                        new ParallelCommandGroup(
                                new IntakeAccept(this.robot.getIntake(), 5000),
                                new PathCommand(this.robot, this.trajectories.intakeSecond, NORMAL_SPEED)
                        ),

                        new PathCommand(this.robot, this.trajectories.shootThird, NORMAL_SPEED),
                        robot.shoot(),
                        new TransferAccept(this.robot.getTransfer(), 1000),
                        robot.ready()
                )
        );

        while (opModeIsActive()) {
            CommandScheduler.getInstance().run();
        }

        super.end();
    }
}