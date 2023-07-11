package xin.jishu.ai.giver;

import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.awt.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("giver")
public class EntryPoint {

    private Robot controller = null;
    private SimpleChannel way = null;
    private final Gson transformer = new Gson();
    public static final String WAY_VERSION = Integer.toString(1);

    /**
     * Directly reference a slf4j logger
     */
    // private static final Logger LOGGER = LogManager.getLogger();
    public EntryPoint() {
        //
        try {
            this.controller = new Robot();
        } catch (Exception wrong) {
            wrong.printStackTrace();
            // EntryPoint.LOGGER.error("Oops!", wrong);
        }
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }


    /**
     * You can use SubscribeEvent and let the Event Bus discover methods to call
     * Do something when the server starts
     *
     * @param event -
     */
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        this.way = NetworkRegistry.ChannelBuilder
                .named(
                        new ResourceLocation("giver", "3e742e9509314f")
                )
                .networkProtocolVersion(() -> EntryPoint.WAY_VERSION)
                .clientAcceptedVersions(EntryPoint.WAY_VERSION::equalsIgnoreCase)
                .serverAcceptedVersions(EntryPoint.WAY_VERSION::equalsIgnoreCase)
                .simpleChannel();

        this.way.messageBuilder(
                        HashMap.class, 0
                )
                .encoder((source, target) ->
                        target.writeBytes(
                                this.transformer.toJson(source)
                                        .getBytes()
                        )
                )
                .decoder(
                        (source) ->
                                this.transformer.fromJson(
                                        source.readBytes(Short.MAX_VALUE)
                                                .toString(),
                                        HashMap.class
                                )
                )
                .consumer((payload, source) -> {
                    NetworkEvent.Context context = source.get();

                    context.enqueueWork(() -> {
                        System.out.println(payload);
                        // EntryPoint.LOGGER.info(payload);
                        // LOGGER.info(String.valueOf(payload));
                    });

                    context.setPacketHandled(true);
                })
                .add();
    }
}
