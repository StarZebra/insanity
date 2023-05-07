package me.starzebra.commands;

import me.starzebra.utils.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.text.DecimalFormat;
import java.util.List;

public class SetRotationCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "setrot";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/setrot <yaw> <pitch>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 0) return;

        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        float currentYaw = player.rotationYaw;
        float currentPitch = player.rotationPitch;
        String yaw = args[0];

        if(yaw.matches("[a-z]+")) return;
        float newYaw = Float.parseFloat(yaw);
        float newPitch = player.rotationPitch;

        if(args.length > 1){
            String pitch = args[1];
            if(pitch.matches("[a-z]+")) return;

            newPitch = Float.parseFloat(pitch);

        }

        if(currentYaw == newYaw && currentPitch == newPitch){
            player.addChatMessage(new ChatComponentText("§f[§4I§f]§c This is already your rotation!"));
            return;
        }

        if(Math.ceil(Math.abs(newYaw)) + Math.ceil(Math.abs(player.rotationYaw)) == 180 || Math.ceil(Math.abs(player.rotationPitch)) + Math.ceil(Math.abs(newPitch)) == 180) {
            player.addChatMessage(new ChatComponentText("§f[§4I§f]§c You can't rotate 180 degrees!"));
            return;
        }

        RotationUtils.Rotation rotation = new RotationUtils.Rotation(newPitch, newYaw);

        RotationUtils.smartLook(rotation, 7, () -> {});

        DecimalFormat df = new DecimalFormat("#.#");

        player.addChatMessage(new ChatComponentText("§f[§4I§f] Yaw: " + newYaw + " Pitch: " + df.format(newPitch)));

    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }
}
