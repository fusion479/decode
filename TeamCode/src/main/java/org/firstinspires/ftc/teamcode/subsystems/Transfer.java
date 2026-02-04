package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Configurable
public class Transfer extends SubsystemBase {
    public static double STOP = 0.15;
    public static double ALLOW = 0;
    private final CRServo leftTransfer, rightTransfer, middleTransfer;
    private final Servo stop;
    private double power;
    private boolean intaking;
    private DistanceSensor ballOne, ballTwo;
    // add brakebeam

    public Transfer(final HardwareMap hwMap) {
        this.stop = hwMap.get(Servo.class, "transferStop");

        this.leftTransfer = hwMap.get(CRServo.class, "leftFlicker");
        this.rightTransfer = hwMap.get(CRServo.class, "rightFlicker");
        this.middleTransfer = hwMap.get(CRServo.class, "middleFlicker");

        this.setStopPosiiton(STOP);
    }

    @Override
    public void periodic() {
        this.leftTransfer.setPower(power);
        this.rightTransfer.setPower(-power);
        if (intaking) {
            this.middleTransfer.setPower(-power);
        }
        else {
            this.middleTransfer.setPower(power);
        }
    }

    public double getleftVoltage() {
        return this.leftTransfer.getPower();
    }

    public void setStopPosiiton(double position) {
        this.stop.setPosition(position);
    }

    public void setPower(double power, boolean intaking) {
        this.power = power;
        this.intaking = intaking;
    }
}