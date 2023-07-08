package xin.jishu.ai.giver.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;
import xin.jishu.ai.giver.EntryPoint;

import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

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
