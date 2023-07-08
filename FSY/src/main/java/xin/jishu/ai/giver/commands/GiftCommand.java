package xin.jishu.ai.giver.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;
import xin.jishu.ai.giver.listeners.InteractionListener;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
@Description("-")
@CommandAlias("giver")
@CommandPermission("giver.gift")
public class GiftCommand extends AbstractCommand {

    private static final GiftCommand INSTANCE = new GiftCommand();

    private GiftCommand() {

    }

    @Subcommand("gift emit")
    @CommandCompletion("-")
    public void onGiftEmit(CommandSender sender, String type) {
        //
        InteractionListener.getInstance()
                .flow(5, null);
        //
        sender.sendMessage(type);
    }


    public static GiftCommand getInstance() {
        return GiftCommand.INSTANCE;
    }

}
