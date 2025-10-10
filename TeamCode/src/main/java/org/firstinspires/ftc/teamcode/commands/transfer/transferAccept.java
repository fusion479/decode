package org.firstinspires.ftc.teamcode.commands.transfer;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Transfer;

public class transferAccept extends CommandBase {
    private final Transfer transfer;

    public transferAccept(final Transfer transfer) {
        this.transfer = transfer;
        super.addRequirements(transfer);
    }

    @Override
    public void initialize() {
        this.transfer.setPower(Transfer.ACCEPT);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}