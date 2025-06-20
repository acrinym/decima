package com.shade.decima.ui.data.viewer.texture.controls;

import com.shade.decima.ui.data.viewer.texture.util.Channel;
import com.shade.util.NotNull;
import com.shade.util.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

/**
 * Simple image provider backed by a {@link BufferedImage} loaded from disk.
 */
public class BufferedImageProvider implements ImageProvider {
    private final BufferedImage image;
    private final String name;

    public BufferedImageProvider(@NotNull BufferedImage image, @Nullable String name) {
        this.image = image;
        this.name = name;
    }

    public static BufferedImageProvider load(@NotNull File file) throws IOException {
        return new BufferedImageProvider(ImageIO.read(file), file.getName());
    }

    @Override
    @NotNull
    public BufferedImage getImage(int mip, int slice) {
        if (mip != 0 || slice != 0) {
            throw new IllegalArgumentException("Invalid mip or slice");
        }
        return image;
    }

    @Override
    @NotNull
    public ByteBuffer getData(int mip, int slice) {
        if (mip != 0 || slice != 0) {
            throw new IllegalArgumentException("Invalid mip or slice");
        }
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
        ByteBuffer buffer = ByteBuffer.allocate(width * height * 4);
        for (int p : pixels) {
            buffer.put((byte) ((p >> 16) & 0xFF));
            buffer.put((byte) ((p >> 8) & 0xFF));
            buffer.put((byte) (p & 0xFF));
            buffer.put((byte) ((p >> 24) & 0xFF));
        }
        buffer.flip();
        return buffer;
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public int getMipCount() {
        return 1;
    }

    @Override
    public int getStreamedMipCount() {
        return 0;
    }

    @Override
    public int getSliceCount(int mip) {
        return 1;
    }

    @Override
    public int getDepth() {
        return 1;
    }

    @Override
    public int getArraySize() {
        return 1;
    }

    @Override
    @Nullable
    public String getName() {
        return name;
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.TEXTURE;
    }

    @Override
    @NotNull
    public String getPixelFormat() {
        return "RGBA8";
    }

    @Override
    @NotNull
    public Set<Channel> getChannels() {
        return EnumSet.allOf(Channel.class);
    }
}
