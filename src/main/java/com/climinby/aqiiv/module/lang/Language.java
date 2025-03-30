package com.climinby.aqiiv.module.lang;

import javafx.scene.text.Font;
import lombok.Getter;

import java.io.InputStream;
import java.util.Locale;

@Getter
public enum Language {
    ENGLISH_US(0, 1, "en_us", "English (US)", Locale.US, "/fonts/NotoSerif/en/NotoSerif-Regular.ttf"),
    ENGLISH_GB(1, 1, "en_gb", "English (UK)", Locale.UK, "/fonts/NotoSerif/en/NotoSerif-Regular.ttf"),
    CHINESE_SIMPLIFIED(2, 1, "zh_cn", "简体中文", Locale.SIMPLIFIED_CHINESE, "/fonts/NotoSerif/cn/NotoSerifSC-Regular.ttf"),
    CANTONESE(3, 1, "zh_hk", "繁體中文(香港)", Locale.TRADITIONAL_CHINESE, "/fonts/NotoSerif/hk/NotoSerifHK-Regular.ttf"),
    JAPANESE(4, 1, "jp_jp", "日本語", Locale.JAPANESE, "/fonts/NotoSerif/jp/NotoSerifJP-Regular.ttf");

    private final int id;
    private final double fontSizeFactor;
    private final String code;
    private final String name;
    private final Locale locale;
    private final String fontPath;

    Language(int id, double fontSizeFactor, String code, String name, Locale locale, String fontPath) {
        this.id = id;
        this.fontSizeFactor = fontSizeFactor;
        this.code = code;
        this.name = name;
        this.locale = locale;
        this.fontPath = fontPath;
    }

    private static Font getFontFromStream(InputStream inputStream) {
        return Font.loadFont(inputStream, 20);
    }

    public InputStream getTranslateStream() {
        return getClass().getResourceAsStream("/lang/" + this.code + ".json");
    }
}
