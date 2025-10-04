package org.firstinspires.ftc.teamcode.utils.commands;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.util.ElapsedTime;

public abstract class OpModeCore extends CommandOpMode {
    private final ElapsedTime period = new ElapsedTime();

    public void logCycles() {
        PanelsTelemetry.INSTANCE.getTelemetry().addData("Period (Seconds / 1 Cycle): ", this.period.seconds());
        PanelsTelemetry.INSTANCE.getTelemetry().addData("Frequency (Hz, Cycles / 1 Second ): ", 1 / this.period.seconds());
    }

    @Override
    public void initialize() {
        CommandScheduler.getInstance().enable();
        CommandScheduler.getInstance().reset();
    }

    public void end() {
        CommandScheduler.getInstance().cancelAll();
        CommandScheduler.getInstance().disable();
        CommandScheduler.getInstance().reset();
    }

    public void resetCycle() {
        this.period.reset();
    }
}
