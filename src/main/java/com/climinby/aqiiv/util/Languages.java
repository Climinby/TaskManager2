package com.climinby.aqiiv.util;

import com.climinby.aqiiv.data.Initializer;
import com.climinby.aqiiv.data.Paths;
import com.climinby.aqiiv.module.lang.Language;
import javafx.scene.text.Font;
import lombok.Getter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Languages {
    private static final Map<String, String> TRANSLATE_MAP = new HashMap<>();
    @Getter
    private static Language currLang = Language.ENGLISH_US;

    static {
        initMap();
    }

    public static void initMap() {
        Language lastLanguage = Language.ENGLISH_US;
        try {
            lastLanguage = JsonOperator.readObject(Paths.LANG_JSON, Language.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        changeLanguage(lastLanguage);
    }

    public static String translate(String key) {
        if(key == null) return "";
        String text = TRANSLATE_MAP.get(key);
        if(text == null) {
            return key;
        }
        return text;
    }

    public static boolean changeLanguage(Language language) {
        Map<String, String> newLangMap;
        Map<String, String> defaultMap;

        try {
            newLangMap = JsonOperator.readMap(language.getTranslateStream(), String.class);
            defaultMap = JsonOperator.readMap(Language.ENGLISH_US.getTranslateStream(), String.class);

            JsonOperator.writeObject(Paths.LANG_JSON, language);
        } catch (IOException e) {
            return false;
        }

        TRANSLATE_MAP.clear();
        TRANSLATE_MAP.putAll(defaultMap);
        TRANSLATE_MAP.putAll(newLangMap);

        currLang = language;


        return true;
    }
}
