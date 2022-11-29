package com.amansprojects.rgbcraft;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("RGBCraft");
    public float health = 20f;

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (client.player == null) return;
            if (client.player.getHealth() < health) {
                health = client.player.getHealth();
                LOGGER.info("Took damage");
            }
        });
    }
}
