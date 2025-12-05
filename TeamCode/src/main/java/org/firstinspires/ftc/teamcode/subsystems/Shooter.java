package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.bylazar.configurables.annotations.Configurable;

import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.utils.PIDController;

@Configurable
public class Shooter extends SubsystemBase {
    public static double CLOSE_TIP_VELOCITY = 3000;
    public static double FAR_TIP_VELOCITY = 25000;
    public static double CLOSE_TIP_POSITION = 0.65;
    public static double FAR_TIP_POSITION = 0.7;

    public static double kP = 0.5;
    public static double kI = 0;
    public static double kD = 0;
    public static double kG = 0;

    private final DcMotorEx rightShooter, leftShooter;
    private final Servo hood;

    private final PIDController controller;
    public Shooter(final HardwareMap hwMap) {
        this.rightShooter = hwMap.get(DcMotorEx.class, "rightShooter");
        this.leftShooter = hwMap.get(DcMotorEx.class, "leftShooter");
        this.hood = hwMap.get(Servo.class, "hood");

        this.rightShooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.leftShooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.rightShooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.leftShooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.rightShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.leftShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.rightShooter.setDirection(DcMotorSimple.Direction.REVERSE);
        this.leftShooter.setDirection(DcMotorSimple.Direction.FORWARD);

        this.controller = new PIDController(Shooter.kP, Shooter.kI, Shooter.kD, Shooter.kG);
        this.controller.setAllowedError(15);

        this.setTarget(this.FAR_TIP_VELOCITY);
        this.setPosition(this.FAR_TIP_POSITION);
    }

    public void periodic() {
        double power = this.controller.calculate(rightShooter.getVelocity());

        PanelsTelemetry.INSTANCE.getTelemetry().addData("power", power);

        this.rightShooter.setPower(Math.max(power, 0));
        this.leftShooter.setPower(Math.max(power,0));
    }

    public double getRightVoltage(){
        return this.rightShooter.getPower();
    }

    public double getleftVoltage(){
        return this.leftShooter.getPower();
    }

    public void setPosition(double position) {
        this.hood.setPosition(position);
    }

    public synchronized double getTarget() {
        return this.controller.getTarget();
    }

    public synchronized void setTarget(double target) {
        this.controller.setTarget(target);
    }

    public synchronized double getVelocity() {
        return this.rightShooter.getVelocity();
    }

    public synchronized boolean isFinished() {
        return this.controller.isFinished();
    }

    public synchronized double getError() {
        return this.controller.getLastError();
    }

    public synchronized void setConstants() {
        this.controller.setCoefficients(Shooter.kP, Shooter.kI, Shooter.kD, Shooter.kG);
    }
}