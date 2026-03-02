package org.firstinspires.ftc.teamcode.commands.shooter;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Shooter;

public class ShooterInterpolated extends CommandBase {

    private final Shooter shooter;
    private final double dist;

    public ShooterInterpolated(final Shooter shooter, double dist) {
        this.shooter = shooter;
        this.dist = dist;
        super.addRequirements(shooter);
    }

    @Override
    public void initialize() {
        this.shooter.setTarget(shooter.interpolatedPower(dist));
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}