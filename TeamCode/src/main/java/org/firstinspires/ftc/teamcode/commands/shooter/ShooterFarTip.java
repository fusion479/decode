package org.firstinspires.ftc.teamcode.commands.shooter;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Shooter;

public class ShooterFarTip extends CommandBase {

    private final Shooter shooter;

    public ShooterFarTip(final Shooter shooter) {
        this.shooter = shooter;

        super.addRequirements(shooter);
    }

    @Override
    public void initialize() {
        this.shooter.setTarget(Shooter.FAR_TIP_VELOCITY);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}