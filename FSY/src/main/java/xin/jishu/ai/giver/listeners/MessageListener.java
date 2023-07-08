package xin.jishu.ai.giver.listeners;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
public class MessageListener implements PluginMessageListener {

    private static final MessageListener INSTANCE = new MessageListener();

    private MessageListener() {
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {

    }

    public static MessageListener getInstance() {
        return MessageListener.INSTANCE;
    }

}
