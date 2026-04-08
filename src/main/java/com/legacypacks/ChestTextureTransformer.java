package com.legacypacks;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Transforms pre-1.15 chest textures into the UV layout expected by the modern chest renderer.
 *
 * The old chest renderer applies an extra (1, -1, -1) scale before drawing the model. Modern
 * Minecraft does not. Old chest textures were therefore authored for a different face assignment:
 *
 * - old north appears on the modern south face
 * - old south appears on the modern north face
 * - old down appears on the modern up face
 * - old up appears on the modern down face
 * - old west/east stay on the same physical side, but their UVs rotate 180 degrees
 *
 * Double chests add one more complication: old packs provide one 128x64 texture for the full
 * 30px-wide chest, while modern Minecraft expects two separate 64x64 textures for the 15px-wide
 * left and right halves.
 */
public class ChestTextureTransformer {

    private static final int SINGLE_BASE = 64;
    private static final int DOUBLE_BASE_W = 128;
    private static final int DOUBLE_BASE_H = 64;

    private static final int D = 14;
    private static final int W = 14;
    private static final int FULL_W = 30;
    private static final int HALF_W = 15;

    private static final Set<String> SINGLE_CHEST_PATHS = Set.of(
            "textures/entity/chest/normal.png",
            "textures/entity/chest/trapped.png",
            "textures/entity/chest/ender.png",
            "textures/entity/chest/christmas.png"
    );

    private static final Map<String, ChestHalfInfo> CHEST_HALVES = new HashMap<>();

    public record ChestHalfInfo(String doubleTexturePath, boolean isLeft) {}

    static {
        registerDouble("textures/entity/chest/normal_double.png",
                "textures/entity/chest/normal_left.png",
                "textures/entity/chest/normal_right.png");
        registerDouble("textures/entity/chest/trapped_double.png",
                "textures/entity/chest/trapped_left.png",
                "textures/entity/chest/trapped_right.png");
        registerDouble("textures/entity/chest/christmas_double.png",
                "textures/entity/chest/christmas_left.png",
                "textures/entity/chest/christmas_right.png");
    }

    private static void registerDouble(String doublePath, String leftPath, String rightPath) {
        CHEST_HALVES.put(leftPath, new ChestHalfInfo(doublePath, true));
        CHEST_HALVES.put(rightPath, new ChestHalfInfo(doublePath, false));
    }

    public static ChestHalfInfo getHalfInfo(String path) {
        return CHEST_HALVES.get(path);
    }

    public static Map<String, ChestHalfInfo> getAllHalves() {
        return CHEST_HALVES;
    }

    public static boolean isSingleChestPath(String path) {
        return SINGLE_CHEST_PATHS.contains(path);
    }

    public static byte[] transformSingleChest(InputStream input) {
        try {
            byte[] originalBytes = input.readAllBytes();
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(originalBytes));
            if (image == null) {
                return originalBytes;
            }

            double sx = (double) image.getWidth() / SINGLE_BASE;
            double sy = (double) image.getHeight() / SINGLE_BASE;

            BufferedImage out = copyImage(image);

            remapSingleSection(image, out, sx, sy, 0, 5, W);
            remapSingleSection(image, out, sx, sy, 19, 10, W);
            remapSingleLock(image, out, sx, sy);

            LegacyPacksMod.LOGGER.info("Legacy Packs: remapped single chest UV layout");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(out, "PNG", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            LegacyPacksMod.LOGGER.warn("Legacy Packs: Failed to transform single chest", e);
            return null;
        }
    }

    private static void remapSingleSection(BufferedImage src, BufferedImage dst,
                                           double sx, double sy,
                                           int vOff, int faceH, int w) {
        int d = s(D, sx);
        int sw = s(w, sx);
        int fH = s(faceH, sy);
        int dY = s(D, sy);
        int capY = s(vOff, sy);
        int faceY = capY + dY;

        int westX = 0;
        int northX = d;
        int eastX = d + sw;
        int southX = 2 * d + sw;

        // In the atlas, the first cap slot is DOWN and the second is UP.
        int downX = d;
        int upX = d + sw;

        copyRect(src, dst, downX, capY, sw, dY, upX, capY);
        copyRect(src, dst, upX, capY, sw, dY, downX, capY);

        copyRectRot180(src, dst, southX, faceY, sw, fH, northX, faceY);
        copyRectRot180(src, dst, northX, faceY, sw, fH, southX, faceY);
        copyRectRot180(src, dst, westX, faceY, d, fH, westX, faceY);
        copyRectRot180(src, dst, eastX, faceY, d, fH, eastX, faceY);
    }

    private static void remapSingleLock(BufferedImage src, BufferedImage dst,
                                        double sx, double sy) {
        int sideW = s(1, sx);
        int faceW = s(2, sx);
        int capH = s(1, sy);
        int lockH = s(4, sy);
        int faceY = s(1, sy);

        int downX = s(1, sx);
        int upX = s(3, sx);
        int westX = 0;
        int northX = s(1, sx);
        int eastX = s(3, sx);
        int southX = s(4, sx);

        copyRect(src, dst, downX, 0, faceW, capH, upX, 0);
        copyRect(src, dst, upX, 0, faceW, capH, downX, 0);

        copyRectRot180(src, dst, southX, faceY, faceW, lockH, northX, faceY);
        copyRectRot180(src, dst, northX, faceY, faceW, lockH, southX, faceY);
        copyRectRot180(src, dst, westX, faceY, sideW, lockH, westX, faceY);
        copyRectRot180(src, dst, eastX, faceY, sideW, lockH, eastX, faceY);
    }

    public static byte[] splitDoubleChest(InputStream doubleStream, boolean isLeft) {
        try {
            BufferedImage old = ImageIO.read(doubleStream);
            if (old == null) {
                LegacyPacksMod.LOGGER.warn("Legacy Packs: Failed to read double chest texture");
                return null;
            }

            double sx = (double) old.getWidth() / DOUBLE_BASE_W;
            double sy = (double) old.getHeight() / DOUBLE_BASE_H;

            int newW = (int) Math.round(SINGLE_BASE * sx);
            int newH = old.getHeight();
            BufferedImage out = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

            remapDoubleSection(old, out, sx, sy, 0, 5, isLeft);
            remapDoubleSection(old, out, sx, sy, 19, 10, isLeft);
            remapDoubleLock(old, out, sx, sy, isLeft);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(out, "PNG", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            LegacyPacksMod.LOGGER.warn("Legacy Packs: Failed to split double chest texture", e);
            return null;
        }
    }

    private static void remapDoubleSection(BufferedImage old, BufferedImage out,
                                           double sx, double sy,
                                           int vOff, int faceH, boolean isLeft) {
        int d = s(D, sx);
        int fullW = s(FULL_W, sx);
        int halfW = s(HALF_W, sx);
        int fH = s(faceH, sy);
        int dY = s(D, sy);
        int capY = s(vOff, sy);
        int faceY = capY + dY;

        int oldWestX = 0;
        int oldNorthX = d;
        int oldEastX = d + fullW;
        int oldSouthX = 2 * d + fullW;
        int oldDownX = d;
        int oldUpX = d + fullW;

        int newWestX = 0;
        int newNorthX = d;
        int newEastX = d + halfW;
        int newSouthX = 2 * d + halfW;
        int newDownX = d;
        int newUpX = d + halfW;

        if (isLeft) {
            copyRect(old, out, oldDownX, capY, halfW, dY, newUpX, capY);
            copyRect(old, out, oldUpX, capY, halfW, dY, newDownX, capY);

            copyRectRot180(old, out, oldNorthX, faceY, halfW, fH, newSouthX, faceY);
            copyRectRot180(old, out, oldSouthX + halfW, faceY, halfW, fH, newNorthX, faceY);

            // Left texture = viewer's left half. Its west face is the seam, east face is outer.
            fillFromColumnVFlip(old, out, oldNorthX + halfW - 1, faceY, d, fH, newWestX, faceY);
            copyRectRot180(old, out, oldEastX, faceY, d, fH, newEastX, faceY);
        } else {
            copyRect(old, out, oldDownX + halfW, capY, halfW, dY, newUpX, capY);
            copyRect(old, out, oldUpX + halfW, capY, halfW, dY, newDownX, capY);

            copyRectRot180(old, out, oldNorthX + halfW, faceY, halfW, fH, newSouthX, faceY);
            copyRectRot180(old, out, oldSouthX, faceY, halfW, fH, newNorthX, faceY);

            // Right texture = viewer's right half. Its west face is outer, east face is seam.
            copyRectRot180(old, out, oldWestX, faceY, d, fH, newWestX, faceY);
            fillFromColumnVFlip(old, out, oldNorthX + halfW, faceY, d, fH, newEastX, faceY);
        }
    }

    private static void remapDoubleLock(BufferedImage old, BufferedImage out,
                                        double sx, double sy, boolean isLeft) {
        int sideW = s(1, sx);
        int capH = s(1, sy);
        int lockH = s(4, sy);
        int faceY = s(1, sy);

        int oldDownX = s(1, sx);
        int oldUpX = s(3, sx);
        int oldWestX = 0;
        int oldNorthX = s(1, sx);
        int oldEastX = s(3, sx);
        int oldSouthX = s(4, sx);

        int newDownX = s(1, sx);
        int newUpX = s(2, sx);
        int newWestX = 0;
        int newNorthX = s(1, sx);
        int newEastX = s(2, sx);
        int newSouthX = s(3, sx);

        if (isLeft) {
            copyRect(old, out, oldDownX, 0, sideW, capH, newUpX, 0);
            copyRect(old, out, oldUpX, 0, sideW, capH, newDownX, 0);

            copyRectRot180(old, out, oldNorthX, faceY, sideW, lockH, newSouthX, faceY);
            copyRectRot180(old, out, oldSouthX + sideW, faceY, sideW, lockH, newNorthX, faceY);

            fillFromColumnVFlip(old, out, oldNorthX + sideW - 1, faceY, sideW, lockH, newWestX, faceY);
            copyRectRot180(old, out, oldEastX, faceY, sideW, lockH, newEastX, faceY);
        } else {
            copyRect(old, out, oldDownX + sideW, 0, sideW, capH, newUpX, 0);
            copyRect(old, out, oldUpX + sideW, 0, sideW, capH, newDownX, 0);

            copyRectRot180(old, out, oldNorthX + sideW, faceY, sideW, lockH, newSouthX, faceY);
            copyRectRot180(old, out, oldSouthX, faceY, sideW, lockH, newNorthX, faceY);

            copyRectRot180(old, out, oldWestX, faceY, sideW, lockH, newWestX, faceY);
            fillFromColumnVFlip(old, out, oldNorthX + sideW, faceY, sideW, lockH, newEastX, faceY);
        }
    }

    private static int s(int base, double scale) {
        return (int) Math.round(base * scale);
    }

    private static BufferedImage copyImage(BufferedImage src) {
        BufferedImage copy = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                copy.setRGB(x, y, src.getRGB(x, y));
            }
        }
        return copy;
    }

    private static void copyRect(BufferedImage src, BufferedImage dst,
                                 int srcX, int srcY, int w, int h,
                                 int dstX, int dstY) {
        w = Math.min(w, Math.min(src.getWidth() - srcX, dst.getWidth() - dstX));
        h = Math.min(h, Math.min(src.getHeight() - srcY, dst.getHeight() - dstY));
        if (w <= 0 || h <= 0) {
            return;
        }

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                dst.setRGB(dstX + x, dstY + y, src.getRGB(srcX + x, srcY + y));
            }
        }
    }

    private static void copyRectRot180(BufferedImage src, BufferedImage dst,
                                       int srcX, int srcY, int w, int h,
                                       int dstX, int dstY) {
        w = Math.min(w, Math.min(src.getWidth() - srcX, dst.getWidth() - dstX));
        h = Math.min(h, Math.min(src.getHeight() - srcY, dst.getHeight() - dstY));
        if (w <= 0 || h <= 0) {
            return;
        }

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                dst.setRGB(dstX + (w - 1 - x), dstY + (h - 1 - y), src.getRGB(srcX + x, srcY + y));
            }
        }
    }

    private static void fillFromColumnVFlip(BufferedImage src, BufferedImage dst,
                                            int srcX, int srcY, int dstW, int h,
                                            int dstX, int dstY) {
        srcX = Math.min(srcX, src.getWidth() - 1);
        h = Math.min(h, Math.min(src.getHeight() - srcY, dst.getHeight() - dstY));
        dstW = Math.min(dstW, dst.getWidth() - dstX);
        if (h <= 0 || dstW <= 0) {
            return;
        }

        for (int y = 0; y < h; y++) {
            int color = src.getRGB(srcX, srcY + y);
            for (int x = 0; x < dstW; x++) {
                dst.setRGB(dstX + x, dstY + (h - 1 - y), color);
            }
        }
    }
}
