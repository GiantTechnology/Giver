package xin.jishu.ai.giver.commands;

import co.aikar.commands.CommandHelp;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
@Description("-")
@CommandAlias("giver")
@CommandPermission("giver.primary")
public class PrimaryCommand extends AbstractCommand {

    private static final PrimaryCommand INSTANCE = new PrimaryCommand();

    private PrimaryCommand() {

    }

    @Default
    public void onDefault(CommandSender sender) {
        this.doHelp(sender);
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    public static PrimaryCommand getInstance() {
        return PrimaryCommand.INSTANCE;
    }

}
