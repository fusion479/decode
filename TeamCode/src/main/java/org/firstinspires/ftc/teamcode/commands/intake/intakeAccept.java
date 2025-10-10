package org.firstinspires.ftc.teamcode.commands.intake;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Intake;

public class intakeAccept extends CommandBase {
    private final Intake intake;

    public intakeAccept(final Intake intake) {
        this.intake = intake;
        super.addRequirements(intake);
    }

    @Override
    public void initialize() {
        this.intake.setPower(Intake.ACCEPT);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
