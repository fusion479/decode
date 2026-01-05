package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Configurable
public class Transfer extends SubsystemBase {
    public static double STOP = 0.5;
    public static double ALLOW = 0.3;

    private double power;

    private CRServo leftTransfer;
    private Servo stop;

    private DistanceSensor ballOne, ballTwo;
    // add brakebeam

    public Transfer(final HardwareMap hwMap) {
        this.stop = hwMap.get(Servo.class, "transferStop");

        this.leftTransfer = hwMap.get(CRServo.class, "leftFlicker");

        this.setStopPosiiton(this.STOP);
    }

    @Override
    public void periodic() {
        this.leftTransfer.setPower(power);
    }

    public double getleftVoltage(){
        return this.leftTransfer.getPower();
    }

    public void setStopPosiiton(double position) {
        this.stop.setPosition(position);
    }

    public void setPower(double power) {
        this.power = power;
    }
}