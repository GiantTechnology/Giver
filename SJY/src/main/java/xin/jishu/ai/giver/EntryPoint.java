package xin.jishu.ai.giver;

import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
        this.way = NetworkRegistry.newSimpleChannel(
                new ResourceLocation("giver", "first_networking"),
                String::new, Objects::nonNull, Objects::nonNull
        );

        this.way.messageBuilder(
                        HashMap.class,
                        ThreadLocalRandom.current()
                                .nextInt()
                )
                .encoder((x, y) -> y.writeBytes(this.transformer.toJson(x).getBytes()))
                .decoder((y) -> this.transformer.fromJson(y.readBytes(Short.MAX_VALUE).toString(), HashMap.class))
                .consumer((payload, context) -> {
                    context.get().enqueueWork(() -> {
                        System.out.println(payload);
                        // EntryPoint.LOGGER.info(payload);
                        // LOGGER.info(String.valueOf(payload));
                    });

                    context.get()
                            .setPacketHandled(true);
                })
                .add();
    }
}
