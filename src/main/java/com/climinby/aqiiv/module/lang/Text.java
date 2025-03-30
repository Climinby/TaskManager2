package com.climinby.aqiiv.module.lang;

import com.climinby.aqiiv.util.Languages;

public class Text {
    private final Object[] args;
    private final String key;
    private final String text;

    private Text(String key, String text, Object... args) {
        this.args = args;
        this.key = key;
        this.text = text;
    }

    public static Text literal(String text, Object... args) {
        return new Text(null, text, args);
    }

    public static Text translatable(String key, Object... args) {
        return new Text(key, null, args);
    }

    @Override
    public String toString() {
        if(text != null) return text;
        String translate = Languages.translate(key);
        for(int i = 0; i < args.length; i++) {
            if(!translate.contains("%$")) break;
            String objStr = args[i].toString();
            if(objStr.contains("%$")) break;
            translate = translate.replaceFirst("%$", objStr);
        }
        return translate;
    }
}
