package xin.jishu.ai.giver.listeners;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;
import xin.jishu.ai.giver.EntryPoint;
import xin.jishu.ai.giver.services.ActionService;
import xin.jishu.ai.giver.sundries.actions.BaseAction;

import javax.script.ScriptEngine;
import javax.script.SimpleBindings;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
public class InteractionListener implements Listener {

    private final ScriptEngine executor;
    private WebSocketClient connector = null;
    private final List<Long> followers = new ArrayList<>();
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
        List<Map<?, ?>> bindings = EntryPoint.getInstance()
                .getConfig()
                .getMapList("listeners.interaction.bindings");

        for (Map<?, ?> binding : bindings) {
            if (
                    Objects.equals(roomId, binding.get("room"))
            ) {
                Player target = EntryPoint.getInstance()
                        .getServer()
                        .getPlayerExact(
                                (String) binding.get("player")
                        );

                if (target == null) {
                    String message = String.format(
                            "The target player %s is currently offline, therefore the execution failed.",
                            binding.get("player")
                    );

                    EntryPoint.getInstance()
                            .getLogger()
                            .log(Level.WARNING, message);
                } else {
                    // 如果玩家在线, 则根据条件判断执行哪些处理逻辑
                    switch (type) {
                        case 1 -> this.simpleExecute("chat", payload, target);
                        case 2 -> this.simpleExecute("like", payload, target);
                        case 3 -> this.simpleExecute("enter", payload, target);
                        case 4 -> {
                            Long userId = (Long) ((Map<?, ?>) payload.get("User"))
                                    .get("Id");

                            if (!this.followers.contains(userId)) {
                                //
                                this.simpleExecute("follow", payload, target);
                                //
                                this.followers.add(userId);
                            }
                        }
                        case 5 -> this.simpleExecute("gift", payload, target);
                        case 6 -> this.simpleExecute("broadcast", payload, target);
                    }
                }
                // 跳出循环
                break;
            }
        }
    }

    private List<Map<?, ?>> filter(String type, Map<String, Object> payload) throws Exception {
        List<Map<?, ?>> result = new ArrayList<>();
        List<Map<?, ?>> mappings = EntryPoint.getInstance()
                .getConfig()
                .getMapList("listeners.interaction.on");

        if(mappings.size() == 0) {
            // 修正 SnakeYaml 的一个奇怪的错误
            mappings = EntryPoint.getInstance()
                    .getConfig()
                    .getMapList("listeners.interaction.true");
        }

        for (Map<?, ?> mapping : mappings) {
            // 判断类型是否匹配
            if (
                    Objects.equals(
                            type, mapping.get("type")
                    )
            ) {
                // 判断动态条件是否匹配
                String condition = (String) mapping.get("condition");
                Boolean matched = (Boolean) this.executor.eval(
                        condition, new SimpleBindings(payload)
                );

                if (matched) {
                    result.add(mapping);
                }
            }
        }

        return result;
    }

    private void simpleExecute(String type, Map<String, Object> payload, Player target) throws Exception {
        List<Map<?, ?>> mappings = this.filter(type, payload);

        for (Map<?, ?> mapping : mappings) {
            // 如果条件匹配, 则执行相应的动作
            List<? extends BaseAction> actions = ((List<?>) mapping.get("actions"))
                    .stream()
                    .map(x -> BaseAction.from(x, payload, target))
                    .collect(Collectors.toList());

            ActionService.getInstance()
                    .execute(actions);
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
