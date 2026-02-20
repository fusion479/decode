package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Configurable
public class Brake extends SubsystemBase {
        public static double BRAKE_POSITION = 0.5;
        public static double UNBRAKE_POSITION = 0.0;

        private final Servo brakeServo;

        public Brake(final HardwareMap hwMap) {
            this.brakeServo = hwMap.get(Servo.class, "brakeServo");
            this.setBrakePosition(UNBRAKE_POSITION);
        }

        public void setBrakePosition(double position) {
            this.brakeServo.setPosition(position);
        }
}
