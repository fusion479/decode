package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake extends SubsystemBase {
    private double upperPower;
    private double lowerPower;

    private final DcMotorEx rightIntake, leftIntake;
    private final CRServo rightRoller, leftRoller;

    public Intake(final HardwareMap hwMap) {
        this.rightIntake = hwMap.get(DcMotorEx.class, "rightIntake");
        this.leftIntake = hwMap.get(DcMotorEx.class, "leftIntake");

        this.leftRoller = hwMap.get(CRServo.class, "leftRoller");
        this.rightRoller = hwMap.get(CRServo.class, "rightRoller");
    }

    @Override
    public void periodic() {
        this.rightIntake.setPower(lowerPower);
        this.leftIntake.setPower(-lowerPower);

        this.rightRoller.setPower(-upperPower);
        this.leftRoller.setPower(upperPower);
    }

    public void setIntakePower(double power) {
        this.upperPower = power;
        this.lowerPower = power;

    }

    public void setOuttakePower(double power) {
        this.lowerPower = power;
    }
}
