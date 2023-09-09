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
        return "/setrot <yaw> <pitch> <setspawn>";
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

        RotationUtils.Rotation rotation = new RotationUtils.Rotation(newPitch, newYaw);

        RotationUtils.smartLook(rotation, 10, () -> {});

        DecimalFormat df = new DecimalFormat("#.#");

        player.addChatMessage(new ChatComponentText("§f[§4I§f] Yaw: " + newYaw + " Pitch: " + df.format(newPitch)));

        if(args.length <= 2) return;
        String willSetSpawn = args[2];
        if(willSetSpawn == null) return;
        if (willSetSpawn.equals("t")) {
            new Thread(() -> {
                try {
                    Thread.sleep(600);
                    player.sendChatMessage("/setspawn");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();

        }

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
