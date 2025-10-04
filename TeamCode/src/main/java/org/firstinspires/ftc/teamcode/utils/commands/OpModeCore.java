package org.firstinspires.ftc.teamcode.utils.commands;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utils.TelemetryCore;

public abstract class OpModeCore extends CommandOpMode {
    private final ElapsedTime period = new ElapsedTime();

    public void logCycles() {
        TelemetryCore.getInstance().addData("Period (Seconds / 1 Cycle): ", this.period.seconds());
        TelemetryCore.getInstance().addData("Frequency (Hz, Cycles / 1 Second ): ", 1 / this.period.seconds());
    }

    @Override
    public void initialize() {
        new TelemetryCore(new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry()));
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
