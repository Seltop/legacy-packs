package com.legacypacks;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Rebuilds the pre-1.20 title-screen logo into the modern single-row layout.
 *
 * The word "MINECRAFT" used to be split into two stacked halves inside a square
 * texture: the left half ("MINEC...") at the top, the right half ("...RAFT")
 * just below it. The old menu drew them side by side. Modern Minecraft's
 * LogoDrawer instead expects a single horizontal strip (the whole word in one
 * row) and samples a 256x44 region out of a logical 256x64 canvas - vanilla
 * ships this as a 1024x256 (4:1) file.
 *
 * Feeding an old square logo to the new code makes it scoop up both stacked
 * rows at once and squish them into the banner, producing a doubled/overlapping
 * mess. This transformer detects that old layout and lays the two halves out
 * side by side in a single row so the modern renderer draws them correctly.
 */
public class LogoTextureTransformer {

    private static final String LOGO_PATH = "textures/gui/title/minecraft.png";

    // Old layout, expressed against the classic 256x256 logical texture.
    // Each half is 155x44; the right half sits one pixel below the left (v=45).
    private static final int BASE = 256;
    private static final int HALF_W = 155;
    private static final int HALF_H = 44;
    private static final int RIGHT_V = 45;

    // Modern logical canvas is 256 wide x 64 tall, with the word in the top 44px.
    // The output file keeps the source resolution: width N -> N x (N / 4), which
    // reproduces vanilla's 4:1 proportions (a 1024 source yields 1024x256).
    private static final int MODERN_ASPECT = 4;

    // Anything already at least twice as wide as it is tall is treated as a
    // modern strip and left untouched; square-ish logos are the old format.
    private static final double MODERN_RATIO_THRESHOLD = 2.0;

    // Minimum alpha for a pixel to count as part of the word when measuring its
    // horizontal extent (skips faint anti-aliased fringe).
    private static final int ALPHA_THRESHOLD = 10;

    public static boolean isLogoPath(String path) {
        return LOGO_PATH.equals(path);
    }

    public static byte[] transformLogo(InputStream input) {
        try {
            byte[] originalBytes = input.readAllBytes();
            BufferedImage src = ImageIO.read(new ByteArrayInputStream(originalBytes));
            if (src == null) {
                return originalBytes;
            }

            // Already a modern single-row strip: nothing to do.
            if (src.getWidth() >= src.getHeight() * MODERN_RATIO_THRESHOLD) {
                return originalBytes;
            }

            double scale = src.getWidth() / (double) BASE;
            int halfSrcW = (int) Math.round(HALF_W * scale);
            int srcH = (int) Math.round(HALF_H * scale);
            int rightSrcY = (int) Math.round(RIGHT_V * scale);

            // Join the two halves side by side at native resolution. The old menu
            // drew them with their inner edges touching, so a 1:1 copy reproduces
            // the seam exactly.
            BufferedImage joined = new BufferedImage(halfSrcW * 2, srcH, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gj = joined.createGraphics();
            gj.drawImage(src, 0, 0, halfSrcW, srcH, 0, 0, halfSrcW, srcH, null);
            gj.drawImage(src, halfSrcW, 0, halfSrcW * 2, srcH,
                    0, rightSrcY, halfSrcW, rightSrcY + srcH, null);
            gj.dispose();

            // Find the word's horizontal ink bounds. Old packs often leave the
            // word left-aligned (the M hugs x=0 but the final letter stops short
            // of the right edge), which would render the logo off-centre. Vanilla
            // bleeds the word to both edges, so we crop to the ink and do the same.
            int[] pixels = joined.getRGB(0, 0, joined.getWidth(), joined.getHeight(), null, 0, joined.getWidth());
            int minX = joined.getWidth();
            int maxX = -1;
            for (int y = 0; y < joined.getHeight(); y++) {
                int row = y * joined.getWidth();
                for (int x = 0; x < joined.getWidth(); x++) {
                    if (((pixels[row + x] >>> 24) & 0xFF) > ALPHA_THRESHOLD) {
                        if (x < minX) minX = x;
                        if (x > maxX) maxX = x;
                    }
                }
            }
            if (maxX < minX) {
                // Fully transparent - nothing sensible to rebuild.
                return originalBytes;
            }
            int cropW = maxX - minX + 1;

            int outW = src.getWidth();
            int outH = Math.round(outW / (float) MODERN_ASPECT);
            // Vertical scale matches source (outH / 64 == scale), so the letter
            // height is preserved; the word is stretched across the full width.
            int dstH = srcH;

            BufferedImage out = new BufferedImage(outW, outH, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = out.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(joined,
                    0, 0, outW, dstH,
                    minX, 0, minX + cropW, srcH,
                    null);
            g.dispose();

            LegacyPacksMod.LOGGER.info("Legacy Packs: rebuilt title logo into modern single-row layout");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(out, "PNG", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            LegacyPacksMod.LOGGER.warn("Legacy Packs: Failed to transform title logo", e);
            return null;
        }
    }
}
