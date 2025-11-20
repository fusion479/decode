package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake extends SubsystemBase {
    private double power;

    private final DcMotorEx rIntake;
    private final DcMotorEx lIntake;

    public Intake(final HardwareMap hwMap) {
        this.rIntake = hwMap.get(DcMotorEx.class, "rIntake");
        this.lIntake = hwMap.get(DcMotorEx.class, "lIntake");

    }

    @Override
    public void periodic() {
        this.rIntake.setPower(power);
        this.lIntake.setPower(power);
    }

    public void setPower(double power) {
        this.power = power;
    }
}
