package com.legacypacks;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Legacy hook for container texture transforms.
 */
public class TextureTransformer {
    private static final String CREATIVE_INVENTORY_TAB =
            "textures/gui/container/creative_inventory/tab_inventory.png";

    private static final int VANILLA_TEXTURE_SIZE = 256;

    // Legacy 1.7/1.8 creative inventory survival tab regions, in 256x256 UV space.
    private static final Rect LEGACY_CLUSTER = new Rect(8, 5, 80, 50);
    private static final Rect LEGACY_LEFT_PANEL = new Rect(8, 5, 53, 50);
    private static final Rect GENERIC_SLOT = new Rect(8, 53, 26, 71);
    private static final Rect MODERN_OFFHAND_SLOT = new Rect(34, 19, 52, 37);

    private static final int CLUSTER_SHIFT_X = 45;
    private static final int LEFT_PANEL_BACKGROUND_SHIFT_X = 72;
    private static final double LEGACY_LAYOUT_THRESHOLD = 1.10;

    private record Rect(int left, int top, int right, int bottom) {
        int width() {
            return right - left;
        }

        int height() {
            return bottom - top;
        }

        boolean contains(int x, int y) {
            return x >= left && x < right && y >= top && y < bottom;
        }
    }

    /**
     * The creative inventory survival tab bakes slot art into legacy positions.
     * Modern Minecraft renders those slots in different places, so the texture
     * needs a pixel remap before it is returned to the game.
     */
    public static boolean needsTransformation(String path) {
        return CREATIVE_INVENTORY_TAB.equals(path);
    }

    /**
     * Remaps legacy creative inventory tab art into the modern slot layout.
     */
    public static byte[] transform(String path, InputStream oldTexture) {
        try {
            byte[] originalBytes = oldTexture.readAllBytes();
            if (!CREATIVE_INVENTORY_TAB.equals(path)) {
                return originalBytes;
            }

            BufferedImage source = ImageIO.read(new ByteArrayInputStream(originalBytes));
            if (source == null) {
                LegacyPacksMod.LOGGER.warn("Legacy Packs: Failed to decode {}", path);
                return originalBytes;
            }

            BufferedImage transformed = transformCreativeInventoryTab(source);

            if (transformed == source) {
                return originalBytes;
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(transformed, "PNG", out);
            return out.toByteArray();
        } catch (IOException e) {
            LegacyPacksMod.LOGGER.warn("Legacy Packs: Failed to transform {}", path, e);
            return null;
        }
    }

    private static BufferedImage transformCreativeInventoryTab(BufferedImage source) {
        if (!looksLikeLegacyCreativeLayout(source)) {
            return source;
        }

        BufferedImage output = copy(source);

        Rect legacyLeftPanel = scaleRect(source, LEGACY_LEFT_PANEL);
        Rect modernOffhandSlot = scaleRect(source, MODERN_OFFHAND_SLOT);
        int backgroundShiftX = scaleX(source, LEFT_PANEL_BACKGROUND_SHIFT_X);

        // The left-side background moved out of the way in modern versions.
        // Sample from the pack's own right-side background so gradients and
        // transparency stay consistent instead of filling with hardcoded colors.
        for (int y = legacyLeftPanel.top; y < legacyLeftPanel.bottom; y++) {
            for (int x = legacyLeftPanel.left; x < legacyLeftPanel.right; x++) {
                if (modernOffhandSlot.contains(x, y)) {
                    continue;
                }

                int sampleX = Math.min(source.getWidth() - 1, x + backgroundShiftX);
                output.setRGB(x, y, source.getRGB(sampleX, y));
            }
        }

        copyRegion(source, output, scaleRect(source, GENERIC_SLOT), modernOffhandSlot.left, modernOffhandSlot.top);

        Rect legacyCluster = scaleRect(source, LEGACY_CLUSTER);
        copyRegion(
                source,
                output,
                legacyCluster,
                legacyCluster.left + scaleX(source, CLUSTER_SHIFT_X),
                legacyCluster.top
        );

        return output;
    }

    private static boolean looksLikeLegacyCreativeLayout(BufferedImage image) {
        Rect legacyCluster = scaleRect(image, LEGACY_CLUSTER);
        Rect modernCluster = new Rect(
                legacyCluster.left + scaleX(image, CLUSTER_SHIFT_X),
                legacyCluster.top,
                legacyCluster.right + scaleX(image, CLUSTER_SHIFT_X),
                legacyCluster.bottom
        );

        double legacyDetail = detailScore(image, legacyCluster);
        double modernDetail = detailScore(image, modernCluster);

        return legacyDetail > modernDetail * LEGACY_LAYOUT_THRESHOLD;
    }

    private static double detailScore(BufferedImage image, Rect rect) {
        long total = 0L;
        long count = 0L;

        for (int y = rect.top; y < rect.bottom; y++) {
            for (int x = rect.left; x < rect.right; x++) {
                int rgba = image.getRGB(x, y);
                if (x + 1 < rect.right) {
                    total += colorDelta(rgba, image.getRGB(x + 1, y));
                    count++;
                }
                if (y + 1 < rect.bottom) {
                    total += colorDelta(rgba, image.getRGB(x, y + 1));
                    count++;
                }
            }
        }

        return count == 0L ? 0.0 : (double) total / count;
    }

    private static int colorDelta(int a, int b) {
        int aA = (a >>> 24) & 0xFF;
        int aR = (a >>> 16) & 0xFF;
        int aG = (a >>> 8) & 0xFF;
        int aB = a & 0xFF;

        int bA = (b >>> 24) & 0xFF;
        int bR = (b >>> 16) & 0xFF;
        int bG = (b >>> 8) & 0xFF;
        int bB = b & 0xFF;

        return Math.abs(aA - bA)
                + Math.abs(aR - bR)
                + Math.abs(aG - bG)
                + Math.abs(aB - bB);
    }

    private static void copyRegion(BufferedImage source, BufferedImage target, Rect sourceRect, int destX, int destY) {
        for (int y = 0; y < sourceRect.height(); y++) {
            for (int x = 0; x < sourceRect.width(); x++) {
                target.setRGB(destX + x, destY + y, source.getRGB(sourceRect.left + x, sourceRect.top + y));
            }
        }
    }

    private static BufferedImage copy(BufferedImage source) {
        BufferedImage copy = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
        copyRegion(source, copy, new Rect(0, 0, source.getWidth(), source.getHeight()), 0, 0);
        return copy;
    }

    private static Rect scaleRect(BufferedImage image, Rect rect) {
        return new Rect(
                scaleX(image, rect.left),
                scaleY(image, rect.top),
                scaleX(image, rect.right),
                scaleY(image, rect.bottom)
        );
    }

    private static int scaleX(BufferedImage image, int value) {
        return (int) Math.round(value * (double) image.getWidth() / VANILLA_TEXTURE_SIZE);
    }

    private static int scaleY(BufferedImage image, int value) {
        return (int) Math.round(value * (double) image.getHeight() / VANILLA_TEXTURE_SIZE);
    }

    public static void clearCache() {
        // No cached transforms.
    }
}
