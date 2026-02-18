package org.firstinspires.ftc.teamcode.commands.shooter;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Shooter;

public class ShooterClose extends CommandBase {

    private final Shooter shooter;

    public ShooterClose(final Shooter shooter) {
        this.shooter = shooter;
        super.addRequirements(shooter);
    }

    @Override
    public void initialize() {
        this.shooter.setTarget(Shooter.CLOSE_VELOCITY);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}