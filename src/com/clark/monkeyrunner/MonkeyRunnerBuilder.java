package com.clark.monkeyrunner;

import com.android.chimpchat.ChimpChat;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * Date: 14-3-3
 */
public final class MonkeyRunnerBuilder {

    public static MonkeyRunner build() {
        Map<String, String> chimp_options = new TreeMap<String, String>();
        chimp_options.put("backend", "adb");
        return new MonkeyRunner(ChimpChat.getInstance(chimp_options));
    }

}
