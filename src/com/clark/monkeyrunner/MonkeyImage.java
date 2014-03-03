package com.clark.monkeyrunner;

import com.android.chimpchat.core.IChimpImage;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * Date: 14-3-3
 */
public class MonkeyImage {
    private static Logger LOG = Logger.getLogger(MonkeyImage.class.getCanonicalName());
    private final IChimpImage impl;

    public MonkeyImage(IChimpImage image) {
        impl = image;
    }

    public IChimpImage getImpl() {
        return impl;
    }

    public byte[] convertToBytes(String format) {
        return impl.convertToBytes(format);
    }

    public boolean writeToFile(String path, String format) {
        return impl.writeToFile(path, format);
    }

    public int getRawPixel(int x, int y) {
        return impl.getPixel(x, y);
    }

    public boolean sameAs(MonkeyImage other, double percent) {
        IChimpImage other_ = other.getImpl();
        return impl.sameAs(other_, percent);
    }

    public MonkeyImage getSubImage(int x, int y, int w, int h) {
        IChimpImage image = impl.getSubImage(x, y, w, h);
        return new MonkeyImage(image);
    }
}
