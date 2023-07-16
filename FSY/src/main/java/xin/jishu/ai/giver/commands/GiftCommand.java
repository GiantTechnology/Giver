package xin.jishu.ai.giver.commands;

import co.aikar.commands.annotation.*;
import com.alibaba.fastjson2.JSON;
import org.bukkit.command.CommandSender;
import xin.jishu.ai.giver.listeners.InteractionListener;

import java.time.ZonedDateTime;
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
    @SuppressWarnings("unchecked")
    @CommandCompletion("{\"GiftName\":\"玫瑰\",\"GiftCount\":1,\"RepeatCount\":1,\"DiamondCount\":1,\"User\":{\"Nickname\":\"sxsx欧克\",},\"RoomId\":7255644442516933439}")
    public void onGiftEmit(CommandSender sender, String source) throws Exception {
        //
        Map<String, Object> payload = JSON.parseObject(source, Map.class);

        InteractionListener.getInstance()
                .flow((short) 5, payload);
        //
        sender.sendMessage(
                ZonedDateTime.now()
                        .toString()
        );
    }


    public static GiftCommand getInstance() {
        return GiftCommand.INSTANCE;
    }

}
