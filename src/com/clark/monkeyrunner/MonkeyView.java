package com.clark.monkeyrunner;

import com.android.chimpchat.core.IChimpView;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * Date: 14-3-3
 */
public class MonkeyView {
    private static final Logger LOG = Logger.getLogger(MonkeyView.class.getName());

    private IChimpView impl;

    public MonkeyView(IChimpView view) {
        impl = view;
    }

    public boolean getChecked() {
        return impl.getChecked();
    }

    public String getViewClass() {
        return impl.getViewClass();
    }

    public String getText() {
        return impl.getText();
    }

    public MonkeyRect getLocation() {
        return new MonkeyRect(impl.getLocation());
    }

    public boolean getEnabled() {
        return impl.getEnabled();
    }

    public boolean getSelected() {
        return impl.getSelected();
    }

    public void setSelected(boolean selected) {
        impl.setSelected(selected);
    }

    public boolean getFocused() {
        return impl.getFocused();
    }

    public void setFocused(boolean focused) {
        impl.setFocused(focused);
    }

    public MonkeyView getParent() {
        MonkeyView parent = new MonkeyView(impl.getParent());
        return parent;
    }

    public List<MonkeyView> getChildren() {
        List<MonkeyView> monkeyViews = new LinkedList<MonkeyView>();
        List<IChimpView> chimpChildren = impl.getChildren();
        if (chimpChildren != null) {
            for (IChimpView iChimpView : chimpChildren) {
                monkeyViews.add(new MonkeyView(iChimpView));
            }
        }

        return monkeyViews;
    }

    public int[] getAccessibilityIds() {
        return impl.getAccessibilityIds();
    }


}
