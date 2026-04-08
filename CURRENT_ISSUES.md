# Legacy Packs - Current Issues

## Project Overview
Fabric mod for Minecraft 1.21.11 that translates old 1.7/1.8 resource pack files to work with modern Minecraft.

## Current Problems

### Chest entity textures (single chest)
In Minecraft 1.15, the chest model's renderer was rewritten and the UV mapping changed. Old resource pack chest textures (textures/entity/chest/normal.png, trapped.png, ender.png, christmas.png) render incorrectly in modern Minecraft — the inside of the chest is shown on the outside, and vice versa. The top/bottom, front/back, and left/right face textures are in the wrong positions on the UV sheet compared to what the new renderer expects.

A ChestTextureTransformer class exists that attempts to swap face UV regions (Top↔Bottom, North↔South, West↔East), but it does not fix the problem. The exact pixel remapping needed between the old 64x64 chest texture layout and the new 1.15+ layout has not been determined correctly yet.

### Double chest textures
Old packs use a single 128x64 texture (normal_double.png) for double chests. Modern Minecraft expects two separate 64x64 textures (normal_left.png + normal_right.png). The mod has splitting code in ChestTextureTransformer but it has not been tested successfully yet because the test resource pack doesn't include a normal_double.png file.