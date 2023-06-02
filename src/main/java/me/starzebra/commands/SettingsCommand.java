package me.starzebra.commands;

import me.starzebra.Insanity;
import me.starzebra.config.ConfigManager;
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
        if(args.length > 0){
            final String name = String.join(" ", args).replaceFirst(args[0] + " ", "");
            final String lowerCase = args[0].toLowerCase();
            switch (lowerCase) {
                case "save": {
                    if (ConfigManager.saveConfig(ConfigManager.configPath + String.format("%s.json", name), true)) {
                        System.out.println("Saved your config!");
                        break;
                    }
                    System.out.println("Saving Failed!");
                    break;
                }
                case "load": {
                    if (ConfigManager.loadConfig(ConfigManager.configPath + String.format("%s.json", name))) {
                        System.out.println("Config loaded!");
                        break;
                    }
                    System.out.println("Loading Failed!");
                    break;
                }
            }
        }else {
            try {
                System.out.println(".config load/save");
                Insanity.clickGui.toggle();
                ConfigManager.openExplorer();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
