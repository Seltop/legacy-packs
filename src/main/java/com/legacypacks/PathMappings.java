package com.legacypacks;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps old resource pack paths (1.7/1.8 era) to modern equivalents (1.21+).
 * Covers the major renames from "The Flattening" (1.13) and subsequent updates.
 */
public class PathMappings {

    // Folder-level renames (old prefix -> new prefix)
    public static final Map<String, String> FOLDER_RENAMES = new HashMap<>();

    // Individual texture file renames (old name -> new name, without folder prefix)
    public static final Map<String, String> BLOCK_TEXTURE_RENAMES = new HashMap<>();
    public static final Map<String, String> ITEM_TEXTURE_RENAMES = new HashMap<>();
    public static final Map<String, String> ENTITY_TEXTURE_RENAMES = new HashMap<>();
    public static final Map<String, String> GUI_TEXTURE_RENAMES = new HashMap<>();
    public static final Map<String, String> MISC_RENAMES = new HashMap<>();

    // Cross-directory renames: old full path -> modern full path
    // For textures that moved between completely different directories (e.g., items/ -> gui/sprites/)
    public static final Map<String, String> CROSS_DIRECTORY_RENAMES = new HashMap<>();

    static {
        // =====================================================================
        // FOLDER RENAMES (1.13 Flattening)
        // =====================================================================
        FOLDER_RENAMES.put("textures/blocks/", "textures/block/");
        FOLDER_RENAMES.put("textures/items/", "textures/item/");

        // =====================================================================
        // BLOCK TEXTURE RENAMES
        // =====================================================================

        // Stone variants
        BLOCK_TEXTURE_RENAMES.put("cobblestone_mossy.png", "mossy_cobblestone.png");
        BLOCK_TEXTURE_RENAMES.put("stonebrick.png", "stone_bricks.png");
        BLOCK_TEXTURE_RENAMES.put("stonebrick_carved.png", "chiseled_stone_bricks.png");
        BLOCK_TEXTURE_RENAMES.put("stonebrick_cracked.png", "cracked_stone_bricks.png");
        BLOCK_TEXTURE_RENAMES.put("stonebrick_mossy.png", "mossy_stone_bricks.png");
        BLOCK_TEXTURE_RENAMES.put("stone_slab_side.png", "smooth_stone_slab_side.png");
        BLOCK_TEXTURE_RENAMES.put("stone_slab_top.png", "smooth_stone.png");

        // Grass and dirt
        BLOCK_TEXTURE_RENAMES.put("grass_side.png", "grass_block_side.png");
        BLOCK_TEXTURE_RENAMES.put("grass_top.png", "grass_block_top.png");
        BLOCK_TEXTURE_RENAMES.put("grass_side_overlay.png", "grass_block_side_overlay.png");
        BLOCK_TEXTURE_RENAMES.put("grass_side_snowed.png", "grass_block_snow.png");
        BLOCK_TEXTURE_RENAMES.put("dirt_podzol_side.png", "podzol_side.png");
        BLOCK_TEXTURE_RENAMES.put("dirt_podzol_top.png", "podzol_top.png");
        BLOCK_TEXTURE_RENAMES.put("mycel_side.png", "mycelium_side.png");
        BLOCK_TEXTURE_RENAMES.put("mycel_top.png", "mycelium_top.png");
        BLOCK_TEXTURE_RENAMES.put("farmland_dry.png", "farmland.png");
        BLOCK_TEXTURE_RENAMES.put("farmland_wet.png", "farmland_moist.png");

        // Wood and logs
        BLOCK_TEXTURE_RENAMES.put("log_oak.png", "oak_log.png");
        BLOCK_TEXTURE_RENAMES.put("log_oak_top.png", "oak_log_top.png");
        BLOCK_TEXTURE_RENAMES.put("log_spruce.png", "spruce_log.png");
        BLOCK_TEXTURE_RENAMES.put("log_spruce_top.png", "spruce_log_top.png");
        BLOCK_TEXTURE_RENAMES.put("log_birch.png", "birch_log.png");
        BLOCK_TEXTURE_RENAMES.put("log_birch_top.png", "birch_log_top.png");
        BLOCK_TEXTURE_RENAMES.put("log_jungle.png", "jungle_log.png");
        BLOCK_TEXTURE_RENAMES.put("log_jungle_top.png", "jungle_log_top.png");
        BLOCK_TEXTURE_RENAMES.put("log_acacia.png", "acacia_log.png");
        BLOCK_TEXTURE_RENAMES.put("log_acacia_top.png", "acacia_log_top.png");
        BLOCK_TEXTURE_RENAMES.put("log_big_oak.png", "dark_oak_log.png");
        BLOCK_TEXTURE_RENAMES.put("log_big_oak_top.png", "dark_oak_log_top.png");

        // Planks
        BLOCK_TEXTURE_RENAMES.put("planks_oak.png", "oak_planks.png");
        BLOCK_TEXTURE_RENAMES.put("planks_spruce.png", "spruce_planks.png");
        BLOCK_TEXTURE_RENAMES.put("planks_birch.png", "birch_planks.png");
        BLOCK_TEXTURE_RENAMES.put("planks_jungle.png", "jungle_planks.png");
        BLOCK_TEXTURE_RENAMES.put("planks_acacia.png", "acacia_planks.png");
        BLOCK_TEXTURE_RENAMES.put("planks_big_oak.png", "dark_oak_planks.png");

        // Leaves
        BLOCK_TEXTURE_RENAMES.put("leaves_oak.png", "oak_leaves.png");
        BLOCK_TEXTURE_RENAMES.put("leaves_spruce.png", "spruce_leaves.png");
        BLOCK_TEXTURE_RENAMES.put("leaves_birch.png", "birch_leaves.png");
        BLOCK_TEXTURE_RENAMES.put("leaves_jungle.png", "jungle_leaves.png");
        BLOCK_TEXTURE_RENAMES.put("leaves_acacia.png", "acacia_leaves.png");
        BLOCK_TEXTURE_RENAMES.put("leaves_big_oak.png", "dark_oak_leaves.png");

        // Saplings
        BLOCK_TEXTURE_RENAMES.put("sapling_oak.png", "oak_sapling.png");
        BLOCK_TEXTURE_RENAMES.put("sapling_spruce.png", "spruce_sapling.png");
        BLOCK_TEXTURE_RENAMES.put("sapling_birch.png", "birch_sapling.png");
        BLOCK_TEXTURE_RENAMES.put("sapling_jungle.png", "jungle_sapling.png");
        BLOCK_TEXTURE_RENAMES.put("sapling_acacia.png", "acacia_sapling.png");
        BLOCK_TEXTURE_RENAMES.put("sapling_roofed_oak.png", "dark_oak_sapling.png");

        // Doors
        BLOCK_TEXTURE_RENAMES.put("door_wood_upper.png", "oak_door_top.png");
        BLOCK_TEXTURE_RENAMES.put("door_wood_lower.png", "oak_door_bottom.png");
        BLOCK_TEXTURE_RENAMES.put("door_iron_upper.png", "iron_door_top.png");
        BLOCK_TEXTURE_RENAMES.put("door_iron_lower.png", "iron_door_bottom.png");
        BLOCK_TEXTURE_RENAMES.put("door_spruce_upper.png", "spruce_door_top.png");
        BLOCK_TEXTURE_RENAMES.put("door_spruce_lower.png", "spruce_door_bottom.png");
        BLOCK_TEXTURE_RENAMES.put("door_birch_upper.png", "birch_door_top.png");
        BLOCK_TEXTURE_RENAMES.put("door_birch_lower.png", "birch_door_bottom.png");
        BLOCK_TEXTURE_RENAMES.put("door_jungle_upper.png", "jungle_door_top.png");
        BLOCK_TEXTURE_RENAMES.put("door_jungle_lower.png", "jungle_door_bottom.png");
        BLOCK_TEXTURE_RENAMES.put("door_acacia_upper.png", "acacia_door_top.png");
        BLOCK_TEXTURE_RENAMES.put("door_acacia_lower.png", "acacia_door_bottom.png");
        BLOCK_TEXTURE_RENAMES.put("door_dark_oak_upper.png", "dark_oak_door_top.png");
        BLOCK_TEXTURE_RENAMES.put("door_dark_oak_lower.png", "dark_oak_door_bottom.png");

        // Wool
        BLOCK_TEXTURE_RENAMES.put("wool_colored_white.png", "white_wool.png");
        BLOCK_TEXTURE_RENAMES.put("wool_colored_orange.png", "orange_wool.png");
        BLOCK_TEXTURE_RENAMES.put("wool_colored_magenta.png", "magenta_wool.png");
        BLOCK_TEXTURE_RENAMES.put("wool_colored_light_blue.png", "light_blue_wool.png");
        BLOCK_TEXTURE_RENAMES.put("wool_colored_yellow.png", "yellow_wool.png");
        BLOCK_TEXTURE_RENAMES.put("wool_colored_lime.png", "lime_wool.png");
        BLOCK_TEXTURE_RENAMES.put("wool_colored_pink.png", "pink_wool.png");
        BLOCK_TEXTURE_RENAMES.put("wool_colored_gray.png", "gray_wool.png");
        BLOCK_TEXTURE_RENAMES.put("wool_colored_silver.png", "light_gray_wool.png");
        BLOCK_TEXTURE_RENAMES.put("wool_colored_cyan.png", "cyan_wool.png");
        BLOCK_TEXTURE_RENAMES.put("wool_colored_purple.png", "purple_wool.png");
        BLOCK_TEXTURE_RENAMES.put("wool_colored_blue.png", "blue_wool.png");
        BLOCK_TEXTURE_RENAMES.put("wool_colored_brown.png", "brown_wool.png");
        BLOCK_TEXTURE_RENAMES.put("wool_colored_green.png", "green_wool.png");
        BLOCK_TEXTURE_RENAMES.put("wool_colored_red.png", "red_wool.png");
        BLOCK_TEXTURE_RENAMES.put("wool_colored_black.png", "black_wool.png");

        // Stained glass
        BLOCK_TEXTURE_RENAMES.put("glass_white.png", "white_stained_glass.png");
        BLOCK_TEXTURE_RENAMES.put("glass_orange.png", "orange_stained_glass.png");
        BLOCK_TEXTURE_RENAMES.put("glass_magenta.png", "magenta_stained_glass.png");
        BLOCK_TEXTURE_RENAMES.put("glass_light_blue.png", "light_blue_stained_glass.png");
        BLOCK_TEXTURE_RENAMES.put("glass_yellow.png", "yellow_stained_glass.png");
        BLOCK_TEXTURE_RENAMES.put("glass_lime.png", "lime_stained_glass.png");
        BLOCK_TEXTURE_RENAMES.put("glass_pink.png", "pink_stained_glass.png");
        BLOCK_TEXTURE_RENAMES.put("glass_gray.png", "gray_stained_glass.png");
        BLOCK_TEXTURE_RENAMES.put("glass_silver.png", "light_gray_stained_glass.png");
        BLOCK_TEXTURE_RENAMES.put("glass_cyan.png", "cyan_stained_glass.png");
        BLOCK_TEXTURE_RENAMES.put("glass_purple.png", "purple_stained_glass.png");
        BLOCK_TEXTURE_RENAMES.put("glass_blue.png", "blue_stained_glass.png");
        BLOCK_TEXTURE_RENAMES.put("glass_brown.png", "brown_stained_glass.png");
        BLOCK_TEXTURE_RENAMES.put("glass_green.png", "green_stained_glass.png");
        BLOCK_TEXTURE_RENAMES.put("glass_red.png", "red_stained_glass.png");
        BLOCK_TEXTURE_RENAMES.put("glass_black.png", "black_stained_glass.png");

        // Stained glass panes
        BLOCK_TEXTURE_RENAMES.put("glass_pane_top_white.png", "white_stained_glass_pane_top.png");
        BLOCK_TEXTURE_RENAMES.put("glass_pane_top_orange.png", "orange_stained_glass_pane_top.png");
        BLOCK_TEXTURE_RENAMES.put("glass_pane_top_magenta.png", "magenta_stained_glass_pane_top.png");
        BLOCK_TEXTURE_RENAMES.put("glass_pane_top_light_blue.png", "light_blue_stained_glass_pane_top.png");
        BLOCK_TEXTURE_RENAMES.put("glass_pane_top_yellow.png", "yellow_stained_glass_pane_top.png");
        BLOCK_TEXTURE_RENAMES.put("glass_pane_top_lime.png", "lime_stained_glass_pane_top.png");
        BLOCK_TEXTURE_RENAMES.put("glass_pane_top_pink.png", "pink_stained_glass_pane_top.png");
        BLOCK_TEXTURE_RENAMES.put("glass_pane_top_gray.png", "gray_stained_glass_pane_top.png");
        BLOCK_TEXTURE_RENAMES.put("glass_pane_top_silver.png", "light_gray_stained_glass_pane_top.png");
        BLOCK_TEXTURE_RENAMES.put("glass_pane_top_cyan.png", "cyan_stained_glass_pane_top.png");
        BLOCK_TEXTURE_RENAMES.put("glass_pane_top_purple.png", "purple_stained_glass_pane_top.png");
        BLOCK_TEXTURE_RENAMES.put("glass_pane_top_blue.png", "blue_stained_glass_pane_top.png");
        BLOCK_TEXTURE_RENAMES.put("glass_pane_top_brown.png", "brown_stained_glass_pane_top.png");
        BLOCK_TEXTURE_RENAMES.put("glass_pane_top_green.png", "green_stained_glass_pane_top.png");
        BLOCK_TEXTURE_RENAMES.put("glass_pane_top_red.png", "red_stained_glass_pane_top.png");
        BLOCK_TEXTURE_RENAMES.put("glass_pane_top_black.png", "black_stained_glass_pane_top.png");

        // Hardened clay / terracotta
        BLOCK_TEXTURE_RENAMES.put("hardened_clay.png", "terracotta.png");
        BLOCK_TEXTURE_RENAMES.put("hardened_clay_stained_white.png", "white_terracotta.png");
        BLOCK_TEXTURE_RENAMES.put("hardened_clay_stained_orange.png", "orange_terracotta.png");
        BLOCK_TEXTURE_RENAMES.put("hardened_clay_stained_magenta.png", "magenta_terracotta.png");
        BLOCK_TEXTURE_RENAMES.put("hardened_clay_stained_light_blue.png", "light_blue_terracotta.png");
        BLOCK_TEXTURE_RENAMES.put("hardened_clay_stained_yellow.png", "yellow_terracotta.png");
        BLOCK_TEXTURE_RENAMES.put("hardened_clay_stained_lime.png", "lime_terracotta.png");
        BLOCK_TEXTURE_RENAMES.put("hardened_clay_stained_pink.png", "pink_terracotta.png");
        BLOCK_TEXTURE_RENAMES.put("hardened_clay_stained_gray.png", "gray_terracotta.png");
        BLOCK_TEXTURE_RENAMES.put("hardened_clay_stained_silver.png", "light_gray_terracotta.png");
        BLOCK_TEXTURE_RENAMES.put("hardened_clay_stained_cyan.png", "cyan_terracotta.png");
        BLOCK_TEXTURE_RENAMES.put("hardened_clay_stained_purple.png", "purple_terracotta.png");
        BLOCK_TEXTURE_RENAMES.put("hardened_clay_stained_blue.png", "blue_terracotta.png");
        BLOCK_TEXTURE_RENAMES.put("hardened_clay_stained_brown.png", "brown_terracotta.png");
        BLOCK_TEXTURE_RENAMES.put("hardened_clay_stained_green.png", "green_terracotta.png");
        BLOCK_TEXTURE_RENAMES.put("hardened_clay_stained_red.png", "red_terracotta.png");
        BLOCK_TEXTURE_RENAMES.put("hardened_clay_stained_black.png", "black_terracotta.png");

        // Glazed terracotta was added in 1.12, but names changed in 1.13
        // (no renames needed - they were already named correctly)

        // Concrete and concrete powder (added 1.12, names changed slightly)

        // Ores
        BLOCK_TEXTURE_RENAMES.put("coal_ore.png", "coal_ore.png"); // same
        BLOCK_TEXTURE_RENAMES.put("iron_ore.png", "iron_ore.png"); // same
        BLOCK_TEXTURE_RENAMES.put("gold_ore.png", "gold_ore.png"); // same
        BLOCK_TEXTURE_RENAMES.put("diamond_ore.png", "diamond_ore.png"); // same
        BLOCK_TEXTURE_RENAMES.put("emerald_ore.png", "emerald_ore.png"); // same
        BLOCK_TEXTURE_RENAMES.put("lapis_ore.png", "lapis_ore.png"); // same
        BLOCK_TEXTURE_RENAMES.put("redstone_ore.png", "redstone_ore.png"); // same
        BLOCK_TEXTURE_RENAMES.put("quartz_ore.png", "nether_quartz_ore.png");

        // Sandstone
        BLOCK_TEXTURE_RENAMES.put("sandstone_normal.png", "sandstone.png");
        BLOCK_TEXTURE_RENAMES.put("sandstone_carved.png", "chiseled_sandstone.png");
        BLOCK_TEXTURE_RENAMES.put("sandstone_smooth.png", "cut_sandstone.png");
        BLOCK_TEXTURE_RENAMES.put("sandstone_top.png", "sandstone_top.png"); // same
        BLOCK_TEXTURE_RENAMES.put("sandstone_bottom.png", "sandstone_bottom.png"); // same
        BLOCK_TEXTURE_RENAMES.put("red_sandstone_normal.png", "red_sandstone.png");
        BLOCK_TEXTURE_RENAMES.put("red_sandstone_carved.png", "chiseled_red_sandstone.png");
        BLOCK_TEXTURE_RENAMES.put("red_sandstone_smooth.png", "cut_red_sandstone.png");

        // Nether
        BLOCK_TEXTURE_RENAMES.put("netherrack.png", "netherrack.png"); // same
        BLOCK_TEXTURE_RENAMES.put("nether_brick.png", "nether_bricks.png");
        BLOCK_TEXTURE_RENAMES.put("soul_sand.png", "soul_sand.png"); // same
        BLOCK_TEXTURE_RENAMES.put("glowstone.png", "glowstone.png"); // same
        BLOCK_TEXTURE_RENAMES.put("quartz_block_side.png", "quartz_block_side.png"); // same
        BLOCK_TEXTURE_RENAMES.put("quartz_block_top.png", "quartz_block_top.png"); // same
        BLOCK_TEXTURE_RENAMES.put("quartz_block_bottom.png", "quartz_block_bottom.png"); // same
        BLOCK_TEXTURE_RENAMES.put("quartz_block_chiseled.png", "chiseled_quartz_block.png");
        BLOCK_TEXTURE_RENAMES.put("quartz_block_chiseled_top.png", "chiseled_quartz_block_top.png");
        BLOCK_TEXTURE_RENAMES.put("quartz_block_lines.png", "quartz_pillar.png");
        BLOCK_TEXTURE_RENAMES.put("quartz_block_lines_top.png", "quartz_pillar_top.png");

        // Prismarine
        BLOCK_TEXTURE_RENAMES.put("prismarine_rough.png", "prismarine.png");
        BLOCK_TEXTURE_RENAMES.put("prismarine_bricks.png", "prismarine_bricks.png"); // same
        BLOCK_TEXTURE_RENAMES.put("prismarine_dark.png", "dark_prismarine.png");

        // Redstone components
        BLOCK_TEXTURE_RENAMES.put("piston_top_normal.png", "piston_top.png");
        BLOCK_TEXTURE_RENAMES.put("piston_top_sticky.png", "piston_top_sticky.png"); // same
        BLOCK_TEXTURE_RENAMES.put("piston_side.png", "piston_side.png"); // same
        BLOCK_TEXTURE_RENAMES.put("piston_bottom.png", "piston_bottom.png"); // same
        BLOCK_TEXTURE_RENAMES.put("piston_inner.png", "piston_inner.png"); // same
        BLOCK_TEXTURE_RENAMES.put("redstone_dust_cross.png", "redstone_dust_dot.png");
        BLOCK_TEXTURE_RENAMES.put("redstone_dust_line.png", "redstone_dust_line0.png");
        BLOCK_TEXTURE_RENAMES.put("repeater_off.png", "repeater.png");
        BLOCK_TEXTURE_RENAMES.put("repeater_on.png", "repeater_on.png");
        BLOCK_TEXTURE_RENAMES.put("comparator_off.png", "comparator.png");
        BLOCK_TEXTURE_RENAMES.put("comparator_on.png", "comparator_on.png");
        BLOCK_TEXTURE_RENAMES.put("redstone_lamp_off.png", "redstone_lamp.png");
        BLOCK_TEXTURE_RENAMES.put("redstone_lamp_on.png", "redstone_lamp_on.png");
        BLOCK_TEXTURE_RENAMES.put("redstone_torch_off.png", "redstone_torch_off.png");
        BLOCK_TEXTURE_RENAMES.put("redstone_torch_on.png", "redstone_torch.png");

        // TNT
        BLOCK_TEXTURE_RENAMES.put("tnt_side.png", "tnt_side.png"); // same
        BLOCK_TEXTURE_RENAMES.put("tnt_top.png", "tnt_top.png"); // same
        BLOCK_TEXTURE_RENAMES.put("tnt_bottom.png", "tnt_bottom.png"); // same

        // Crafting and utility blocks
        BLOCK_TEXTURE_RENAMES.put("crafting_table_front.png", "crafting_table_front.png"); // same
        BLOCK_TEXTURE_RENAMES.put("crafting_table_side.png", "crafting_table_side.png"); // same
        BLOCK_TEXTURE_RENAMES.put("crafting_table_top.png", "crafting_table_top.png"); // same
        BLOCK_TEXTURE_RENAMES.put("furnace_front_off.png", "furnace_front.png");
        BLOCK_TEXTURE_RENAMES.put("furnace_front_on.png", "furnace_front_on.png");
        BLOCK_TEXTURE_RENAMES.put("furnace_side.png", "furnace_side.png"); // same
        BLOCK_TEXTURE_RENAMES.put("furnace_top.png", "furnace_top.png"); // same
        BLOCK_TEXTURE_RENAMES.put("enchanting_table_bottom.png", "enchanting_table_bottom.png"); // same
        BLOCK_TEXTURE_RENAMES.put("enchanting_table_side.png", "enchanting_table_side.png"); // same
        BLOCK_TEXTURE_RENAMES.put("enchanting_table_top.png", "enchanting_table_top.png"); // same
        BLOCK_TEXTURE_RENAMES.put("bookshelf.png", "bookshelf.png"); // same
        BLOCK_TEXTURE_RENAMES.put("anvil_base.png", "anvil.png");
        BLOCK_TEXTURE_RENAMES.put("anvil_top_damaged_0.png", "anvil_top.png");
        BLOCK_TEXTURE_RENAMES.put("anvil_top_damaged_1.png", "chipped_anvil_top.png");
        BLOCK_TEXTURE_RENAMES.put("anvil_top_damaged_2.png", "damaged_anvil_top.png");

        // Crops
        BLOCK_TEXTURE_RENAMES.put("wheat_stage_0.png", "wheat_stage0.png");
        BLOCK_TEXTURE_RENAMES.put("wheat_stage_1.png", "wheat_stage1.png");
        BLOCK_TEXTURE_RENAMES.put("wheat_stage_2.png", "wheat_stage2.png");
        BLOCK_TEXTURE_RENAMES.put("wheat_stage_3.png", "wheat_stage3.png");
        BLOCK_TEXTURE_RENAMES.put("wheat_stage_4.png", "wheat_stage4.png");
        BLOCK_TEXTURE_RENAMES.put("wheat_stage_5.png", "wheat_stage5.png");
        BLOCK_TEXTURE_RENAMES.put("wheat_stage_6.png", "wheat_stage6.png");
        BLOCK_TEXTURE_RENAMES.put("wheat_stage_7.png", "wheat_stage7.png");
        BLOCK_TEXTURE_RENAMES.put("potatoes_stage_0.png", "potatoes_stage0.png");
        BLOCK_TEXTURE_RENAMES.put("potatoes_stage_1.png", "potatoes_stage1.png");
        BLOCK_TEXTURE_RENAMES.put("potatoes_stage_2.png", "potatoes_stage2.png");
        BLOCK_TEXTURE_RENAMES.put("potatoes_stage_3.png", "potatoes_stage3.png");
        BLOCK_TEXTURE_RENAMES.put("carrots_stage_0.png", "carrots_stage0.png");
        BLOCK_TEXTURE_RENAMES.put("carrots_stage_1.png", "carrots_stage1.png");
        BLOCK_TEXTURE_RENAMES.put("carrots_stage_2.png", "carrots_stage2.png");
        BLOCK_TEXTURE_RENAMES.put("carrots_stage_3.png", "carrots_stage3.png");
        BLOCK_TEXTURE_RENAMES.put("beetroots_stage_0.png", "beetroots_stage0.png");
        BLOCK_TEXTURE_RENAMES.put("beetroots_stage_1.png", "beetroots_stage1.png");
        BLOCK_TEXTURE_RENAMES.put("beetroots_stage_2.png", "beetroots_stage2.png");
        BLOCK_TEXTURE_RENAMES.put("beetroots_stage_3.png", "beetroots_stage3.png");
        BLOCK_TEXTURE_RENAMES.put("melon_stem_disconnected.png", "melon_stem.png");
        BLOCK_TEXTURE_RENAMES.put("melon_stem_connected.png", "attached_melon_stem.png");
        BLOCK_TEXTURE_RENAMES.put("pumpkin_stem_disconnected.png", "pumpkin_stem.png");
        BLOCK_TEXTURE_RENAMES.put("pumpkin_stem_connected.png", "attached_pumpkin_stem.png");
        BLOCK_TEXTURE_RENAMES.put("nether_wart_stage_0.png", "nether_wart_stage0.png");
        BLOCK_TEXTURE_RENAMES.put("nether_wart_stage_1.png", "nether_wart_stage1.png");
        BLOCK_TEXTURE_RENAMES.put("nether_wart_stage_2.png", "nether_wart_stage2.png");

        // Flowers
        BLOCK_TEXTURE_RENAMES.put("flower_rose.png", "poppy.png");
        BLOCK_TEXTURE_RENAMES.put("flower_dandelion.png", "dandelion.png");
        BLOCK_TEXTURE_RENAMES.put("flower_blue_orchid.png", "blue_orchid.png");
        BLOCK_TEXTURE_RENAMES.put("flower_allium.png", "allium.png");
        BLOCK_TEXTURE_RENAMES.put("flower_houstonia.png", "azure_bluet.png");
        BLOCK_TEXTURE_RENAMES.put("flower_tulip_red.png", "red_tulip.png");
        BLOCK_TEXTURE_RENAMES.put("flower_tulip_orange.png", "orange_tulip.png");
        BLOCK_TEXTURE_RENAMES.put("flower_tulip_white.png", "white_tulip.png");
        BLOCK_TEXTURE_RENAMES.put("flower_tulip_pink.png", "pink_tulip.png");
        BLOCK_TEXTURE_RENAMES.put("flower_oxeye_daisy.png", "oxeye_daisy.png");
        BLOCK_TEXTURE_RENAMES.put("flower_paeonia_top.png", "peony_top.png");
        BLOCK_TEXTURE_RENAMES.put("flower_paeonia_bottom.png", "peony_bottom.png");
        BLOCK_TEXTURE_RENAMES.put("flower_syringa_top.png", "lilac_top.png");
        BLOCK_TEXTURE_RENAMES.put("flower_syringa_bottom.png", "lilac_bottom.png");
        BLOCK_TEXTURE_RENAMES.put("flower_sunflower_front.png", "sunflower_front.png");
        BLOCK_TEXTURE_RENAMES.put("flower_sunflower_back.png", "sunflower_back.png");
        BLOCK_TEXTURE_RENAMES.put("flower_sunflower_bottom.png", "sunflower_bottom.png");
        BLOCK_TEXTURE_RENAMES.put("flower_sunflower_top.png", "sunflower_top.png");
        BLOCK_TEXTURE_RENAMES.put("double_plant_fern_bottom.png", "large_fern_bottom.png");
        BLOCK_TEXTURE_RENAMES.put("double_plant_fern_top.png", "large_fern_top.png");
        BLOCK_TEXTURE_RENAMES.put("double_plant_grass_bottom.png", "tall_grass_bottom.png");
        BLOCK_TEXTURE_RENAMES.put("double_plant_grass_top.png", "tall_grass_top.png");
        BLOCK_TEXTURE_RENAMES.put("double_plant_rose_bottom.png", "rose_bush_bottom.png");
        BLOCK_TEXTURE_RENAMES.put("double_plant_rose_top.png", "rose_bush_top.png");
        BLOCK_TEXTURE_RENAMES.put("tallgrass.png", "grass.png");
        BLOCK_TEXTURE_RENAMES.put("fern.png", "fern.png"); // same
        BLOCK_TEXTURE_RENAMES.put("deadbush.png", "dead_bush.png");

        // Mushroom
        BLOCK_TEXTURE_RENAMES.put("mushroom_red.png", "red_mushroom.png");
        BLOCK_TEXTURE_RENAMES.put("mushroom_brown.png", "brown_mushroom.png");
        BLOCK_TEXTURE_RENAMES.put("mushroom_block_skin_brown.png", "brown_mushroom_block.png");
        BLOCK_TEXTURE_RENAMES.put("mushroom_block_skin_red.png", "red_mushroom_block.png");
        BLOCK_TEXTURE_RENAMES.put("mushroom_block_skin_stem.png", "mushroom_stem.png");
        BLOCK_TEXTURE_RENAMES.put("mushroom_block_inside.png", "mushroom_block_inside.png"); // same

        // Ice
        BLOCK_TEXTURE_RENAMES.put("ice_packed.png", "packed_ice.png");

        // Misc blocks
        BLOCK_TEXTURE_RENAMES.put("waterlily.png", "lily_pad.png");
        BLOCK_TEXTURE_RENAMES.put("web.png", "cobweb.png");
        BLOCK_TEXTURE_RENAMES.put("mob_spawner.png", "spawner.png");
        BLOCK_TEXTURE_RENAMES.put("end_stone.png", "end_stone.png"); // same
        BLOCK_TEXTURE_RENAMES.put("endframe_top.png", "end_portal_frame_top.png");
        BLOCK_TEXTURE_RENAMES.put("endframe_side.png", "end_portal_frame_side.png");
        BLOCK_TEXTURE_RENAMES.put("endframe_eye.png", "end_portal_frame_eye.png");
        BLOCK_TEXTURE_RENAMES.put("torch_on.png", "torch.png");
        BLOCK_TEXTURE_RENAMES.put("noteblock.png", "note_block.png");
        BLOCK_TEXTURE_RENAMES.put("jukebox_side.png", "jukebox_side.png"); // same
        BLOCK_TEXTURE_RENAMES.put("jukebox_top.png", "jukebox_top.png"); // same
        BLOCK_TEXTURE_RENAMES.put("trapdoor.png", "oak_trapdoor.png");
        BLOCK_TEXTURE_RENAMES.put("trip_wire.png", "tripwire.png");
        BLOCK_TEXTURE_RENAMES.put("trip_wire_source.png", "tripwire_hook.png");
        BLOCK_TEXTURE_RENAMES.put("rail_normal.png", "rail.png");
        BLOCK_TEXTURE_RENAMES.put("rail_normal_turned.png", "rail_corner.png");
        BLOCK_TEXTURE_RENAMES.put("rail_golden.png", "powered_rail.png");
        BLOCK_TEXTURE_RENAMES.put("rail_golden_powered.png", "powered_rail_on.png");
        BLOCK_TEXTURE_RENAMES.put("rail_detector.png", "detector_rail.png");
        BLOCK_TEXTURE_RENAMES.put("rail_detector_powered.png", "detector_rail_on.png");
        BLOCK_TEXTURE_RENAMES.put("rail_activator.png", "activator_rail.png");
        BLOCK_TEXTURE_RENAMES.put("rail_activator_powered.png", "activator_rail_on.png");
        BLOCK_TEXTURE_RENAMES.put("dispenser_front_horizontal.png", "dispenser_front.png");
        BLOCK_TEXTURE_RENAMES.put("dispenser_front_vertical.png", "dispenser_front_vertical.png"); // same
        BLOCK_TEXTURE_RENAMES.put("dropper_front_horizontal.png", "dropper_front.png");
        BLOCK_TEXTURE_RENAMES.put("dropper_front_vertical.png", "dropper_front_vertical.png"); // same
        BLOCK_TEXTURE_RENAMES.put("command_block.png", "command_block_side.png");
        BLOCK_TEXTURE_RENAMES.put("sponge_wet.png", "wet_sponge.png");

        // =====================================================================
        // ITEM TEXTURE RENAMES
        // =====================================================================

        // Tools
        ITEM_TEXTURE_RENAMES.put("wood_sword.png", "wooden_sword.png");
        ITEM_TEXTURE_RENAMES.put("wood_pickaxe.png", "wooden_pickaxe.png");
        ITEM_TEXTURE_RENAMES.put("wood_axe.png", "wooden_axe.png");
        ITEM_TEXTURE_RENAMES.put("wood_shovel.png", "wooden_shovel.png");
        ITEM_TEXTURE_RENAMES.put("wood_hoe.png", "wooden_hoe.png");
        ITEM_TEXTURE_RENAMES.put("stone_sword.png", "stone_sword.png"); // same
        ITEM_TEXTURE_RENAMES.put("stone_pickaxe.png", "stone_pickaxe.png"); // same
        ITEM_TEXTURE_RENAMES.put("stone_axe.png", "stone_axe.png"); // same
        ITEM_TEXTURE_RENAMES.put("stone_shovel.png", "stone_shovel.png"); // same
        ITEM_TEXTURE_RENAMES.put("stone_hoe.png", "stone_hoe.png"); // same

        // Food
        ITEM_TEXTURE_RENAMES.put("apple_golden.png", "golden_apple.png");
        ITEM_TEXTURE_RENAMES.put("carrot_golden.png", "golden_carrot.png");
        ITEM_TEXTURE_RENAMES.put("melon.png", "melon_slice.png");
        ITEM_TEXTURE_RENAMES.put("porkchop_raw.png", "porkchop.png");
        ITEM_TEXTURE_RENAMES.put("porkchop_cooked.png", "cooked_porkchop.png");
        ITEM_TEXTURE_RENAMES.put("beef_raw.png", "beef.png");
        ITEM_TEXTURE_RENAMES.put("beef_cooked.png", "cooked_beef.png");
        ITEM_TEXTURE_RENAMES.put("chicken_raw.png", "chicken.png");
        ITEM_TEXTURE_RENAMES.put("chicken_cooked.png", "cooked_chicken.png");
        ITEM_TEXTURE_RENAMES.put("fish_raw.png", "cod.png");
        ITEM_TEXTURE_RENAMES.put("fish_cooked.png", "cooked_cod.png");
        ITEM_TEXTURE_RENAMES.put("fish_raw_salmon.png", "salmon.png");
        ITEM_TEXTURE_RENAMES.put("fish_cooked_salmon.png", "cooked_salmon.png");
        ITEM_TEXTURE_RENAMES.put("fish_raw_clownfish.png", "tropical_fish.png");
        ITEM_TEXTURE_RENAMES.put("fish_pufferfish_raw.png", "pufferfish.png");
        ITEM_TEXTURE_RENAMES.put("mutton_raw.png", "mutton.png");
        ITEM_TEXTURE_RENAMES.put("mutton_cooked.png", "cooked_mutton.png");
        ITEM_TEXTURE_RENAMES.put("rabbit_raw.png", "rabbit.png");
        ITEM_TEXTURE_RENAMES.put("rabbit_cooked.png", "cooked_rabbit.png");

        // Dyes
        ITEM_TEXTURE_RENAMES.put("dye_powder_black.png", "ink_sac.png");
        ITEM_TEXTURE_RENAMES.put("dye_powder_red.png", "red_dye.png");
        ITEM_TEXTURE_RENAMES.put("dye_powder_green.png", "green_dye.png");
        ITEM_TEXTURE_RENAMES.put("dye_powder_brown.png", "cocoa_beans.png");
        ITEM_TEXTURE_RENAMES.put("dye_powder_blue.png", "lapis_lazuli.png");
        ITEM_TEXTURE_RENAMES.put("dye_powder_purple.png", "purple_dye.png");
        ITEM_TEXTURE_RENAMES.put("dye_powder_cyan.png", "cyan_dye.png");
        ITEM_TEXTURE_RENAMES.put("dye_powder_silver.png", "light_gray_dye.png");
        ITEM_TEXTURE_RENAMES.put("dye_powder_gray.png", "gray_dye.png");
        ITEM_TEXTURE_RENAMES.put("dye_powder_pink.png", "pink_dye.png");
        ITEM_TEXTURE_RENAMES.put("dye_powder_lime.png", "lime_dye.png");
        ITEM_TEXTURE_RENAMES.put("dye_powder_yellow.png", "yellow_dye.png");
        ITEM_TEXTURE_RENAMES.put("dye_powder_light_blue.png", "light_blue_dye.png");
        ITEM_TEXTURE_RENAMES.put("dye_powder_magenta.png", "magenta_dye.png");
        ITEM_TEXTURE_RENAMES.put("dye_powder_orange.png", "orange_dye.png");
        ITEM_TEXTURE_RENAMES.put("dye_powder_white.png", "bone_meal.png");

        // Records / Music discs
        ITEM_TEXTURE_RENAMES.put("record_13.png", "music_disc_13.png");
        ITEM_TEXTURE_RENAMES.put("record_cat.png", "music_disc_cat.png");
        ITEM_TEXTURE_RENAMES.put("record_blocks.png", "music_disc_blocks.png");
        ITEM_TEXTURE_RENAMES.put("record_chirp.png", "music_disc_chirp.png");
        ITEM_TEXTURE_RENAMES.put("record_far.png", "music_disc_far.png");
        ITEM_TEXTURE_RENAMES.put("record_mall.png", "music_disc_mall.png");
        ITEM_TEXTURE_RENAMES.put("record_mellohi.png", "music_disc_mellohi.png");
        ITEM_TEXTURE_RENAMES.put("record_stal.png", "music_disc_stal.png");
        ITEM_TEXTURE_RENAMES.put("record_strad.png", "music_disc_strad.png");
        ITEM_TEXTURE_RENAMES.put("record_ward.png", "music_disc_ward.png");
        ITEM_TEXTURE_RENAMES.put("record_11.png", "music_disc_11.png");
        ITEM_TEXTURE_RENAMES.put("record_wait.png", "music_disc_wait.png");

        // Misc items
        ITEM_TEXTURE_RENAMES.put("door_wood.png", "oak_door.png");
        ITEM_TEXTURE_RENAMES.put("door_iron.png", "iron_door.png");
        ITEM_TEXTURE_RENAMES.put("boat.png", "oak_boat.png");
        ITEM_TEXTURE_RENAMES.put("sign.png", "oak_sign.png");
        ITEM_TEXTURE_RENAMES.put("bed.png", "red_bed.png");
        ITEM_TEXTURE_RENAMES.put("minecart_normal.png", "minecart.png");
        ITEM_TEXTURE_RENAMES.put("minecart_chest.png", "chest_minecart.png");
        ITEM_TEXTURE_RENAMES.put("minecart_furnace.png", "furnace_minecart.png");
        ITEM_TEXTURE_RENAMES.put("minecart_tnt.png", "tnt_minecart.png");
        ITEM_TEXTURE_RENAMES.put("minecart_hopper.png", "hopper_minecart.png");
        ITEM_TEXTURE_RENAMES.put("minecart_command_block.png", "command_block_minecart.png");
        ITEM_TEXTURE_RENAMES.put("fireball.png", "fire_charge.png");
        ITEM_TEXTURE_RENAMES.put("seeds_wheat.png", "wheat_seeds.png");
        ITEM_TEXTURE_RENAMES.put("seeds_melon.png", "melon_seeds.png");
        ITEM_TEXTURE_RENAMES.put("seeds_pumpkin.png", "pumpkin_seeds.png");
        ITEM_TEXTURE_RENAMES.put("seeds_beetroot.png", "beetroot_seeds.png");
        ITEM_TEXTURE_RENAMES.put("bucket_water.png", "water_bucket.png");
        ITEM_TEXTURE_RENAMES.put("bucket_lava.png", "lava_bucket.png");
        ITEM_TEXTURE_RENAMES.put("bucket_milk.png", "milk_bucket.png");
        ITEM_TEXTURE_RENAMES.put("potion_bottle_drinkable.png", "potion.png");
        ITEM_TEXTURE_RENAMES.put("potion_bottle_splash.png", "splash_potion.png");
        ITEM_TEXTURE_RENAMES.put("potion_bottle_lingering.png", "lingering_potion.png");
        ITEM_TEXTURE_RENAMES.put("potion_bottle_empty.png", "glass_bottle.png");
        ITEM_TEXTURE_RENAMES.put("spawn_egg.png", "spawn_egg.png"); // same
        ITEM_TEXTURE_RENAMES.put("spawn_egg_overlay.png", "spawn_egg_overlay.png"); // same
        ITEM_TEXTURE_RENAMES.put("comparator.png", "comparator.png"); // same
        ITEM_TEXTURE_RENAMES.put("repeater.png", "repeater.png"); // same
        ITEM_TEXTURE_RENAMES.put("string.png", "string.png"); // same
        ITEM_TEXTURE_RENAMES.put("reeds.png", "sugar_cane.png");

        // Armor
        ITEM_TEXTURE_RENAMES.put("leather_helmet.png", "leather_helmet.png"); // same
        ITEM_TEXTURE_RENAMES.put("chainmail_helmet.png", "chainmail_helmet.png"); // same
        ITEM_TEXTURE_RENAMES.put("iron_helmet.png", "iron_helmet.png"); // same
        ITEM_TEXTURE_RENAMES.put("gold_helmet.png", "golden_helmet.png");
        ITEM_TEXTURE_RENAMES.put("gold_chestplate.png", "golden_chestplate.png");
        ITEM_TEXTURE_RENAMES.put("gold_leggings.png", "golden_leggings.png");
        ITEM_TEXTURE_RENAMES.put("gold_boots.png", "golden_boots.png");
        ITEM_TEXTURE_RENAMES.put("gold_sword.png", "golden_sword.png");
        ITEM_TEXTURE_RENAMES.put("gold_pickaxe.png", "golden_pickaxe.png");
        ITEM_TEXTURE_RENAMES.put("gold_axe.png", "golden_axe.png");
        ITEM_TEXTURE_RENAMES.put("gold_shovel.png", "golden_shovel.png");
        ITEM_TEXTURE_RENAMES.put("gold_hoe.png", "golden_hoe.png");
        ITEM_TEXTURE_RENAMES.put("gold_horse_armor.png", "golden_horse_armor.png");
        ITEM_TEXTURE_RENAMES.put("gold_ingot.png", "gold_ingot.png"); // same
        ITEM_TEXTURE_RENAMES.put("gold_nugget.png", "gold_nugget.png"); // same

        // =====================================================================
        // ENTITY TEXTURE RENAMES
        // =====================================================================
        ENTITY_TEXTURE_RENAMES.put("chest/normal.png", "chest/normal.png"); // same
        ENTITY_TEXTURE_RENAMES.put("chest/ender.png", "chest/ender.png"); // same
        ENTITY_TEXTURE_RENAMES.put("chest/trapped.png", "chest/trapped.png"); // same
        // Double chest textures (normal_double, trapped_double, christmas_double)
        // are handled by ChestTextureTransformer which splits them into left/right
        // halves with proper UV remapping. Do NOT add simple renames for these.

        // =====================================================================
        // GUI RENAMES (old path -> modern path, relative to textures/)
        // =====================================================================
        // options_background was renamed to menu_background
        GUI_TEXTURE_RENAMES.put("textures/gui/options_background.png", "textures/gui/menu_background.png");
        // achievement directory was renamed to advancements in 1.12
        GUI_TEXTURE_RENAMES.put("textures/gui/achievement/achievement_background.png", "textures/gui/advancements/window.png");

        // =====================================================================
        // MISC PATH RENAMES
        // =====================================================================
        MISC_RENAMES.put("mcpatcher/", "optifine/");

        // =====================================================================
        // CROSS-DIRECTORY RENAMES (items/ -> gui/sprites/ etc.)
        // Armor slot icons: were item textures in 1.7/1.8, now GUI sprites
        // =====================================================================
        CROSS_DIRECTORY_RENAMES.put("textures/items/empty_armor_slot_helmet.png", "textures/gui/sprites/container/slot/helmet.png");
        CROSS_DIRECTORY_RENAMES.put("textures/items/empty_armor_slot_chestplate.png", "textures/gui/sprites/container/slot/chestplate.png");
        CROSS_DIRECTORY_RENAMES.put("textures/items/empty_armor_slot_leggings.png", "textures/gui/sprites/container/slot/leggings.png");
        CROSS_DIRECTORY_RENAMES.put("textures/items/empty_armor_slot_boots.png", "textures/gui/sprites/container/slot/boots.png");
    }

    // Reverse lookup maps: modern name -> legacy name (built from the forward maps above)
    private static final Map<String, String> REVERSE_BLOCK = new HashMap<>();
    private static final Map<String, String> REVERSE_ITEM = new HashMap<>();
    private static final Map<String, String> REVERSE_ENTITY = new HashMap<>();
    private static final Map<String, String> REVERSE_GUI = new HashMap<>();

    static {
        // Build reverse maps (modern -> legacy), skipping entries where old == new
        for (Map.Entry<String, String> e : BLOCK_TEXTURE_RENAMES.entrySet()) {
            if (!e.getKey().equals(e.getValue())) {
                REVERSE_BLOCK.put(stripExtension(e.getValue()), stripExtension(e.getKey()));
            }
        }
        for (Map.Entry<String, String> e : ITEM_TEXTURE_RENAMES.entrySet()) {
            if (!e.getKey().equals(e.getValue())) {
                REVERSE_ITEM.put(stripExtension(e.getValue()), stripExtension(e.getKey()));
            }
        }
        for (Map.Entry<String, String> e : ENTITY_TEXTURE_RENAMES.entrySet()) {
            if (!e.getKey().equals(e.getValue())) {
                REVERSE_ENTITY.put(stripExtension(e.getValue()), stripExtension(e.getKey()));
            }
        }
        for (Map.Entry<String, String> e : GUI_TEXTURE_RENAMES.entrySet()) {
            if (!e.getKey().equals(e.getValue())) {
                REVERSE_GUI.put(stripExtension(e.getValue()), stripExtension(e.getKey()));
            }
        }
    }

    private static String stripExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        return dot > 0 ? filename.substring(0, dot) : filename;
    }

    // Forward lookup maps: legacy name (no ext) -> modern name (no ext)
    private static final Map<String, String> FORWARD_BLOCK = new HashMap<>();
    private static final Map<String, String> FORWARD_ITEM = new HashMap<>();
    private static final Map<String, String> FORWARD_ENTITY = new HashMap<>();

    static {
        for (Map.Entry<String, String> e : BLOCK_TEXTURE_RENAMES.entrySet()) {
            if (!e.getKey().equals(e.getValue())) {
                FORWARD_BLOCK.put(stripExtension(e.getKey()), stripExtension(e.getValue()));
            }
        }
        for (Map.Entry<String, String> e : ITEM_TEXTURE_RENAMES.entrySet()) {
            if (!e.getKey().equals(e.getValue())) {
                FORWARD_ITEM.put(stripExtension(e.getKey()), stripExtension(e.getValue()));
            }
        }
        for (Map.Entry<String, String> e : ENTITY_TEXTURE_RENAMES.entrySet()) {
            if (!e.getKey().equals(e.getValue())) {
                FORWARD_ENTITY.put(stripExtension(e.getKey()), stripExtension(e.getValue()));
            }
        }
    }

    /**
     * Given a modern Identifier, returns the equivalent legacy Identifier
     * that an old resource pack would use. Returns null if no translation exists.
     *
     * Example: "textures/block/grass_block_side.png" -> "textures/blocks/grass_side.png"
     */
    public static Identifier toLegacyIdentifier(Identifier modern) {
        String path = modern.getPath();

        // Don't translate paths that are already legacy (prevents recursion)
        if (path.startsWith("textures/blocks/") || path.startsWith("textures/items/")) {
            return null;
        }

        String legacyPath = toLegacyPath(path);
        if (legacyPath != null) {
            return Identifier.of(modern.getNamespace(), legacyPath);
        }
        return null;
    }

    /**
     * Given a legacy Identifier (from an old pack), returns the modern equivalent.
     * Used by findResources to report discovered legacy files under modern names.
     *
     * Example: "textures/blocks/grass_side.png" -> "textures/block/grass_block_side.png"
     */
    public static Identifier toModernIdentifier(Identifier legacy) {
        String path = legacy.getPath();
        String modernPath = toModernPath(path);
        if (modernPath != null) {
            return Identifier.of(legacy.getNamespace(), modernPath);
        }
        return null;
    }

    /**
     * Translates a modern resource prefix to its legacy equivalent.
     * Used by findResources to also scan legacy directories.
     *
     * Example: "textures/block" -> "textures/blocks"
     */
    public static String toLegacyPrefix(String prefix) {
        if (prefix.equals("textures/block") || prefix.equals("textures/block/")) {
            return "textures/blocks";
        }
        if (prefix.equals("textures/item") || prefix.equals("textures/item/")) {
            return "textures/items";
        }
        // For the general "textures" prefix, we can't just remap -
        // the findResources call with "textures" already scans everything
        return null;
    }

    private static String toLegacyPath(String path) {
        // Block textures: textures/block/X -> textures/blocks/OLD_X
        if (path.startsWith("textures/block/")) {
            String name = path.substring("textures/block/".length());
            String nameNoExt = stripExtension(name);
            String ext = name.substring(nameNoExt.length());
            String legacyName = REVERSE_BLOCK.get(nameNoExt);
            if (legacyName != null) {
                return "textures/blocks/" + legacyName + ext;
            } else {
                // Even if no file rename, the folder changed (block -> blocks)
                return "textures/blocks/" + name;
            }
        }
        // Item textures: textures/item/X -> textures/items/OLD_X
        if (path.startsWith("textures/item/")) {
            String name = path.substring("textures/item/".length());
            String nameNoExt = stripExtension(name);
            String ext = name.substring(nameNoExt.length());
            String legacyName = REVERSE_ITEM.get(nameNoExt);
            if (legacyName != null) {
                return "textures/items/" + legacyName + ext;
            } else {
                return "textures/items/" + name;
            }
        }
        // Entity textures (folder didn't change, just file names)
        if (path.startsWith("textures/entity/")) {
            String sub = path.substring("textures/entity/".length());
            String subNoExt = stripExtension(sub);
            String ext = sub.substring(subNoExt.length());
            String legacyName = REVERSE_ENTITY.get(subNoExt);
            if (legacyName != null) {
                return "textures/entity/" + legacyName + ext;
            }
        }
        // GUI path renames (modern -> legacy)
        for (Map.Entry<String, String> e : GUI_TEXTURE_RENAMES.entrySet()) {
            if (path.equals(e.getValue())) {
                return e.getKey();
            }
        }
        // Cross-directory renames (modern -> legacy, e.g. gui/sprites/slot/helmet -> items/empty_armor_slot_helmet)
        for (Map.Entry<String, String> e : CROSS_DIRECTORY_RENAMES.entrySet()) {
            if (path.equals(e.getValue())) {
                return e.getKey();
            }
        }
        return null;
    }

    private static String toModernPath(String path) {
        // Legacy block textures: textures/blocks/OLD_X -> textures/block/X
        if (path.startsWith("textures/blocks/")) {
            String name = path.substring("textures/blocks/".length());
            String nameNoExt = stripExtension(name);
            String ext = name.substring(nameNoExt.length());
            String modernName = FORWARD_BLOCK.get(nameNoExt);
            if (modernName != null) {
                return "textures/block/" + modernName + ext;
            } else {
                // File name didn't change, just the folder
                return "textures/block/" + name;
            }
        }
        // Legacy item textures: textures/items/OLD_X -> textures/item/X
        if (path.startsWith("textures/items/")) {
            String name = path.substring("textures/items/".length());
            String nameNoExt = stripExtension(name);
            String ext = name.substring(nameNoExt.length());
            String modernName = FORWARD_ITEM.get(nameNoExt);
            if (modernName != null) {
                return "textures/item/" + modernName + ext;
            } else {
                return "textures/item/" + name;
            }
        }
        // Legacy entity textures (folder same, file names changed)
        if (path.startsWith("textures/entity/")) {
            String sub = path.substring("textures/entity/".length());
            String subNoExt = stripExtension(sub);
            String ext = sub.substring(subNoExt.length());
            String modernName = FORWARD_ENTITY.get(subNoExt);
            if (modernName != null) {
                return "textures/entity/" + modernName + ext;
            }
        }
        // GUI path renames (legacy -> modern)
        for (Map.Entry<String, String> e : GUI_TEXTURE_RENAMES.entrySet()) {
            if (path.equals(e.getKey())) {
                return e.getValue();
            }
        }
        // Cross-directory renames (legacy -> modern)
        for (Map.Entry<String, String> e : CROSS_DIRECTORY_RENAMES.entrySet()) {
            if (path.equals(e.getKey())) {
                return e.getValue();
            }
        }
        return null;
    }
}
