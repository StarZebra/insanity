package me.starzebra.commands;

import me.starzebra.Insanity;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class SettingsCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "insane";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/insane";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Insanity.clickGui.toggle();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
