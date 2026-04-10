package com.legacypacks.mixin;

import com.legacypacks.ChestTextureTransformer;
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

        // Single chest UV remapping (pack HAS the texture but UV layout changed in 1.15)
        if (cir.getReturnValue() != null && ChestTextureTransformer.isSingleChestPath(id.getPath())) {
            InputSupplier<InputStream> original = cir.getReturnValue();
            cir.setReturnValue(() -> {
                try (InputStream in = original.get()) {
                    byte[] transformed = ChestTextureTransformer.transformSingleChest(in);
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

            // Try 3: Double chest texture splitting
            String path3 = id.getPath();
            ChestTextureTransformer.ChestHalfInfo chestInfo = ChestTextureTransformer.getHalfInfo(path3);
            if (chestInfo != null) {
                Identifier doubleId = Identifier.of(id.getNamespace(), chestInfo.doubleTexturePath());
                InputSupplier<InputStream> doubleSupplier = self.open(type, doubleId);
                if (doubleSupplier != null) {
                    boolean isLeft = chestInfo.isLeft();
                    cir.setReturnValue(() -> {
                        try (InputStream doubleStream = doubleSupplier.get()) {
                            byte[] half = ChestTextureTransformer.splitDoubleChest(doubleStream, isLeft);
                            if (half != null) {
                                LegacyPacksMod.LOGGER.info("Legacy Packs: split {} from {}",
                                        id, chestInfo.doubleTexturePath());
                                return new ByteArrayInputStream(half);
                            }
                        }
                        return null;
                    });
                    return;
                }
            }

            // Try 4: Provide mcmeta for virtual sprites
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

            legacypacks_offerExistingChestTransforms(self, type, namespace, prefix, consumer);

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

            // Part 3: Virtual double chest halves
            if (prefix.startsWith("textures/entity/chest") || prefix.equals("textures/entity")
                    || prefix.equals("textures")) {
                for (Map.Entry<String, ChestTextureTransformer.ChestHalfInfo> entry :
                        ChestTextureTransformer.getAllHalves().entrySet()) {
                    String halfPath = entry.getKey();
                    if (!halfPath.startsWith(prefix)) {
                        continue;
                    }
                    ChestTextureTransformer.ChestHalfInfo info = entry.getValue();
                    Identifier doubleId = Identifier.of(namespace, info.doubleTexturePath());
                    InputSupplier<InputStream> doubleSupplier = self.open(type, doubleId);
                    if (doubleSupplier != null) {
                        Identifier halfId = Identifier.of(namespace, halfPath);
                        boolean isLeft = info.isLeft();
                        consumer.accept(halfId, () -> {
                            try (InputStream doubleStream = doubleSupplier.get()) {
                                byte[] half = ChestTextureTransformer.splitDoubleChest(doubleStream, isLeft);
                                if (half != null) {
                                    LegacyPacksMod.LOGGER.info("Legacy Packs: split {} from {}",
                                            halfId, info.doubleTexturePath());
                                    return new ByteArrayInputStream(half);
                                }
                            }
                            return null;
                        });
                    }
                }
            }

            // Part 4: Cross-directory renames (e.g., items/empty_armor_slot_* -> gui/sprites/container/slot/*)
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

    @Unique
    private static void legacypacks_offerExistingChestTransforms(DirectoryResourcePack self,
                                                                 ResourceType type,
                                                                 String namespace,
                                                                 String prefix,
                                                                 ResourcePack.ResultConsumer consumer) {
        if (!prefix.startsWith("textures/entity/chest")
                && !prefix.equals("textures/entity")
                && !prefix.equals("textures")) {
            return;
        }

        for (String path : ChestTextureTransformer.getSingleChestPaths()) {
            if (!path.startsWith(prefix)) {
                continue;
            }

            Identifier id = Identifier.of(namespace, path);
            InputSupplier<InputStream> original = self.open(type, id);
            if (original == null) {
                continue;
            }

            consumer.accept(id, () -> {
                try (InputStream in = original.get()) {
                    byte[] transformed = ChestTextureTransformer.transformSingleChest(in);
                    if (transformed != null) {
                        return new ByteArrayInputStream(transformed);
                    }
                }
                return null;
            });
        }
    }
}
