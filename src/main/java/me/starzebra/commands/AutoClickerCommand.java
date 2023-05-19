package me.starzebra.commands;

import me.starzebra.features.AutoClicker;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class AutoClickerCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "toggleac";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        AutoClicker.enabled = !AutoClicker.enabled;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("[§4I§f] " + (AutoClicker.enabled ? "§r§aEnabled" : "§r§cDisabled")));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
