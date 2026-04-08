package com.legacypacks;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LegacyPacksMod implements ClientModInitializer {

    public static final String MOD_ID = "legacy-packs";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Legacy Packs loaded! Old resource packs will be automatically translated.");
    }
}
