package xin.jishu.ai.giver.listeners;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.bukkit.event.Listener;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;
import xin.jishu.ai.giver.EntryPoint;
import xin.jishu.ai.giver.services.ActionService;
import xin.jishu.ai.giver.sundries.actions.BaseAction;
import xin.jishu.ai.giver.sundries.actions.ExecuteAction;

import javax.script.ScriptEngine;
import javax.script.SimpleBindings;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
public class InteractionListener implements Listener {

    private ScriptEngine executor = null;
    private WebSocketClient connector = null;
    private List<Long> followers = new ArrayList<>();
    private static final InteractionListener INSTANCE = new InteractionListener();

    private InteractionListener() {
        //
        NashornScriptEngineFactory engineFactory = new NashornScriptEngineFactory();

        this.executor = engineFactory.getScriptEngine();
        //
        try {
            String fqdn = EntryPoint.getInstance()
                    .getConfig()
                    .getString("listeners.interaction.fqdn");

            this.connector = new WebSocketClient(new URI(fqdn)) {
                private short retry = 0;
                private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

                @Override
                public void onOpen(ServerHandshake payload) {
                    EntryPoint.getInstance()
                            .getLogger()
                            .log(
                                    Level.INFO,
                                    String.format(
                                            "Successfully connected to %s:%s.",
                                            this.getURI().getHost(), this.getURI().getPort()
                                    )
                            );
                    // 重置计数器
                    this.retry = 0;
                }

                @Override
                public void onMessage(String message) {
                    try {
                        JSONObject payload = JSON.parseObject(message);

                        InteractionListener.this.flow(
                                payload.getShort("Type"),
                                payload.getJSONObject("Data")
                        );
                    } catch (Exception wrong) {
                        EntryPoint.getInstance()
                                .getLogger()
                                .log(Level.SEVERE, "Oops!", wrong);
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    EntryPoint.getInstance()
                            .getLogger()
                            .log(
                                    Level.WARNING,
                                    String.format(
                                            "Due to %s, the connection was disconnected by the %s.",
                                            reason, remote ? "server" : "client"
                                    )
                            );
                    // 重新打开连接
                    this.reconnect();
                }

                @Override
                public void onError(Exception wrong) {
                    EntryPoint.getInstance()
                            .getLogger()
                            .log(Level.SEVERE, "Oops!", wrong);
                    // 重新打开连接
                    this.reconnect();
                }

                @Override
                public void reconnect() {
                    EntryPoint.getInstance()
                            .getLogger()
                            .log(Level.INFO, "Attempting to reconnect to server.");
                    //
                    if (this.retry < 6) {
                        this.retry += 1;
                    }
                    //
                    this.executor.schedule(() -> {
                        if (this.getReadyState() == ReadyState.OPEN) {
                            EntryPoint.getInstance()
                                    .getLogger()
                                    .log(
                                            Level.INFO, "Detected a request to reconnect in a normal state. Ignore it."
                                    );
                        } else {
                            super.reconnect();
                        }
                    }, (long) Math.pow(2.0, this.retry), TimeUnit.SECONDS);
                }
            };
        } catch (Exception wrong) {
            EntryPoint.getInstance()
                    .getLogger()
                    .log(Level.SEVERE, "Oops!", wrong);
        }
    }

    public void flow(Short type, Map<String, Object> payload) throws Exception {
        Long roomId = (Long) payload.get("RoomId");
        long exceptRoomId = EntryPoint.getInstance()
                .getConfig()
                .getLong("entities.environment.room");

        if (roomId == null || roomId == exceptRoomId) {
            switch (type) {
                // Chat
                case 1 -> this.simpleExecute(
                        "listeners.interaction.on.chat", payload
                );
                // Like
                case 2 -> this.simpleExecute(
                        "listeners.interaction.on.like", payload
                );
                // Enter
                case 3 -> this.simpleExecute(
                        "listeners.interaction.on.follow.enter", payload
                );
                // Follow
                case 4 -> {
                    Long userId = (Long) ((Map<?, ?>) payload.get("User"))
                            .get("Id");

                    if (this.followers.contains(userId)) {

                    } else {
                        this.simpleExecute(
                                "listeners.interaction.on.follow.execute", payload
                        );
                    }

                }
                // Gift
                case 5 -> {
                    List<Map<?, ?>> mappings = this.filter("entities.gift", payload);

                    for (Map<?, ?> mapping : mappings) {
                        // 如果条件匹配, 则执行相应的动作
                        List<? extends BaseAction> actions = ((List<?>) mapping.get("actions"))
                                .stream()
                                .map(x -> BaseAction.from(x, payload))
                                .collect(Collectors.toList());

                        ActionService.getInstance()
                                .execute(actions);
                        // 跳出循环
                        break;
                    }
                }
                // Broadcast
                case 6 -> this.simpleExecute(
                        "listeners.interaction.on.broadcast", payload
                );
            }
        }
    }

    private List<Map<?, ?>> filter(String key, Map<String, Object> payload) throws Exception {
        List<Map<?, ?>> result = new ArrayList<>();
        List<Map<?, ?>> mappings = EntryPoint.getInstance()
                .getConfig()
                .getMapList(key);

        for (Map<?, ?> mapping : mappings) {
            // 动态匹配条件
            String condition = (String) mapping.get("condition");
            Boolean matched = (Boolean) this.executor.eval(
                    condition, new SimpleBindings(payload)
            );

            if (matched) {
                result.add(mapping);
            }
        }

        return result;
    }

    private void simpleExecute(String key, Map<String, Object> payload) throws Exception {
        List<Map<?, ?>> mappings = this.filter(
                key, payload
        );

        for (Map<?, ?> mapping : mappings) {
            // 如果条件匹配, 则执行相应的动作
            new ExecuteAction(
                    Map.of(
                            "content",
                            mapping.get("execute")
                    ),
                    payload
            ).run();
            // 跳出循环
            break;
        }
    }

    public WebSocketClient getConnector() {
        return this.connector;
    }

    public static InteractionListener getInstance() {
        return InteractionListener.INSTANCE;
    }

}
