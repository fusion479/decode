package org.firstinspires.ftc.teamcode.commands.transfer;

import com.arcrobotics.ftclib.command.CommandBase;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Transfer;

public class TransferAccept extends CommandBase {
    private final Transfer transfer;
    private final int duration;
    private final ElapsedTime timer;

    public TransferAccept(final Transfer transfer, final int duration) {
        this.transfer = transfer;
        this.duration = duration;
        this.timer = new ElapsedTime();

        super.addRequirements(this.transfer);
    }

    @Override
    public void initialize() {
        this.timer.reset();
        this.transfer.setPower(1);
    }

    @Override
    public boolean isFinished() {
        if (this.timer.milliseconds() >= this.duration) {
            this.transfer.setPower(0);
            return true;
        }
        return false;
    }
}
