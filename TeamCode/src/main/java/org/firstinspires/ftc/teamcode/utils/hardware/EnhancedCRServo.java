package org.firstinspires.ftc.teamcode.utils.hardware;

import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.utils.PIDController;

public class EnhancedCRServo {
    private final CRServo servo;
    private final PIDController controller;

    private double position;

    public EnhancedCRServo(CRServo servo) {
        this.servo = servo;

        this.controller = new PIDController(0, 0, 0, 0);
    }

    public void update() {
        this.servo.setPower(this.controller.calculate(this.position));
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public void setPower(double power) {
        this.servo.setPower(power);
    }

    public PIDController getController() {
        return this.controller;
    }
}