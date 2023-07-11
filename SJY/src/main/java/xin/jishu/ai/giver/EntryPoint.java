package xin.jishu.ai.giver;

import com.google.gson.Gson;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.event.EventNetworkChannel;
import org.joor.Reflect;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author sxsx欧克
 */
// The value here should match an entry in the META-INF/mods.toml file
@Mod("giver")
public class EntryPoint {

    private Robot controller = null;
    private EventNetworkChannel way = null;
    private final Gson transformer = new Gson();

    /**
     * Directly reference a slf4j logger
     */
    // private static final Logger LOGGER = LogManager.getLogger();
    public EntryPoint() {
        //
        try {
            System.setProperty(
                    "java.awt.headless", Boolean.FALSE.toString()
            );

            this.controller = new Robot();
        } catch (Exception wrong) {
            wrong.printStackTrace();
            // EntryPoint.LOGGER.error("Oops!", wrong);
        }
        //
        this.initialize();
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }


    /**
     * You can use SubscribeEvent and let the Event Bus discover methods to call
     *
     * @param event -
     */
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
    }

    private void initialize() {
        this.way = NetworkRegistry.newEventChannel(
                new ResourceLocation("giver", "3e742e9509314f"),
                () -> NetworkRegistry.ACCEPTVANILLA,
                NetworkRegistry.acceptMissingOr(NetworkRegistry.ACCEPTVANILLA),
                NetworkRegistry.acceptMissingOr(NetworkRegistry.ACCEPTVANILLA)
        );

        this.way.addListener(event -> {
            FriendlyByteBuf payload = event.getPayload();
            NetworkEvent.Context context = event.getSource()
                    .get();
            //
            if (context.getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
                //
                byte[] content = new byte[payload.readableBytes()];

                payload.duplicate()
                        .readBytes(content);

                Map<?, ?> parsedPayload = this.transformer.fromJson(
                        new String(content), Map.class
                );
                //
                String action = (String) parsedPayload.get("action");
                Map<?, ?> arguments = (Map<?, ?>) parsedPayload.get("arguments");

                switch (action) {
                    case "play" -> {
                    }
                    case "input" -> {
                        //
                        String inputType = ((String) arguments.get("type"));
                        boolean complex = Objects.equals(inputType, "mouseMove");

                        if (complex) {
                            List<?> offset = (List<?>) arguments.get("offset");

                            Point at = MouseInfo.getPointerInfo()
                                    .getLocation();

                            this.controller.mouseMove(
                                    this.calculate(
                                            at.x, offset.get(0)
                                    ),
                                    this.calculate(
                                            at.y, offset.get(1)
                                    )
                            );
                        } else {
                            Reflect.on(this.controller)
                                    .call(
                                            inputType,
                                            (Object) Reflect.onClass(
                                                    KeyEvent.class
                                            ).get((String) arguments.get("subtype"))
                                    );
                        }
                    }
                }
            }
            //
            context.setPacketHandled(true);
        });
    }

    private Integer calculate(Integer base, Object source) {
        if (source instanceof String) {
            return base + Integer.parseInt((String) source);
        } else if (source instanceof Integer) {
            return (Integer) source;
        } else if (source instanceof Double) {
            return ((Double) source).intValue();
        } else {
            throw new IllegalArgumentException(
                    source.getClass()
                            .getName()
            );
        }
    }
}
