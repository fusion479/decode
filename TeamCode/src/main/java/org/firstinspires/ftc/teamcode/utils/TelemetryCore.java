package org.firstinspires.ftc.teamcode.utils;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

public class TelemetryCore {
    private static MultipleTelemetry instance;

    public TelemetryCore(MultipleTelemetry telemetry) {
        TelemetryCore.instance = telemetry;
    }

    public static synchronized MultipleTelemetry getInstance() {
        return TelemetryCore.instance;
    }
}
