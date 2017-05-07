package com.stardust.autojs.script;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.stardust.util.MapEntries;

import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Stardust on 2017/4/2.
 */

public abstract class ScriptSource implements Serializable {

    public static final int EXECUTION_MODE_NORMAL = 0;
    public static final int EXECUTION_MODE_UI = 0x00000001;
    public static final int EXECUTION_MODE_AUTO = 0x00000002;

    private static final Map<String, Integer> EXECUTION_MODES = new MapEntries<String, Integer>()
            .entry("ui", EXECUTION_MODE_UI)
            .entry("auto", EXECUTION_MODE_AUTO)
            .map();
    private static final int EXECUTION_MODE_STRING_MAX_LENGTH = 7;

    private int mExecutionMode = -1;
    private String mName;

    public ScriptSource(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    @NonNull
    public abstract String getScript();

    @Nullable
    public abstract Reader getScriptReader();

    @NonNull
    public Reader getNonNullScriptReader() {
        Reader reader = getScriptReader();
        if (reader == null) {
            return new StringReader(getScript());
        }
        return reader;
    }

    public int getExecutionMode() {
        if (mExecutionMode == -1) {
            mExecutionMode = parseExecutionMode(getScript());
        }
        return mExecutionMode;
    }

    private int parseExecutionMode(String script) {
        if (script == null || script.length() == 0 || script.charAt(0) != '"')
            return EXECUTION_MODE_NORMAL;
        int i = script.lastIndexOf("\";", EXECUTION_MODE_STRING_MAX_LENGTH + 2);
        if (i == -1)
            return EXECUTION_MODE_NORMAL;
        String modeString = script.substring(1, i);
        return parseExecutionMode(modeString.split(" "));
    }

    private int parseExecutionMode(String[] modeStrings) {
        int mode = 0;
        for (String modeString : modeStrings) {
            Integer i = EXECUTION_MODES.get(modeString);
            if (i != null) {
                mode |= i;
            }
        }
        return mode;
    }

    @Override
    public String toString() {
        return mName + ".js";
    }
}
