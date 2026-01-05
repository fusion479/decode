package org.firstinspires.ftc.teamcode.commands.intake;

import com.arcrobotics.ftclib.command.CommandBase;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Intake;

public class IntakeAccept extends CommandBase {
    private final Intake intake;
    private final int duration;
    private final ElapsedTime timer;

    public IntakeAccept(final Intake intake, final int duration) {
        this.intake = intake;
        this.duration = duration;
        this.timer = new ElapsedTime();

        super.addRequirements(this.intake);
    }

    @Override
    public void initialize() {
        this.timer.reset();
        this.intake.setIntakePower(1);
    }

    @Override
    public boolean isFinished() {
        if (this.timer.milliseconds() >= this.duration) {
            this.intake.setIntakePower(0);
            return true;
        }
        return false;
    }
}

