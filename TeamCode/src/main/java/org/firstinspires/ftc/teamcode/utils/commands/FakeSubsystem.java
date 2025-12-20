package org.firstinspires.ftc.teamcode.utils.commands;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.utils.PIDController;

public class FakeSubsystem extends SubsystemBase {

    private final DcMotorEx fakeSubsystem;



    public FakeSubsystem(final HardwareMap hwMap) {
        this.fakeSubsystem = hwMap.get(DcMotorEx.class, "FakeSubsystem");

        this.fakeSubsystem.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.fakeSubsystem.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        this.fakeSubsystem.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.fakeSubsystem.setDirection(DcMotorSimple.Direction.FORWARD);
    }
}