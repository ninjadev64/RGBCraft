package com.amansprojects.rgbcraft;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.effect.StatusEffects;

import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.gitlab.mguimard.openrgb.client.OpenRGBClient;
import io.gitlab.mguimard.openrgb.entity.OpenRGBColor;
import io.gitlab.mguimard.openrgb.entity.OpenRGBDevice;

public class Main implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("RGBCraft");
    public static final OpenRGBClient client = new OpenRGBClient("localhost", 6742, "RGBCraft");
    public static final OpenRGBDevice controller;

    static {
        OpenRGBDevice d;
        try { d = client.getDeviceController(1); }
        catch (IOException e) { d = null; }
        controller = d;
    }
    
    public float health = 20f;

    @Override
    public void onInitialize() {
        LOGGER.info("Ensure your OpenRGB server is running on port 6742!");
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (client.player == null) return;

            if (client.player.getHealth() < health) {
                health = client.player.getHealth();
                flash(OpenRGBColor.fromHexaString("#e03636"));
            }
            else if (client.player.getActiveStatusEffects().get(StatusEffects.POISON) != null) {
                flash(OpenRGBColor.fromHexaString("#28be25"));
            }
            else if (client.player.getHungerManager().getFoodLevel() < 3) {
                flash(OpenRGBColor.fromHexaString("#Ffa600"));
            }
        });
    }

    public static void flash(OpenRGBColor c) {
        new Thread(() -> {
            OpenRGBColor[] existing = (OpenRGBColor[]) controller.getColors().toArray();
            OpenRGBColor[] colours = new OpenRGBColor[controller.getColors().size()];
            Arrays.fill(colours, c);
            try {
                client.updateLeds(1, colours);
                Thread.sleep(500);
                client.updateLeds(1, existing);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
