package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Configurable
public class Transfer extends SubsystemBase {
    public static double STOP = 0.28;
    public static double ALLOW = 0.12;
    private final CRServo leftTransfer;
    private final Servo stop;
    private double power;
    private DistanceSensor ballOne, ballTwo;
    // add brakebeam

    public Transfer(final HardwareMap hwMap) {
        this.stop = hwMap.get(Servo.class, "transferStop");

        this.leftTransfer = hwMap.get(CRServo.class, "leftFlicker");

        this.setStopPosiiton(STOP);
    }

    @Override
    public void periodic() {
        this.leftTransfer.setPower(power);
    }

    public double getleftVoltage() {
        return this.leftTransfer.getPower();
    }

    public void setStopPosiiton(double position) {
        this.stop.setPosition(position);
    }

    public void setPower(double power) {
        this.power = power;
    }
}