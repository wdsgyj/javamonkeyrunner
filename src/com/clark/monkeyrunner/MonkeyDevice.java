package com.clark.monkeyrunner;

import com.android.chimpchat.core.*;
import com.android.chimpchat.hierarchyviewer.HierarchyViewer;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * Date: 14-3-3
 */
public class MonkeyDevice {
    private static final Logger LOG = Logger.getLogger(MonkeyDevice.class.getName());

    public static final String DOWN = TouchPressType.DOWN.getIdentifier();
    public static final String UP = TouchPressType.UP.getIdentifier();
    public static final String DOWN_AND_UP = TouchPressType.DOWN_AND_UP.getIdentifier();

    private IChimpDevice impl;

    public MonkeyDevice(IChimpDevice device) {
        impl = device;
    }

    public IChimpDevice getImpl() {
        return impl;
    }

    public HierarchyViewer getHierarchyViewer() {
        return impl.getHierarchyViewer();
    }

    public MonkeyImage takeSnapshot() {
        IChimpImage image = impl.takeSnapshot();
        return new MonkeyImage(image);
    }

    public String getProperty(String key) {
        return impl.getProperty(key);
    }

    public String getSystemProperty(String key) {
        return impl.getSystemProperty(key);
    }

    /**
     * 触摸事件
     * @param x 横坐标
     * @param y 纵坐标
     * @param name "down"、"up"、"downAndUp"、"move"
     */
    public void touch(int x, int y, String name) {
        TouchPressType type = TouchPressType.fromIdentifier(name);
        if (type == null) {
            if ("move".equalsIgnoreCase(name)) {
                try {
                    impl.getManager().touchMove(x, y);
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, "Error sending touch event: " + x + " " + y + " " + type, e);
                }
                return;
            } else {
                LOG.warning(String.format("Invalid TouchPressType specified (%s) default used instead",
                        name));
                type = TouchPressType.DOWN_AND_UP;
            }
        }

        impl.touch(x, y, type);
    }

    public void drag(int startx, int starty, int endx, int endy, double seconds, int steps) {
        long ms = (long) (seconds * 1000.0);
        impl.drag(startx, starty, endx, endy, steps, ms);
    }

    public void press(String name, String touchType) {
        touchType = touchType == null ? DOWN_AND_UP : touchType;

        // The old docs had this string, and so in favor of maintaining
        // backwards compatibility, let's special case it to the new one.
        if (touchType.equals("DOWN_AND_UP")) {
            touchType = "downAndUp";
        }
        TouchPressType type = TouchPressType.fromIdentifier(touchType);

        impl.press(name, type);
    }

    public void type(String message) {
        impl.type(message);
    }

    public String shell(String cmd) {
        return impl.shell(cmd);
    }

    public void reboot(String into) {
        impl.reboot(into);
    }

    public boolean installPackage(String path) {
        return impl.installPackage(path);
    }

    public boolean removePackage(String packageName) {
        return impl.removePackage(packageName);
    }

    public void startActivity(String uri, String action, String data, String mimetype, Collection<String> categories,
                              Map<String, Object> extras, String component, int flags) {
        impl.startActivity(uri, action, data, mimetype, categories, extras, component, flags);
    }

    public void broadcastIntent(String uri, String action, String data, String mimetype,
                                Collection<String> categories, Map<String, Object> extras,
                                String component, int flags) {
        impl.broadcastIntent(uri, action, data, mimetype, categories, extras, component, flags);
    }

    public Map<String, Object> instrument(String packageName, Map<String, Object> instrumentArgs) {
        if (instrumentArgs == null) {
            instrumentArgs = Collections.emptyMap();
        }
        return impl.instrument(packageName, instrumentArgs);
    }

    public void wake() {
        impl.wake();
    }

    public Collection<String> getPropertyList() {
        return impl.getPropertyList();
    }

    public Collection<String> getViewIdList() {
        return impl.getViewIdList();
    }

    public MonkeyView getViewById(String id) {
        IChimpView view = impl.getView(By.id(id));
        return new MonkeyView(view);
    }

    public MonkeyView getViewByAccessibilityIds(int windowId, int accessibilityId) {
        IChimpView view = impl.getView(By.accessibilityIds(windowId, accessibilityId));
        return new MonkeyView(view);
    }

    public MonkeyView getRootView() {
        return new MonkeyView(impl.getRootView());
    }

    public Collection<MonkeyView> getViewsByText(String text) {
        List<MonkeyView> monkeyViews = new LinkedList<MonkeyView>();
        Collection<IChimpView> views = impl.getViews(By.text(text));
        if (views != null) {
            for (IChimpView view : views) {
                monkeyViews.add(new MonkeyView(view));
            }
        }
        return monkeyViews;
    }
}
