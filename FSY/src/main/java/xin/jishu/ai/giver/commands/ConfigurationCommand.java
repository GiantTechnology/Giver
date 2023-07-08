package xin.jishu.ai.giver.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import xin.jishu.ai.giver.EntryPoint;

import java.time.ZonedDateTime;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
@Description("-")
@CommandAlias("giver")
@CommandPermission("giver.configuration")
public class ConfigurationCommand extends AbstractCommand {

    private static final ConfigurationCommand INSTANCE = new ConfigurationCommand();

    private ConfigurationCommand() {

    }

    @Subcommand("configuration reload")
    public void onConfigurationReload(CommandSender sender) {
        //
        EntryPoint.getInstance()
                .reloadConfig();
        //
        sender.sendMessage(
                ZonedDateTime.now()
                        .toString()
        );
    }

    public static ConfigurationCommand getInstance() {
        return ConfigurationCommand.INSTANCE;
    }

}
