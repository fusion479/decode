package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Transfer extends SubsystemBase {

    public static double ACCEPT = 1.0;
    private double power;

    private final DcMotorEx transfer;

    public Transfer(final HardwareMap hwMap) {
        this.transfer = hwMap.get(DcMotorEx.class, "transfer");

    }

    //public void startThread(CommandOpMode opMode) {
    //    new Thread(() -> {
    //        while (opMode.opModeIsActive())
    //            try {
    //                this.transfer.setPower(power)
    //                Thread.sleep(50);
    //            } catch (InterruptedException e) {
    //                StringWriter errors = new StringWriter();
    //                e.printStackTrace(new PrintWriter(errors));
    //                TelemetryCore.getInstance().addLine(errors.toString());
    //            }
    //    }).start();
    //}

    @Override
    public void periodic() {
        this.transfer.setPower(power);
    }

    public void setPower(double power) {
        this.power = power;
    }
}