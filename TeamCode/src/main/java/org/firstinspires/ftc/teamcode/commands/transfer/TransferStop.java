package org.firstinspires.ftc.teamcode.commands.transfer;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Transfer;

public class TransferStop extends CommandBase {

    private final Transfer transfer;

    public TransferStop(final Transfer transfer){
        this.transfer = transfer;
    }

    @Override
    public void initialize() {
        this.transfer.setStopPosiiton(Transfer.STOP);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
