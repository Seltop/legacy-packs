# Legacy Packs - Current Issues

## Project Overview
Fabric mod for Minecraft 1.21.11 that translates old 1.7/1.8 resource pack files to work with modern Minecraft.

## What Works
- Block/item texture path remapping (300+ renames)
- Sprite sheet slicing (widgets.png, icons.png, bars.png, tabs.png → individual sprites)
- Resolution scaling for any pack resolution
- Build system working

## Current Problem
The survival inventory GUI background texture doesn't line up with where the game actually renders slots. The crafting area graphics in old packs are drawn at different pixel positions than where modern Minecraft expects them, so the slot borders and the actual interactive slots are visually offset from each other.

The same type of issue may affect the brewing stand GUI (fuel slot area was added in later versions).
