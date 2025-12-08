package org.firstinspires.ftc.teamcode.opmodes.auton;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.CommandRobot;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeAccept;
import org.firstinspires.ftc.teamcode.commands.transfer.TransferAccept;
import org.firstinspires.ftc.teamcode.opmodes.auton.trajectories.BlueFarTrajectories;
import org.firstinspires.ftc.teamcode.utils.commands.OpModeCore;
import org.firstinspires.ftc.teamcode.utils.commands.PathCommand;

@Configurable
@Autonomous(name = "Forward", preselectTeleOp = "BlueMain")
public class Forward extends OpModeCore {
    private MotorEx rightFront, leftFront, rightRear, leftRear;
    private static ElapsedTime timer = new ElapsedTime();

    @Override
    public void initialize() {
        this.rightFront = hardwareMap.get(MotorEx.class, "rightFront");
        this.leftFront = hardwareMap.get(MotorEx.class, "leftFront");
        this.rightRear = hardwareMap.get(MotorEx.class, "rightRear");
        this.leftRear = hardwareMap.get(MotorEx.class, "leftRear");

        this.rightFront.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        this.leftFront.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        this.rightRear.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        this.leftRear.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void runOpMode() {
        this.initialize();
        super.waitForStart();
        timer.reset();

        while (opModeIsActive()) {
            if (timer.seconds() < 2) {
                rightFront.set(0.5);
                leftFront.set(0.5);
                rightRear.set(0.5);
                leftRear.set(0.5);
            }
        }

        this.leftFront.set(0);
        this.rightFront.set(0);
        this.leftRear.set(0);
        this.rightRear.set(0);
    }
}