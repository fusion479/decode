package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake extends SubsystemBase {
    private double power;

    private final DcMotorEx rightIntake, leftIntake;
    private final CRServo rightRoller, leftRoller;

    public Intake(final HardwareMap hwMap) {
        this.rightIntake = hwMap.get(DcMotorEx.class, "rightINtake");
        this.leftIntake = hwMap.get(DcMotorEx.class, "lIntake");

        this.leftRoller = hwMap.get(CRServo.class, "leftRoller");
        this.rightRoller = hwMap.get(CRServo.class, "rIntakeRoller");
    }

    @Override
    public void periodic() {
        this.rightIntake.setPower(power);
        this.leftIntake.setPower(power);

        this.rightRoller.setPower(power);
        this.leftRoller.setPower(power);
    }

    public void setPower(double power) {
        this.power = power;
    }
}
