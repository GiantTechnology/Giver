package xin.jishu.ai.giver.commands;

import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;
import xin.jishu.ai.giver.listeners.InteractionListener;

import java.util.HashMap;
import java.util.Map;

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
    @CommandCompletion("玫瑰 1")
    public void onGiftEmit(CommandSender sender, String name, Integer count) throws Exception {
        //
        Map<String, Object> payload = new HashMap<>();

        payload.put("GiftName", name);
        payload.put("GiftCount", count);
        //
        InteractionListener.getInstance()
                .flow((short) 5, payload);
        //
        sender.sendMessage(name);
    }


    public static GiftCommand getInstance() {
        return GiftCommand.INSTANCE;
    }

}
