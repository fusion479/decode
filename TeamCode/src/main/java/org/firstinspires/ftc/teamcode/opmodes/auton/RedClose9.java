package org.firstinspires.ftc.teamcode.opmodes.auton;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.CommandRobot;
import org.firstinspires.ftc.teamcode.commands.transfer.TransferAccept;
import org.firstinspires.ftc.teamcode.opmodes.auton.trajectories.BlueCloseTrajectories;
import org.firstinspires.ftc.teamcode.opmodes.auton.trajectories.BlueFarOneLine15Trajectories;
import org.firstinspires.ftc.teamcode.opmodes.auton.trajectories.BlueFarOneLineTrajectories;
import org.firstinspires.ftc.teamcode.opmodes.auton.trajectories.RedClose9Trajectories;
import org.firstinspires.ftc.teamcode.opmodes.auton.trajectories.RedCloseTrajectories;
import org.firstinspires.ftc.teamcode.utils.commands.OpModeCore;
import org.firstinspires.ftc.teamcode.utils.commands.PathCommand;

@Configurable
@Autonomous(name = "Red Close 9", preselectTeleOp = "RedMain")
public class RedClose9 extends OpModeCore {
    public static double SCORE_SPEED = 0.70;
    public static double NORMAL_SPEED = 0.85;
    public static double INTAKE_SPEED = 0.85;
    public static int SHOOT_DURATION = 1500;
    public static int INTAKE_DURATION = 1700;
    public static int SHOOT_WAIT = 100;

    public static int FIRST_SHOOT_WAIT = 1000;

    private CommandRobot robot;
    private RedClose9Trajectories trajectories;

    @Override
    public void initialize() {
        this.trajectories = new RedClose9Trajectories();

        this.robot = new CommandRobot(super.hardwareMap, super.gamepad1, this.trajectories.getStart(), "red");
    }

    @Override
    public void runOpMode() {
        CommandScheduler.getInstance().enable();
        this.initialize();

        super.waitForStart();

        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new ParallelCommandGroup(
                                new PathCommand(this.robot, this.trajectories.shootFirst, SCORE_SPEED),
                                robot.autonClose()
                        ),
                        new WaitCommand(FIRST_SHOOT_WAIT),

                        robot.shoot(),
                        new TransferAccept(this.robot.getIntake(), this.robot.getTransfer(), SHOOT_DURATION),
                        robot.closeready(),

                        new PathCommand(this.robot, this.trajectories.setupSecond, NORMAL_SPEED),
                        new ParallelCommandGroup(
                                new TransferAccept(this.robot.getIntake(), this.robot.getTransfer(), INTAKE_DURATION),
                                new PathCommand(this.robot, this.trajectories.intakeSecond, INTAKE_SPEED)
                        ),

                        new PathCommand(this.robot, this.trajectories.shootSecond, SCORE_SPEED),
                        new WaitCommand(SHOOT_WAIT),
                        robot.shoot(),
                        new TransferAccept(this.robot.getIntake(), this.robot.getTransfer(), SHOOT_DURATION),
                        robot.closeready(),

                        new PathCommand(this.robot, this.trajectories.setupThird, NORMAL_SPEED),
                        new ParallelCommandGroup(
                                new TransferAccept(this.robot.getIntake(), this.robot.getTransfer(), INTAKE_DURATION),
                                new PathCommand(this.robot, this.trajectories.intakeThird, INTAKE_SPEED)
                        ),

                        new PathCommand(this.robot, this.trajectories.shootThird, SCORE_SPEED),
                        new WaitCommand(SHOOT_WAIT),
                        robot.shoot(),
                        new TransferAccept(this.robot.getIntake(), this.robot.getTransfer(), SHOOT_DURATION),
                        robot.closeready(),

                        new PathCommand(this.robot, this.trajectories.park, NORMAL_SPEED)
                )
        );

        while (opModeIsActive()) {
            CommandScheduler.getInstance().run();
        }

        super.end();
    }
}