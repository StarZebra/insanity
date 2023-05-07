package me.starzebra.commands;

import me.starzebra.features.ItemUpdateFix;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class ItemUpdateFixCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "itemtoggle";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        ItemUpdateFix.toggled = !ItemUpdateFix.toggled;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("[§4I§f] " + (ItemUpdateFix.toggled ? "§r§aEnabled" : "§r§cDisabled")));

    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
