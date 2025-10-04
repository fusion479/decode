package org.firstinspires.ftc.teamcode.utils.hardware;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.ImuOrientationOnRobot;

import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class EnhancedIMU {
    private final IMU imu;
    private YawPitchRollAngles angles;

    public EnhancedIMU(IMU imu, ImuOrientationOnRobot orientation) {
        this.imu = imu;

        this.imu.initialize(new IMU.Parameters(orientation));
        this.imu.resetYaw();
    }

    public void startThread(CommandOpMode opMode) {
        new Thread(() -> {
            while (opMode.opModeIsActive()) {
                try {
                    synchronized (this.imu) {
                        this.angles = this.imu.getRobotYawPitchRollAngles();
                    }
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public YawPitchRollAngles getRobotYawPitchRollAngles() {
        return this.angles;
    }
}
