# Legacy Packs - Current Issues

## Project Overview
Fabric mod for Minecraft 1.21.11 that translates old 1.7/1.8 resource pack files to work with modern Minecraft.

## Current Problems

### Chest entity textures (single chest)
In Minecraft 1.15, the chest model's renderer was rewritten and the UV mapping changed. Old resource pack chest textures (`textures/entity/chest/normal.png`, `trapped.png`, `ender.png`, `christmas.png`) render incorrectly in modern Minecraft. The inside of the chest is shown on the outside, and vice versa. The top/bottom, front/back, and left/right face textures are in the wrong positions on the UV sheet compared to what the new renderer expects.

The required remap is atlas-based, not a simple face swap. In 64x64 coordinates:

- `(14,0,28,14)` rotates `180 degrees` in place
- `(14,19,28,14)` rotates `180 degrees` in place
- the face strips use panel remaps rather than a whole-strip flip:
  `x=0` stays in place and rotates `180 degrees`, `x=14` moves to `x=42` and rotates `180 degrees`, `x=28` stays in place and rotates `180 degrees`, `x=42` moves to `x=14` and rotates `180 degrees`
- the same pattern applies to `y=14,h=5` and `y=33,h=10`
- the lock uses the same rule at its smaller scale

This should always be scaled from the source image dimensions so the same transform works for 16x, 32x, 64x, 128x, etc. resource packs.

### Double chest textures
Old packs use a single 128x64 texture (`normal_double.png`). Modern Minecraft expects two separate 64x64 textures (`normal_left.png` and `normal_right.png`). The mod has splitting code in `ChestTextureTransformer`, but it has not been tested successfully yet because the test resource pack does not include a `normal_double.png` file.
