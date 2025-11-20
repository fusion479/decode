package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Transfer extends SubsystemBase {
    public static double STOP = 0.5;

    private double power;

    private CRServo leftTransfer, rightTransfer;
    private Servo stop;

    private DistanceSensor ballOne, ballTwo;
    // add brakebeam

    public Transfer(final HardwareMap hwMap) {
        this.stop = hwMap.get(Servo.class, "transferStop");

        this.leftTransfer = hwMap.get(CRServo.class, "leftTransfer");
        this.rightTransfer = hwMap.get(CRServo.class, "rightTransfer");
    }

    @Override
    public void periodic() {
        this.leftTransfer.setPower(power);
        this.rightTransfer.setPower(power);
    }

    public void stop() {
        this.stop.setPosition(Transfer.STOP);
    }

    public void setPower(double power) {
        this.power = power;
    }
}