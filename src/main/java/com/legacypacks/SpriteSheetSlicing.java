package com.legacypacks;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Handles slicing old sprite sheet textures (widgets.png, icons.png, etc.)
 * into individual sprite files that modern Minecraft expects.
 *
 * In 1.7/1.8, GUI elements were packed into large sprite sheets.
 * In 1.20.2+, they were split into individual files under textures/gui/sprites/.
 */
public class SpriteSheetSlicing {
    private static final String CREATIVE_TABS_SHEET = "textures/gui/container/creative_inventory/tabs.png";
    private static final int[] CREATIVE_TAB_X = {0, 28, 56, 84, 112, 140, 140};

    /**
     * Defines a region to slice from a source sprite sheet.
     */
    public record SliceInfo(String sourceSheet, int x, int y, int width, int height, String mcmeta) {}

    // Maps: sprite path (e.g. "textures/gui/sprites/hud/hotbar.png")
    //    -> SliceInfo (source sheet + UV region)
    private static final Map<String, SliceInfo> SPRITE_SLICES = new HashMap<>();

    // mcmeta content for sprites that need nine-slice or other metadata
    private static final String BUTTON_MCMETA =
            "{\"gui\":{\"scaling\":{\"type\":\"nine_slice\",\"width\":200,\"height\":20,\"border\":{\"left\":20,\"top\":4,\"right\":20,\"bottom\":4}}}}";

    static {
        // =====================================================================
        // widgets.png (256x256) -> individual sprites
        // =====================================================================
        String W = "textures/gui/widgets.png";

        // Hotbar
        widgetSprite("hud/hotbar", W, 0, 0, 182, 22);
        widgetSprite("hud/hotbar_selection", W, 0, 22, 24, 24);
        widgetSprite("hud/hotbar_offhand_left", W, 24, 22, 29, 24);
        widgetSprite("hud/hotbar_offhand_right", W, 53, 22, 29, 24);

        // Buttons (need nine-slice metadata)
        addSlice("textures/gui/sprites/widget/button_disabled.png",
                new SliceInfo(W, 0, 46, 200, 20, BUTTON_MCMETA));
        addSlice("textures/gui/sprites/widget/button.png",
                new SliceInfo(W, 0, 66, 200, 20, BUTTON_MCMETA));
        addSlice("textures/gui/sprites/widget/button_highlighted.png",
                new SliceInfo(W, 0, 86, 200, 20, BUTTON_MCMETA));

        // =====================================================================
        // icons.png (256x256) -> individual sprites
        // =====================================================================
        String I = "textures/gui/icons.png";

        // Crosshair
        iconSprite("hud/crosshair", I, 0, 0, 15, 15);
        iconSprite("hud/crosshair_attack_indicator_background", I, 36, 94, 16, 4);
        iconSprite("hud/crosshair_attack_indicator_progress", I, 52, 94, 16, 4);
        iconSprite("hud/crosshair_attack_indicator_full", I, 68, 94, 16, 16);
        iconSprite("hud/hotbar_attack_indicator_background", I, 0, 94, 18, 18);
        iconSprite("hud/hotbar_attack_indicator_progress", I, 18, 94, 18, 18);

        // Armor
        iconSprite("hud/armor_empty", I, 16, 9, 9, 9);
        iconSprite("hud/armor_half", I, 25, 9, 9, 9);
        iconSprite("hud/armor_full", I, 34, 9, 9, 9);

        // Food / Hunger
        iconSprite("hud/food_empty", I, 16, 27, 9, 9);
        iconSprite("hud/food_full", I, 52, 27, 9, 9);
        iconSprite("hud/food_half", I, 61, 27, 9, 9);
        iconSprite("hud/food_full_hunger", I, 88, 27, 9, 9);
        iconSprite("hud/food_half_hunger", I, 97, 27, 9, 9);
        iconSprite("hud/food_empty_hunger", I, 133, 27, 9, 9);

        // Air bubbles
        iconSprite("hud/air", I, 16, 18, 9, 9);
        iconSprite("hud/air_bursting", I, 25, 18, 9, 9);

        // Experience bar
        iconSprite("hud/experience_bar_background", I, 0, 64, 182, 5);
        iconSprite("hud/experience_bar_progress", I, 0, 69, 182, 5);

        // Jump bar
        iconSprite("hud/jump_bar_cooldown", I, 0, 74, 182, 5);
        iconSprite("hud/jump_bar_background", I, 0, 84, 182, 5);
        iconSprite("hud/jump_bar_progress", I, 0, 89, 182, 5);

        // Hearts - Normal
        iconSprite("hud/heart/container", I, 16, 0, 9, 9);
        iconSprite("hud/heart/container_blinking", I, 25, 0, 9, 9);
        iconSprite("hud/heart/full", I, 52, 0, 9, 9);
        iconSprite("hud/heart/half", I, 61, 0, 9, 9);
        iconSprite("hud/heart/full_blinking", I, 70, 0, 9, 9);
        iconSprite("hud/heart/half_blinking", I, 79, 0, 9, 9);

        // Hearts - Poisoned
        iconSprite("hud/heart/poisoned_full", I, 88, 0, 9, 9);
        iconSprite("hud/heart/poisoned_half", I, 97, 0, 9, 9);
        iconSprite("hud/heart/poisoned_full_blinking", I, 106, 0, 9, 9);
        iconSprite("hud/heart/poisoned_half_blinking", I, 115, 0, 9, 9);

        // Hearts - Withered
        iconSprite("hud/heart/withered_full", I, 124, 0, 9, 9);
        iconSprite("hud/heart/withered_half", I, 133, 0, 9, 9);
        iconSprite("hud/heart/withered_full_blinking", I, 142, 0, 9, 9);
        iconSprite("hud/heart/withered_half_blinking", I, 151, 0, 9, 9);

        // Hearts - Absorbing
        iconSprite("hud/heart/absorbing_full", I, 160, 0, 9, 9);
        iconSprite("hud/heart/absorbing_half", I, 169, 0, 9, 9);

        // Hearts - Hardcore
        iconSprite("hud/heart/container_hardcore", I, 16, 45, 9, 9);
        iconSprite("hud/heart/container_hardcore_blinking", I, 25, 45, 9, 9);
        iconSprite("hud/heart/hardcore_full", I, 52, 45, 9, 9);
        iconSprite("hud/heart/hardcore_half", I, 61, 45, 9, 9);
        iconSprite("hud/heart/hardcore_full_blinking", I, 70, 45, 9, 9);
        iconSprite("hud/heart/hardcore_half_blinking", I, 79, 45, 9, 9);
        iconSprite("hud/heart/poisoned_hardcore_full", I, 88, 45, 9, 9);
        iconSprite("hud/heart/poisoned_hardcore_half", I, 97, 45, 9, 9);
        iconSprite("hud/heart/poisoned_hardcore_full_blinking", I, 106, 45, 9, 9);
        iconSprite("hud/heart/poisoned_hardcore_half_blinking", I, 115, 45, 9, 9);
        iconSprite("hud/heart/withered_hardcore_full", I, 124, 45, 9, 9);
        iconSprite("hud/heart/withered_hardcore_half", I, 133, 45, 9, 9);
        iconSprite("hud/heart/withered_hardcore_full_blinking", I, 142, 45, 9, 9);
        iconSprite("hud/heart/withered_hardcore_half_blinking", I, 151, 45, 9, 9);
        iconSprite("hud/heart/absorbing_hardcore_full", I, 160, 45, 9, 9);
        iconSprite("hud/heart/absorbing_hardcore_half", I, 169, 45, 9, 9);

        // Vehicle hearts
        iconSprite("hud/heart/vehicle_container", I, 52, 9, 9, 9);
        iconSprite("hud/heart/vehicle_full", I, 88, 9, 9, 9);
        iconSprite("hud/heart/vehicle_half", I, 97, 9, 9, 9);

        // Ping indicators
        iconSprite("icon/ping_5", I, 0, 176, 10, 8);
        iconSprite("icon/ping_4", I, 0, 184, 10, 8);
        iconSprite("icon/ping_3", I, 0, 192, 10, 8);
        iconSprite("icon/ping_2", I, 0, 200, 10, 8);
        iconSprite("icon/ping_1", I, 0, 208, 10, 8);
        iconSprite("icon/ping_unknown", I, 0, 216, 10, 8);

        // =====================================================================
        // bars.png (256x256) -> boss bar sprites
        // =====================================================================
        String B = "textures/gui/bars.png";

        barSprite("boss_bar/pink_background", B, 0, 0, 182, 5);
        barSprite("boss_bar/pink_progress", B, 0, 5, 182, 5);
        barSprite("boss_bar/blue_background", B, 0, 10, 182, 5);
        barSprite("boss_bar/blue_progress", B, 0, 15, 182, 5);
        barSprite("boss_bar/red_background", B, 0, 20, 182, 5);
        barSprite("boss_bar/red_progress", B, 0, 25, 182, 5);
        barSprite("boss_bar/green_background", B, 0, 30, 182, 5);
        barSprite("boss_bar/green_progress", B, 0, 35, 182, 5);
        barSprite("boss_bar/yellow_background", B, 0, 40, 182, 5);
        barSprite("boss_bar/yellow_progress", B, 0, 45, 182, 5);
        barSprite("boss_bar/purple_background", B, 0, 50, 182, 5);
        barSprite("boss_bar/purple_progress", B, 0, 55, 182, 5);
        barSprite("boss_bar/white_background", B, 0, 60, 182, 5);
        barSprite("boss_bar/white_progress", B, 0, 65, 182, 5);
        barSprite("boss_bar/notched_6_background", B, 0, 80, 182, 5);
        barSprite("boss_bar/notched_6_progress", B, 0, 85, 182, 5);
        barSprite("boss_bar/notched_10_background", B, 0, 90, 182, 5);
        barSprite("boss_bar/notched_10_progress", B, 0, 95, 182, 5);
        barSprite("boss_bar/notched_12_background", B, 0, 100, 182, 5);
        barSprite("boss_bar/notched_12_progress", B, 0, 105, 182, 5);
        barSprite("boss_bar/notched_20_background", B, 0, 110, 182, 5);
        barSprite("boss_bar/notched_20_progress", B, 0, 115, 182, 5);

        // =====================================================================
        // Creative inventory tabs.png (256x256) -> individual tab sprites
        // =====================================================================
        String T = "textures/gui/container/creative_inventory/tabs.png";

        // Scroller
        addSlice("textures/gui/sprites/container/creative_inventory/scroller.png",
                new SliceInfo(T, 232, 0, 12, 15, null));
        addSlice("textures/gui/sprites/container/creative_inventory/scroller_disabled.png",
                new SliceInfo(T, 244, 0, 12, 15, null));

        // Legacy tabs.png stores creative tabs on a 28px grid with transparent
        // gaps between most tabs. The final right-edge tab does not have its own
        // clean 26px block in 1.7/1.8, so we reuse the last full-width slice.
        for (int i = 0; i < 7; i++) {
            int x = CREATIVE_TAB_X[i];
            addSlice("textures/gui/sprites/container/creative_inventory/tab_top_unselected_" + (i + 1) + ".png",
                    new SliceInfo(T, x, 0, 26, 32, null));
            addSlice("textures/gui/sprites/container/creative_inventory/tab_top_selected_" + (i + 1) + ".png",
                    new SliceInfo(T, x, 32, 26, 32, null));
            addSlice("textures/gui/sprites/container/creative_inventory/tab_bottom_unselected_" + (i + 1) + ".png",
                    new SliceInfo(T, x, 64, 26, 32, null));
            addSlice("textures/gui/sprites/container/creative_inventory/tab_bottom_selected_" + (i + 1) + ".png",
                    new SliceInfo(T, x, 96, 26, 32, null));
        }

        // =====================================================================
        // Container overlay sprites (extracted from container background PNGs)
        // =====================================================================

        // Furnace overlays
        addSlice("textures/gui/sprites/container/furnace/lit_progress.png",
                new SliceInfo("textures/gui/container/furnace.png", 176, 0, 14, 14, null));
        addSlice("textures/gui/sprites/container/furnace/burn_progress.png",
                new SliceInfo("textures/gui/container/furnace.png", 176, 14, 24, 16, null));

        // Brewing stand overlays
        // 1.7/1.8 packs have no equivalent fuel bar sprite. Let vanilla provide it.
        addSlice("textures/gui/sprites/container/brewing_stand/brew_progress.png",
                new SliceInfo("textures/gui/container/brewing_stand.png", 176, 0, 9, 28, null));
        addSlice("textures/gui/sprites/container/brewing_stand/bubbles.png",
                new SliceInfo("textures/gui/container/brewing_stand.png", 185, 0, 12, 29, null));

        // Enchanting table overlays
        addSlice("textures/gui/sprites/container/enchanting_table/enchantment_slot.png",
                new SliceInfo("textures/gui/container/enchanting_table.png", 0, 166, 108, 19, null));
        addSlice("textures/gui/sprites/container/enchanting_table/enchantment_slot_disabled.png",
                new SliceInfo("textures/gui/container/enchanting_table.png", 0, 185, 108, 19, null));
        addSlice("textures/gui/sprites/container/enchanting_table/enchantment_slot_highlighted.png",
                new SliceInfo("textures/gui/container/enchanting_table.png", 0, 204, 108, 19, null));
        addSlice("textures/gui/sprites/container/enchanting_table/level_1.png",
                new SliceInfo("textures/gui/container/enchanting_table.png", 0, 223, 16, 16, null));
        addSlice("textures/gui/sprites/container/enchanting_table/level_2.png",
                new SliceInfo("textures/gui/container/enchanting_table.png", 16, 223, 16, 16, null));
        addSlice("textures/gui/sprites/container/enchanting_table/level_3.png",
                new SliceInfo("textures/gui/container/enchanting_table.png", 32, 223, 16, 16, null));
        addSlice("textures/gui/sprites/container/enchanting_table/level_1_disabled.png",
                new SliceInfo("textures/gui/container/enchanting_table.png", 0, 239, 16, 16, null));
        addSlice("textures/gui/sprites/container/enchanting_table/level_2_disabled.png",
                new SliceInfo("textures/gui/container/enchanting_table.png", 16, 239, 16, 16, null));
        addSlice("textures/gui/sprites/container/enchanting_table/level_3_disabled.png",
                new SliceInfo("textures/gui/container/enchanting_table.png", 32, 239, 16, 16, null));

        // Anvil overlays
        addSlice("textures/gui/sprites/container/anvil/text_field.png",
                new SliceInfo("textures/gui/container/anvil.png", 0, 166, 110, 16, null));
        addSlice("textures/gui/sprites/container/anvil/text_field_disabled.png",
                new SliceInfo("textures/gui/container/anvil.png", 0, 182, 110, 16, null));
        addSlice("textures/gui/sprites/container/anvil/error.png",
                new SliceInfo("textures/gui/container/anvil.png", 176, 0, 28, 21, null));

        // Beacon overlays
        addSlice("textures/gui/sprites/container/beacon/button.png",
                new SliceInfo("textures/gui/container/beacon.png", 0, 219, 22, 22, null));
        addSlice("textures/gui/sprites/container/beacon/button_selected.png",
                new SliceInfo("textures/gui/container/beacon.png", 22, 219, 22, 22, null));
        addSlice("textures/gui/sprites/container/beacon/button_disabled.png",
                new SliceInfo("textures/gui/container/beacon.png", 44, 219, 22, 22, null));
        addSlice("textures/gui/sprites/container/beacon/button_highlighted.png",
                new SliceInfo("textures/gui/container/beacon.png", 66, 219, 22, 22, null));
        addSlice("textures/gui/sprites/container/beacon/confirm.png",
                new SliceInfo("textures/gui/container/beacon.png", 90, 220, 18, 18, null));
        addSlice("textures/gui/sprites/container/beacon/cancel.png",
                new SliceInfo("textures/gui/container/beacon.png", 112, 220, 18, 18, null));

        // Inventory (effect backgrounds)
        addSlice("textures/gui/sprites/hud/effect_background.png",
                new SliceInfo("textures/gui/container/inventory.png", 141, 166, 24, 24, null));
        addSlice("textures/gui/sprites/hud/effect_background_ambient.png",
                new SliceInfo("textures/gui/container/inventory.png", 165, 166, 24, 24, null));
        addSlice("textures/gui/sprites/container/inventory/effect_background_large.png",
                new SliceInfo("textures/gui/container/inventory.png", 0, 166, 120, 32, null));
        addSlice("textures/gui/sprites/container/inventory/effect_background_small.png",
                new SliceInfo("textures/gui/container/inventory.png", 0, 198, 32, 32, null));
    }

    private static void widgetSprite(String name, String source, int x, int y, int w, int h) {
        addSlice("textures/gui/sprites/" + name + ".png", new SliceInfo(source, x, y, w, h, null));
    }

    private static void iconSprite(String name, String source, int x, int y, int w, int h) {
        addSlice("textures/gui/sprites/" + name + ".png", new SliceInfo(source, x, y, w, h, null));
    }

    private static void barSprite(String name, String source, int x, int y, int w, int h) {
        addSlice("textures/gui/sprites/" + name + ".png", new SliceInfo(source, x, y, w, h, null));
    }

    private static void addSlice(String spritePath, SliceInfo info) {
        SPRITE_SLICES.put(spritePath, info);
    }

    /**
     * Returns the mcmeta JSON for a given sprite path, or null if no mcmeta needed.
     * Called when the game requests "path.png.mcmeta" for a sprite we provide.
     */
    public static String getMcmeta(String spritePath) {
        SliceInfo info = SPRITE_SLICES.get(spritePath);
        if (info != null) {
            return info.mcmeta();
        }
        return null;
    }

    /**
     * Returns mcmeta for a .mcmeta path (strips the .mcmeta suffix and looks up the sprite).
     */
    public static String getMcmetaForPath(String mcmetaPath) {
        if (mcmetaPath.endsWith(".mcmeta")) {
            String spritePath = mcmetaPath.substring(0, mcmetaPath.length() - ".mcmeta".length());
            return getMcmeta(spritePath);
        }
        return null;
    }

    /**
     * Returns the SliceInfo for a given modern sprite path, or null if not a sliceable sprite.
     */
    public static SliceInfo getSliceInfo(String spritePath) {
        return SPRITE_SLICES.get(spritePath);
    }

    /**
     * Returns all sprite paths that can be sliced from available source sheets.
     * Useful for findResources to report virtual sprites.
     */
    public static Map<String, SliceInfo> getAllSlices() {
        return Collections.unmodifiableMap(SPRITE_SLICES);
    }

    /**
     * Returns all sprite paths whose path starts with the given prefix.
     */
    public static List<Map.Entry<String, SliceInfo>> getSlicesWithPrefix(String prefix) {
        List<Map.Entry<String, SliceInfo>> result = new ArrayList<>();
        String searchPrefix = "textures/gui/sprites/" + prefix;
        for (Map.Entry<String, SliceInfo> entry : SPRITE_SLICES.entrySet()) {
            if (entry.getKey().startsWith(searchPrefix)) {
                result.add(entry);
            }
        }
        return result;
    }

    // Expected vanilla sprite sheet size (all UV coordinates are relative to this)
    private static final int VANILLA_SHEET_SIZE = 256;

    /**
     * Slices a sprite from a source sheet image.
     * Automatically scales UV coordinates based on the actual image resolution
     * vs the vanilla 256x256 base, so it works with any pack resolution (16x, 32x, 64x, 128x, etc.).
     * Returns the sliced sprite as a PNG byte array, or null if slicing fails.
     */
    public static byte[] sliceSprite(InputStream sourceStream, SliceInfo info) {
        try {
            BufferedImage source = ImageIO.read(sourceStream);
            if (source == null) {
                LegacyPacksMod.LOGGER.warn("Legacy Packs: Failed to read source image for slicing");
                return null;
            }

            // Scale coordinates based on actual image size vs vanilla 256x256
            double scaleX = (double) source.getWidth() / VANILLA_SHEET_SIZE;
            double scaleY = (double) source.getHeight() / VANILLA_SHEET_SIZE;

            int x = (int) (info.x() * scaleX);
            int y = (int) (info.y() * scaleY);
            int w = (int) (info.width() * scaleX);
            int h = (int) (info.height() * scaleY);

            // Clamp to image bounds
            x = Math.min(x, source.getWidth());
            y = Math.min(y, source.getHeight());
            w = Math.min(w, source.getWidth() - x);
            h = Math.min(h, source.getHeight() - y);

            if (w <= 0 || h <= 0) {
                LegacyPacksMod.LOGGER.warn("Legacy Packs: Slice region out of bounds for {} ({}x{} sheet, scale {}/{})",
                        info.sourceSheet(), source.getWidth(), source.getHeight(), scaleX, scaleY);
                return null;
            }

            BufferedImage sliced = source.getSubimage(x, y, w, h);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(sliced, "PNG", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            LegacyPacksMod.LOGGER.warn("Legacy Packs: Failed to slice sprite from sheet", e);
            return null;
        }
    }

    /**
     * Returns all unique source sheet paths referenced by the slicing definitions.
     */
    public static Set<String> getAllSourceSheets() {
        Set<String> sheets = new HashSet<>();
        for (SliceInfo info : SPRITE_SLICES.values()) {
            sheets.add(info.sourceSheet());
        }
        return sheets;
    }

    public static void clearCache() {
        // Reserved for future caching implementation
    }
}
