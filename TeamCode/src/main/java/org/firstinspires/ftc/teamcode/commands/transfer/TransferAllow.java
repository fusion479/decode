package org.firstinspires.ftc.teamcode.commands.transfer;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Transfer;

public class TransferAllow extends CommandBase {

    private final Transfer transfer;

    public TransferAllow(final Transfer transfer){
        this.transfer = transfer;
    }

    @Override
    public void initialize() {
        this.transfer.setStopPosiiton(Transfer.ALLOW);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
