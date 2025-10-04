package org.firstinspires.ftc.teamcode.utils.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.follower.Follower;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;

import org.firstinspires.ftc.teamcode.CommandRobot;

public class PathChainCommand extends CommandBase {
    private final PathChain pathChain;
    private final double speed;
    private final Follower follower;

    public PathChainCommand(CommandRobot robot, Path... paths) {
        this.pathChain = new PathChain(paths);
        this.speed = 1;
        this.follower = robot.getFollower();
    }

    public PathChainCommand(CommandRobot robot, double speed, Path... paths) {
        this.pathChain = new PathChain(paths);
        this.follower = robot.getFollower();
        this.speed = speed;
    }

    @Override
    public void initialize() {
        follower.setMaxPower(speed);
        follower.followPath(pathChain, true);
    }

    @Override
    public void execute() {
        follower.update();
        follower.getDashboardPoseTracker().update();
    }

    @Override
    public boolean isFinished() {
        return Thread.currentThread().isInterrupted() || !follower.isBusy();
    }

    @Override
    public void end(boolean interrupted) {
        follower.setMaxPower(1);
    }
}