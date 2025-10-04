package org.firstinspires.ftc.teamcode.utils;
import com.qualcomm.robotcore.util.Range;

public class MotionProfile {
    private double maxAccel;
    private double maxDeAccel;
    private double maxVel;
    private double minVel;
    private double pow = 0.0;
    public MotionProfile(double maxAccel,double maxDeAccel,double maxVel,double minVel){
        this.maxAccel = maxAccel;
        this.maxDeAccel = maxDeAccel;
        this.maxVel = maxVel;
        this.minVel = minVel;
    }
    private double calculateAccel(double check) {
        double rel;

        if (Math.abs(pow) > Math.abs(check))
            rel = Math.min(maxDeAccel, Math.abs(check - pow));
        else rel = Math.min(maxAccel, Math.abs(check - pow));

        return check - pow >= 0 ? Range.clip(rel, this.minVel, this.maxVel) : -Range.clip(rel, this.minVel, this.maxVel);
    }
}

