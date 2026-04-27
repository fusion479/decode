package org.firstinspires.ftc.teamcode.opmodes.auton;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.CommandRobot;
import org.firstinspires.ftc.teamcode.opmodes.auton.trajectories.CharlesArcTrajectories;
import org.firstinspires.ftc.teamcode.utils.commands.OpModeCore;
import org.firstinspires.ftc.teamcode.utils.commands.PathCommand;

@Autonomous(name = "CharlesArc")
public class CharlesArc extends OpModeCore {
    public static double NORMAL_SPEED = 0.777;
    private CommandRobot robot;
    private CharlesArcTrajectories trajectories;

    @Override
    public void initialize(){
        this.trajectories = new CharlesArcTrajectories();

        this.robot new CommandRobot(super.hardwareMap, super.gamepad1, this.trajectories.getStart());
    }

    @Override
    public void runOpMode(){
        CommandScheduler.getInstance().enable();
        this.initialize();
        super.waitForStart();

        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new ParallelCommandGroup(
                                new PathCommand(this.robot, this.trajectories.arc1, NORMAL_SPEED)
                        )
                )
        );

        while (opModeIsActive()) {
            CommandScheduler.getInstance().run();
        }

        super.end();
    }
}
