package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Configurable
public class Transfer extends SubsystemBase {
    public static double STOP = 0.43;
    public static double ALLOW = 0.65;
    public static double MULTIPLIER = 0.8;
    private final CRServo leftTransfer, rightTransfer;
    private final Servo stop;
    private double power;
    private boolean intaking;
    private DistanceSensor ballOne, ballTwo;
    // add brakebeam

    public Transfer(final HardwareMap hwMap) {
        this.stop = hwMap.get(Servo.class, "transferStop");

        this.leftTransfer = hwMap.get(CRServo.class, "leftFlicker");
        this.rightTransfer = hwMap.get(CRServo.class, "rightFlicker");
        this.setStopPosiiton(STOP);
    }

    @Override
    public void periodic() {
        this.leftTransfer.setPower(power * MULTIPLIER);
        this.rightTransfer.setPower(-power * MULTIPLIER);
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