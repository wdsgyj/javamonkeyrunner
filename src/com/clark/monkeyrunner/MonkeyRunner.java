package com.clark.monkeyrunner;

import com.android.chimpchat.ChimpChat;
import com.android.chimpchat.core.ChimpImageBase;
import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.IChimpImage;

import javax.swing.*;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * Date: 14-3-3
 */
public class MonkeyRunner {
    private static final Logger LOG = Logger.getLogger(MonkeyRunner.class.getCanonicalName());
    private ChimpChat chimpchat;

    MonkeyRunner(ChimpChat chimpchat) {
        this.chimpchat = chimpchat;
    }

    public void shutdown() {
        chimpchat.shutdown();
    }

    public MonkeyDevice waitForConnection(long second, String deviceId) {
        long timeoutMs = second * 1000;
        IChimpDevice device = chimpchat.waitForConnection(timeoutMs,
                deviceId == null ? ".*" : deviceId);
        MonkeyDevice chimpDevice = new MonkeyDevice(device);
        return chimpDevice;
    }

    public void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            LOG.log(Level.SEVERE, "Error sleeping", e);
        }
    }

    public String help(String msg) {
        return "";
    }

    public void alert(String message, String title, String okTitle) {
        Object[] options = {okTitle};
        JOptionPane.showOptionDialog(null, message, title, JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
    }

    public int choice(String message, String title, Collection<String> choices) {
        Object[] possibleValues = choices.toArray();
        Object selectedValue = JOptionPane.showInputDialog(null, message, title,
                JOptionPane.QUESTION_MESSAGE, null, possibleValues, possibleValues[0]);

        for (int x = 0; x < possibleValues.length; x++) {
            if (possibleValues[x].equals(selectedValue)) {
                return x;
            }
        }
        // Error
        return -1;
    }

    public String input(String message, String initialValue, String title) {
        return (String) JOptionPane.showInputDialog(null, message, title,
                JOptionPane.QUESTION_MESSAGE, null, null, initialValue);
    }

    public MonkeyImage loadImageFromFile(String path) {
        IChimpImage image = ChimpImageBase.loadImageFromFile(path);
        return new MonkeyImage(image);
    }
}
