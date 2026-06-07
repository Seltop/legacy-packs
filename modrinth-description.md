<!-- Summary field (one-liner under the title, max 256 chars) -->
<!-- Automatically converts old 1.7/1.8-era resource packs to the modern format, so legacy texture packs just work on current Minecraft — no manual reorganizing required. -->

# Legacy Packs

**Legacy Packs** lets you use old resource packs — the kind made for Minecraft
1.7 and 1.8 — on modern versions, without editing a single file.

Back in the day, textures lived in folders like `textures/items/` and
`textures/blocks/`, and many were packed into single sprite sheets. Modern
Minecraft expects `textures/item/`, `textures/block/`, individual sprite
files, and a different chest texture layout. Legacy Packs bridges that gap
automatically while the pack loads.

## Features
- Translates old texture paths to the modern format (e.g. `items/apple.png` → `item/apple.png`)
- Slices old combined sprite sheets into the individual textures modern Minecraft expects
- Remaps chest texture UVs and splits double-chest textures to the new format
- Works on the fly — drop in any supported pack and enable it like normal; no manual conversion

## Requirements
- Minecraft 1.21.x
- Fabric Loader 0.18.1+
- Fabric API
- Java 21+

Client-side only.
