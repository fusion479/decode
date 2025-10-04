package org.firstinspires.ftc.teamcode.utils.commands;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import java.util.function.DoubleConsumer;

public class GamepadTrigger {
    private final GamepadKeys.Trigger trigger;
    private final DoubleConsumer command;
    private final GamepadEx gamepad;
    private boolean isReleased = true;

    public GamepadTrigger(GamepadKeys.Trigger trigger, DoubleConsumer command, GamepadEx gamepad) {
        this.trigger = trigger;
        this.command = command;
        this.gamepad = gamepad;
    }

    public void update() {
        if (!isReleased) {
            this.isReleased = true;
            command.accept(0);
        }

        if (this.gamepad.getTrigger(this.trigger) > 0) {
            this.isReleased = false;
            command.accept(this.gamepad.getTrigger(this.trigger));
        }
    }

    public void startThread(CommandOpMode opMode) {
        new Thread(() -> {
            try {
                while (opMode.opModeIsActive()) {
                    synchronized (this) {
                        if (!isReleased) {
                            this.isReleased = true;
                            command.accept(0);
                        }

                        if (this.gamepad.getTrigger(this.trigger) > 0) {
                            this.isReleased = false;
                            command.accept(this.gamepad.getTrigger(this.trigger));
                        }
                    }
                    Thread.sleep(50);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}