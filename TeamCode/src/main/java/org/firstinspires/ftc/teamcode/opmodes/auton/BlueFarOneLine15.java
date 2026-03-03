package org.firstinspires.ftc.teamcode.opmodes.auton;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.CommandRobot;
import org.firstinspires.ftc.teamcode.commands.transfer.TransferAccept;
import org.firstinspires.ftc.teamcode.opmodes.auton.trajectories.BlueFarOneLine15Trajectories;
import org.firstinspires.ftc.teamcode.opmodes.auton.trajectories.BlueFarOneLineTrajectories;
import org.firstinspires.ftc.teamcode.utils.commands.OpModeCore;
import org.firstinspires.ftc.teamcode.utils.commands.PathCommand;

@Configurable
@Autonomous(name = "Blue Far One Line 15", preselectTeleOp = "BlueMain")
public class BlueFarOneLine15 extends OpModeCore {
    public static double SCORE_SPEED = 0.65;
    public static double NORMAL_SPEED = 0.83;

    public static double HP_SPEED = 0.6;
    public static double INTAKE_SPEED = 1;
    public static int SHOOT_DURATION = 1500;
    public static int INTAKE_DURATION = 1700;
    public static int INTAKE_DURATION_HP = 2500;
    public static int SHOOT_WAIT = 100;

    public static int FIRST_SHOOT_WAIT = 2000;
    public static int INTAKE_HP_WAIT = 0;

    private CommandRobot robot;
    private BlueFarOneLine15Trajectories trajectories;

    @Override
    public void initialize() {
        this.trajectories = new BlueFarOneLine15Trajectories();

        this.robot = new CommandRobot(super.hardwareMap, super.gamepad1, this.trajectories.getStart(), "blue");
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
                                robot.autonFar()
                        ),
                        new WaitCommand(FIRST_SHOOT_WAIT),

                        robot.shoot(),
                        new TransferAccept(this.robot.getIntake(), this.robot.getTransfer(), SHOOT_DURATION),
                        robot.ready(),

                        new ParallelCommandGroup(
                                new TransferAccept(this.robot.getIntake(), this.robot.getTransfer(), INTAKE_DURATION_HP),
                                new SequentialCommandGroup(
                                        new PathCommand(this.robot, this.trajectories.intakeSecond, HP_SPEED),
                                        new WaitCommand(INTAKE_HP_WAIT)
                                )
                        ),

                        new PathCommand(this.robot, this.trajectories.shootSecond, SCORE_SPEED),
                        new WaitCommand(SHOOT_WAIT),
                        robot.shoot(),
                        new TransferAccept(this.robot.getIntake(), this.robot.getTransfer(), SHOOT_DURATION),
                        robot.ready(),

                        new PathCommand(this.robot, this.trajectories.setupThird, NORMAL_SPEED),
                        new ParallelCommandGroup(
                                new TransferAccept(this.robot.getIntake(), this.robot.getTransfer(), INTAKE_DURATION),
                                new PathCommand(this.robot, this.trajectories.intakeThird, INTAKE_SPEED)
                        ),

                        new PathCommand(this.robot, this.trajectories.shootThird, SCORE_SPEED),
                        new WaitCommand(SHOOT_WAIT),
                        robot.shoot(),
                        new TransferAccept(this.robot.getIntake(), this.robot.getTransfer(), SHOOT_DURATION),
                        robot.ready(),

                        new ParallelCommandGroup(
                                new TransferAccept(this.robot.getIntake(), this.robot.getTransfer(), INTAKE_DURATION_HP),
                                new SequentialCommandGroup(
                                        new PathCommand(this.robot, this.trajectories.intakeFourth, INTAKE_SPEED),
                                        new WaitCommand(INTAKE_HP_WAIT)
                                )
                        ),

                        new PathCommand(this.robot, this.trajectories.shootFourth, SCORE_SPEED),
                        new WaitCommand(SHOOT_WAIT),
                        robot.shoot(),
                        new TransferAccept(this.robot.getIntake(), this.robot.getTransfer(), SHOOT_DURATION),
                        robot.ready(),

                        new ParallelCommandGroup(
                                new TransferAccept(this.robot.getIntake(), this.robot.getTransfer(), INTAKE_DURATION_HP),
                                new SequentialCommandGroup(
                                        new PathCommand(this.robot, this.trajectories.intakeFifth, INTAKE_SPEED),
                                        new WaitCommand(INTAKE_HP_WAIT)
                                )
                        ),

                        new PathCommand(this.robot, this.trajectories.shootFifth, SCORE_SPEED),
                        new WaitCommand(SHOOT_WAIT),
                        robot.shoot(),
                        new TransferAccept(this.robot.getIntake(), this.robot.getTransfer(), SHOOT_DURATION),
                        robot.ready(),

                        new PathCommand(this.robot, this.trajectories.park, 1)
                )
        );

        while (opModeIsActive()) {
            CommandScheduler.getInstance().run();
        }

        super.end();
    }
}
