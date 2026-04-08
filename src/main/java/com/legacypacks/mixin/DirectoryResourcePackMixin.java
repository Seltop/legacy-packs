package com.legacypacks.mixin;

import com.legacypacks.LegacyPacksMod;
import com.legacypacks.PathMappings;
import com.legacypacks.SpriteSheetSlicing;
import com.legacypacks.TextureTransformer;
import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

@Mixin(DirectoryResourcePack.class)
public abstract class DirectoryResourcePackMixin {

    @Unique
    private static final ThreadLocal<Boolean> legacypacks_active = ThreadLocal.withInitial(() -> false);

    @Inject(method = "open(Lnet/minecraft/resource/ResourceType;Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/InputSupplier;",
            at = @At("RETURN"), cancellable = true)
    private void legacypacks_onOpen(ResourceType type, Identifier id,
                                     CallbackInfoReturnable<InputSupplier<InputStream>> cir) {
        if (type != ResourceType.CLIENT_RESOURCES || legacypacks_active.get()) {
            return;
        }

        // Container background passthrough hook for versions that need pixel remapping.
        if (cir.getReturnValue() != null && TextureTransformer.needsTransformation(id.getPath())) {
            InputSupplier<InputStream> original = cir.getReturnValue();
            String path = id.getPath();
            cir.setReturnValue(() -> {
                try (InputStream in = original.get()) {
                    byte[] transformed = TextureTransformer.transform(path, in);
                    if (transformed != null) {
                        return new ByteArrayInputStream(transformed);
                    }
                }
                return null;
            });
            return;
        }

        if (cir.getReturnValue() != null) {
            return;
        }

        legacypacks_active.set(true);
        try {
            DirectoryResourcePack self = (DirectoryResourcePack) (Object) this;

            // Try 1: Legacy path translation
            Identifier legacyId = PathMappings.toLegacyIdentifier(id);
            if (legacyId != null) {
                InputSupplier<InputStream> result = self.open(type, legacyId);
                if (result != null) {
                    LegacyPacksMod.LOGGER.info("Legacy Packs: translated {} -> {}", id, legacyId);
                    cir.setReturnValue(result);
                    return;
                }
            }

            // Try 2: Sprite sheet slicing
            String path = id.getPath();
            SpriteSheetSlicing.SliceInfo sliceInfo = SpriteSheetSlicing.getSliceInfo(path);
            if (sliceInfo != null) {
                Identifier sheetId = Identifier.of(id.getNamespace(), sliceInfo.sourceSheet());
                InputSupplier<InputStream> sheetSupplier = self.open(type, sheetId);
                if (sheetSupplier != null) {
                    cir.setReturnValue(() -> {
                        try (InputStream sheetStream = sheetSupplier.get()) {
                            byte[] sliced = SpriteSheetSlicing.sliceSprite(sheetStream, sliceInfo);
                            if (sliced != null) {
                                LegacyPacksMod.LOGGER.info("Legacy Packs: sliced {} from {}", id, sliceInfo.sourceSheet());
                                return new ByteArrayInputStream(sliced);
                            }
                        }
                        return null;
                    });
                    return;
                }
            }

            // Try 3: Provide mcmeta for virtual sprites
            if (path.endsWith(".png.mcmeta")) {
                String mcmeta = SpriteSheetSlicing.getMcmetaForPath(path);
                if (mcmeta != null) {
                    String spritePath = path.substring(0, path.length() - ".mcmeta".length());
                    SpriteSheetSlicing.SliceInfo spriteInfo = SpriteSheetSlicing.getSliceInfo(spritePath);
                    if (spriteInfo != null) {
                        Identifier sheetId = Identifier.of(id.getNamespace(), spriteInfo.sourceSheet());
                        if (self.open(type, sheetId) != null) {
                            byte[] mcmetaBytes = mcmeta.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                            cir.setReturnValue(() -> new ByteArrayInputStream(mcmetaBytes));
                        }
                    }
                }
            }
        } finally {
            legacypacks_active.set(false);
        }
    }

    @Inject(method = "findResources(Lnet/minecraft/resource/ResourceType;Ljava/lang/String;Ljava/lang/String;Lnet/minecraft/resource/ResourcePack$ResultConsumer;)V",
            at = @At("RETURN"))
    private void legacypacks_onFindResources(ResourceType type, String namespace, String prefix,
                                              ResourcePack.ResultConsumer consumer, CallbackInfo ci) {
        if (type != ResourceType.CLIENT_RESOURCES || legacypacks_active.get()) {
            return;
        }

        legacypacks_active.set(true);
        try {
            DirectoryResourcePack self = (DirectoryResourcePack) (Object) this;

            // Part 1: Legacy folder scanning
            String legacyPrefix = PathMappings.toLegacyPrefix(prefix);
            if (legacyPrefix != null) {
                self.findResources(type, namespace, legacyPrefix, (legacyId, supplier) -> {
                    Identifier modernId = PathMappings.toModernIdentifier(legacyId);
                    if (modernId != null) {
                        LegacyPacksMod.LOGGER.info("Legacy Packs: discovered {} -> {}", legacyId, modernId);
                        consumer.accept(modernId, supplier);
                    }
                });
            }

            // Part 2: Virtual sprites from sprite sheet slicing
            if (prefix.startsWith("textures/gui/sprites") || prefix.equals("textures/gui")
                    || prefix.equals("textures")) {
                String spritePrefix = prefix.startsWith("textures/gui/sprites")
                        ? prefix.substring("textures/gui/sprites".length())
                        : "";
                if (spritePrefix.startsWith("/")) {
                    spritePrefix = spritePrefix.substring(1);
                }

                for (Map.Entry<String, SpriteSheetSlicing.SliceInfo> entry :
                        SpriteSheetSlicing.getAllSlices().entrySet()) {
                    String spritePath = entry.getKey();
                    String pathInSprites = spritePath.substring("textures/gui/sprites/".length());
                    if (!spritePrefix.isEmpty() && !pathInSprites.startsWith(spritePrefix)) {
                        continue;
                    }

                    SpriteSheetSlicing.SliceInfo info = entry.getValue();
                    Identifier sheetId = Identifier.of(namespace, info.sourceSheet());
                    InputSupplier<InputStream> sheetSupplier = self.open(type, sheetId);
                    if (sheetSupplier != null) {
                        Identifier spriteId = Identifier.of(namespace, spritePath);
                        consumer.accept(spriteId, () -> {
                            try (InputStream sheetStream = sheetSupplier.get()) {
                                byte[] sliced = SpriteSheetSlicing.sliceSprite(sheetStream, info);
                                if (sliced != null) {
                                    LegacyPacksMod.LOGGER.info("Legacy Packs: sliced {} from {}", spriteId, info.sourceSheet());
                                    return new ByteArrayInputStream(sliced);
                                }
                            }
                            return null;
                        });
                    }
                }
            }

            // Part 3: Cross-directory renames (e.g., items/empty_armor_slot_* -> gui/sprites/container/slot/*)
            for (Map.Entry<String, String> entry : PathMappings.CROSS_DIRECTORY_RENAMES.entrySet()) {
                String legacyPath = entry.getKey();
                String modernPath = entry.getValue();
                if (!modernPath.startsWith(prefix)) {
                    continue;
                }
                Identifier legacyId = Identifier.of(namespace, legacyPath);
                InputSupplier<InputStream> supplier = self.open(type, legacyId);
                if (supplier != null) {
                    Identifier modernId = Identifier.of(namespace, modernPath);
                    LegacyPacksMod.LOGGER.info("Legacy Packs: cross-dir {} -> {}", legacyId, modernId);
                    consumer.accept(modernId, supplier);
                }
            }
        } finally {
            legacypacks_active.set(false);
        }
    }
}
