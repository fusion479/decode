package org.firstinspires.ftc.teamcode.utils.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.pedroPathing.Follower;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.CommandRobot;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

// add drivetrain subsystem requirement
public class PathCommand extends CommandBase {
    private final Path path;
    private final Follower follower;
    private final double speed;
    private final ElapsedTime timer;

    public PathCommand(CommandRobot robot, Path path) {
        this.path = path;
        this.speed = 1;
        this.timer = new ElapsedTime();
        this.follower = robot.getFollower();

        super.addRequirements(robot.getDrivetrain());
    }

    public PathCommand(Drivetrain drivetrain, Path path, double speed) {
        this.path = path;
        this.timer = new ElapsedTime();
        this.follower = drivetrain.getFollower();
        this.speed = speed;

        super.addRequirements(drivetrain);
    }

    public PathCommand(Drivetrain drivetrain, Path path) {
        this.path = path;
        this.timer = new ElapsedTime();
        this.follower = drivetrain.getFollower();
        this.speed = 1;

        super.addRequirements(drivetrain);
    }

    public PathCommand(CommandRobot robot, Path path, double speed) {
        this.path = path;
        this.timer = new ElapsedTime();
        this.follower = robot.getFollower();
        this.speed = speed;

        super.addRequirements(robot.getDrivetrain());
    }

    @Override
    public void initialize() {
        this.timer.reset();
        this.follower.setMaxPower(speed);
        this.follower.followPath(path, true);
    }

    @Override
    public void execute() {
        this.follower.update();
        this.follower.poseTracker.update();
    }

    @Override
    public boolean isFinished() {
        return Thread.currentThread().isInterrupted() || !this.follower.isBusy() || this.timer.seconds() > path.getPathEndTimeoutConstraint();
    }

    @Override
    public void end(boolean interrupted) {
        this.follower.setMaxPower(1);
    }
}