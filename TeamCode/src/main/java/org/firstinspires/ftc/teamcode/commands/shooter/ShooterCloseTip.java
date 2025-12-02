package org.firstinspires.ftc.teamcode.commands.shooter;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Shooter;

public class ShooterCloseTip extends CommandBase {

    private final Shooter shooter;

    public ShooterCloseTip(final Shooter shooter) {
        this.shooter = shooter;
        super.addRequirements(shooter);
    }

    @Override
    public void initialize() {
        this.shooter.setPosition(Shooter.CLOSE_TIP_POSITION);
        this.shooter.setTarget(Shooter.CLOSE_TIP_VELOCITY);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}