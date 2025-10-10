package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.utils.PIDController;

import java.io.PrintWriter;
import java.io.StringWriter;
public class Shooter extends SubsystemBase {
    public static double CLOSE_TIP_VELOCITY = 10;
    public static double FAR_TIP_VELOCITY = 15;

    public static double kP = 0.0055;
    public static double kI = 0;
    public static double kD = 0;
    public static double kG = 0;

    private final DcMotorEx shooter;

    private final PIDController controller;
    public Shooter(final HardwareMap hwMap) {
        this.shooter = hwMap.get(DcMotorEx.class, "shooter");

        this.shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        this.shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        this.shooter.setDirection(DcMotorSimple.Direction.REVERSE);

        this.controller = new PIDController(Shooter.kP, Shooter.kI, Shooter.kD, Shooter.kG);
        this.controller.setAllowedError(15);

        this.setTarget(0);
    }
    public void startThread(CommandOpMode opMode) {
        new Thread(() -> {
            while (opMode.opModeIsActive())
                try {
                    double power;

                    power = this.controller.calculate(shooter.getVelocity());
                    this.shooter.setPower(Math.max(power, Shooter.MIN_POWER));

                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));
                    //TelemetryCore.getInstance().addLine(errors.toString());
                }
        }).start();
    }

    public void periodic() {
        double power;

        power = this.controller.calculate(shooter.getVelocity());
        this.shooter.setPower(Math.max(power, Shooter.MIN_POWER));
    }

    public synchronized double getTarget() {
        return this.controller.getTarget();
    }

    public synchronized void setMinPower(double min) {
        Shooter.MIN_POWER = min;
    }

    public synchronized void setTarget(double target) {
        this.controller.setTarget(target);
    }

    public synchronized double getVelocity() {
        return this.shooter.getVelocity();
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
