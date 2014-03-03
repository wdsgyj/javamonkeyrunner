package com.clark.monkeyrunner;

import com.android.chimpchat.core.ChimpRect;

import java.awt.*;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * Date: 14-3-3
 */
public class MonkeyRect {
    private static final Logger LOG = Logger.getLogger(MonkeyRect.class.getName());

    private ChimpRect rect;

    public int left;
    public int top;
    public int right;
    public int bottom;

    public MonkeyRect(ChimpRect rect) {
        this.rect = rect;
        this.left = rect.left;
        this.right = rect.right;
        this.top = rect.top;
        this.bottom = rect.bottom;
    }

    public int getWidth() {
        return right - left;
    }

    public int getHeight() {
        return bottom - top;
    }

    public Point getCenter() {
        return new Point(left + ((right - left) >> 1), top + ((bottom - top) >> 1));
    }
}
