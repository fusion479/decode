package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.utils.PIDController;

import java.io.PrintWriter;
import java.io.StringWriter;

@Configurable
public class Shooter extends SubsystemBase {
    public static double CLOSE_TIP_VELOCITY = 3;
    public static double FAR_TIP_VELOCITY = 15;

    public static double kP = 0.065;
    public static double kI = 0;
    public static double kD = 0;
    public static double kG = 0;

    private final DcMotorEx rightShooter;
    private final DcMotorEx leftShooter;

    private final PIDController controller;
    public Shooter(final HardwareMap hwMap) {
        this.rightShooter = hwMap.get(DcMotorEx.class, "rightFront");
        this.leftShooter = hwMap.get(DcMotorEx.class, "rightRear");

        this.rightShooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.leftShooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.rightShooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.leftShooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        this.rightShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.leftShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.rightShooter.setDirection(DcMotorSimple.Direction.REVERSE);
        this.leftShooter.setDirection(DcMotorSimple.Direction.FORWARD);

        this.controller = new PIDController(Shooter.kP, Shooter.kI, Shooter.kD, Shooter.kG);
        this.controller.setAllowedError(15);

        this.setTarget(0);
    }
    public void startThread(CommandOpMode opMode) {
        new Thread(() -> {
            while (opMode.opModeIsActive())
                try {
                    double power;

                    power = this.controller.calculate(rightShooter.getVelocity());

//                    this.shooter.setPower(Math.max(power, Shooter.MIN_POWER));

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

        power = this.controller.calculate(rightShooter.getVelocity()) * 1000;

        this.rightShooter.setPower(Math.max(power, 0));
        this.leftShooter.setPower(Math.max(power,0));

    }

    public synchronized double getTarget() {
        return this.controller.getTarget();
    }

//    public synchronized void setMinPower(double min) {
//        Shooter.MIN_POWER = min;
//    }

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