package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.utils.PIDController;

@Configurable
public class Shooter extends SubsystemBase {
    public static double VERY_CLOSE = 0.5;
    public static double CLOSE = 1.0;

    public static double CLOSE_TIP_VELOCITY = 1530;
    public static double FAR_TIP_VELOCITY = 1915;
    public static double ROAM_VELOCITY = 1915;
    public static double CLOSE_VELOCITY = 1530;
    public static double CLOSE_HORI_VELOCITY = 1450;
    public static double COMPENSATE = 12.0;

    public static double kP = 1.05;
    public static double kI = 0;
    public static double kD = 0.03;
    public static double kF = 0.02;
    public static double kS = 0;

    private final DcMotorEx rightShooter, leftShooter;
    private final VoltageSensor voltageSensor;
    private final PIDController controller;

    public Shooter(final HardwareMap hwMap) {
        this.voltageSensor = hwMap.get(VoltageSensor.class, "Control Hub");

        this.rightShooter = hwMap.get(DcMotorEx.class, "rightShooter");
        this.leftShooter = hwMap.get(DcMotorEx.class, "leftShooter");

        this.rightShooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.leftShooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.rightShooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.leftShooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.rightShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.leftShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.rightShooter.setDirection(DcMotorSimple.Direction.REVERSE);
        this.leftShooter.setDirection(DcMotorSimple.Direction.FORWARD);

        this.controller = new PIDController(Shooter.kP, Shooter.kI, Shooter.kD, Shooter.kF);
        this.controller.setAllowedError(0);

        this.setTarget(ROAM_VELOCITY);
    }

    public void periodic() {
        double power = this.controller.calculate(rightShooter.getVelocity()) + kS * (COMPENSATE / voltageSensor.getVoltage());

        PanelsTelemetry.INSTANCE.getTelemetry().addData("Target", this.controller.getTarget());
        PanelsTelemetry.INSTANCE.getTelemetry().addData("Velocity", this.getVelocity());
        PanelsTelemetry.INSTANCE.getTelemetry().addData("Error", this.controller.getLastError());

        PanelsTelemetry.INSTANCE.getTelemetry().addData("Amps", this.getAmps());
        PanelsTelemetry.INSTANCE.getTelemetry().update();

        this.rightShooter.setPower(Math.max(power, 0));
        this.leftShooter.setPower(Math.max(power, 0));

        this.controller.setCoefficients(kP, kI, kD, kF);
    }

    public double getRightVoltage() {
        return this.rightShooter.getPower();
    }

    public double getleftVoltage() {
        return this.leftShooter.getPower();
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

    public synchronized double getAmps() {
        return this.rightShooter.getCurrent(CurrentUnit.AMPS);
    }

    public synchronized boolean isFinished() {
        return this.controller.isFinished();
    }

    public synchronized double getError() {
        return this.controller.getLastError();
    }

    public synchronized void setConstants() {
        this.controller.setCoefficients(Shooter.kP, Shooter.kI, Shooter.kD, Shooter.kF);
    }

    public double interpolatedPower(double dist) {
        if (dist < VERY_CLOSE) return CLOSE_TIP_VELOCITY;
        else if (dist < CLOSE) return CLOSE_VELOCITY;
        else return FAR_TIP_VELOCITY;
    }
}