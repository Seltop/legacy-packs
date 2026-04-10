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
 * Transforms pre-1.15 chest textures into the atlas layout expected by the modern chest renderer.
 *
 * Single chest textures are fixed by remapping the legacy 64x64 atlas, not by guessing face swaps:
 *
 * - the 28x14 regions at (14,0) and (14,19) rotate 180 degrees in place
 * - west/east strips stay in place and rotate 180 degrees
 * - north/south strips swap positions and rotate 180 degrees
 * - the lock follows the same pattern at a smaller scale
 *
 * All coordinates are scaled from the actual image size, so 16x, 32x, 64x, 128x, etc. packs all
 * use the same transform.
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

    public static Set<String> getSingleChestPaths() {
        return SINGLE_CHEST_PATHS;
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

            remapSingleCap(image, out, sx, sy, 0);
            remapSingleStrip(image, out, sx, sy, 14, 5);
            remapSingleCap(image, out, sx, sy, 19);
            remapSingleStrip(image, out, sx, sy, 33, 10);
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

    private static void remapSingleCap(BufferedImage src, BufferedImage dst,
                                       double sx, double sy, int vOff) {
        int w = s(W, sx);
        int dY = s(D, sy);
        int y = s(vOff, sy);

        // The lid/base cap row is not one 28x14 rotation. The two 14x14 halves swap places and
        // each half flips vertically.
        copyRectFlipV(src, dst, w, y, w, dY, 2 * w, y);
        copyRectFlipV(src, dst, 2 * w, y, w, dY, w, y);
    }

    private static void remapSingleStrip(BufferedImage src, BufferedImage dst,
                                         double sx, double sy,
                                         int vOff, int faceH) {
        int d = s(D, sx);
        int w = s(W, sx);
        int h = s(faceH, sy);
        int y = s(vOff, sy);

        int westX = 0;
        int northX = d;
        int eastX = d + w;
        int southX = 2 * d + w;

        copyRectRot180(src, dst, westX, y, d, h, westX, y);
        copyRectRot180(src, dst, northX, y, w, h, southX, y);
        copyRectRot180(src, dst, eastX, y, d, h, eastX, y);
        copyRectRot180(src, dst, southX, y, w, h, northX, y);
    }

    private static void remapSingleLock(BufferedImage src, BufferedImage dst,
                                        double sx, double sy) {
        int sideW = s(1, sx);
        int faceW = s(2, sx);
        int lockH = s(4, sy);
        int faceY = s(1, sy);

        int westX = 0;
        int northX = s(1, sx);
        int eastX = s(3, sx);
        int southX = s(4, sx);

        // The top row of the lock atlas already matches the modern layout.
        copyRectRot180(src, dst, westX, faceY, sideW, lockH, westX, faceY);
        copyRectRot180(src, dst, northX, faceY, faceW, lockH, southX, faceY);
        copyRectRot180(src, dst, eastX, faceY, sideW, lockH, eastX, faceY);
        copyRectRot180(src, dst, southX, faceY, faceW, lockH, northX, faceY);
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

        // Old 128x64 layout:  [empty D][Cap1 FULL_W][Cap2 FULL_W]
        //                     [West D] [North FULL_W][East D][South FULL_W]
        int oldCap1X = d;
        int oldCap2X = d + fullW;
        int oldWestX = 0;
        int oldNorthX = d;
        int oldEastX = d + fullW;
        int oldSouthX = 2 * d + fullW;

        // New 64x64 layout:   [empty D][Cap2 HALF_W][Cap1 HALF_W]  (caps swapped)
        //                     [West D] [North HALF_W][East D][South HALF_W]
        int newCap1X = d + halfW;
        int newCap2X = d;
        int newWestX = 0;
        int newNorthX = d;
        int newEastX = d + halfW;
        int newSouthX = 2 * d + halfW;

        if (isLeft) {
            // Caps: extract left half, swap + flipV
            copyRectFlipV(old, out, oldCap1X, capY, halfW, dY, newCap1X, capY);
            copyRectFlipV(old, out, oldCap2X, capY, halfW, dY, newCap2X, capY);

            // North/South: swap + flipV, extract appropriate halves
            copyRectFlipV(old, out, oldNorthX, faceY, halfW, fH, newSouthX, faceY);
            copyRectFlipV(old, out, oldSouthX + halfW, faceY, halfW, fH, newNorthX, faceY);

            // West = seam (fabricated), East = outer (rot180)
            fillFromColumnVFlip(old, out, oldNorthX + halfW - 1, faceY, d, fH, newWestX, faceY);
            copyRectRot180(old, out, oldEastX, faceY, d, fH, newEastX, faceY);
        } else {
            // Caps: extract right half, swap + flipV
            copyRectFlipV(old, out, oldCap1X + halfW, capY, halfW, dY, newCap1X, capY);
            copyRectFlipV(old, out, oldCap2X + halfW, capY, halfW, dY, newCap2X, capY);

            // North/South: swap + flipV, extract appropriate halves
            copyRectFlipV(old, out, oldNorthX + halfW, faceY, halfW, fH, newSouthX, faceY);
            copyRectFlipV(old, out, oldSouthX, faceY, halfW, fH, newNorthX, faceY);

            // West = outer (rot180), East = seam (fabricated)
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

        int oldCap1X = s(1, sx);
        int oldCap2X = s(3, sx);
        int oldWestX = 0;
        int oldNorthX = s(1, sx);
        int oldEastX = s(3, sx);
        int oldSouthX = s(4, sx);

        int newCap1X = s(2, sx);
        int newCap2X = s(1, sx);
        int newWestX = 0;
        int newNorthX = s(1, sx);
        int newEastX = s(2, sx);
        int newSouthX = s(3, sx);

        if (isLeft) {
            copyRectFlipV(old, out, oldCap1X, 0, sideW, capH, newCap1X, 0);
            copyRectFlipV(old, out, oldCap2X, 0, sideW, capH, newCap2X, 0);

            copyRectFlipV(old, out, oldNorthX, faceY, sideW, lockH, newSouthX, faceY);
            copyRectFlipV(old, out, oldSouthX + sideW, faceY, sideW, lockH, newNorthX, faceY);

            fillFromColumnVFlip(old, out, oldNorthX + sideW - 1, faceY, sideW, lockH, newWestX, faceY);
            copyRectRot180(old, out, oldEastX, faceY, sideW, lockH, newEastX, faceY);
        } else {
            copyRectFlipV(old, out, oldCap1X + sideW, 0, sideW, capH, newCap1X, 0);
            copyRectFlipV(old, out, oldCap2X + sideW, 0, sideW, capH, newCap2X, 0);

            copyRectFlipV(old, out, oldNorthX + sideW, faceY, sideW, lockH, newSouthX, faceY);
            copyRectFlipV(old, out, oldSouthX, faceY, sideW, lockH, newNorthX, faceY);

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

    private static void copyRectFlipV(BufferedImage src, BufferedImage dst,
                                      int srcX, int srcY, int w, int h,
                                      int dstX, int dstY) {
        w = Math.min(w, Math.min(src.getWidth() - srcX, dst.getWidth() - dstX));
        h = Math.min(h, Math.min(src.getHeight() - srcY, dst.getHeight() - dstY));
        if (w <= 0 || h <= 0) {
            return;
        }

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                dst.setRGB(dstX + x, dstY + (h - 1 - y), src.getRGB(srcX + x, srcY + y));
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
