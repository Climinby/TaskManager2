package com.climinby.aqiiv.data;

import java.io.File;
import java.io.IOException;

public class Initializer {
    public static void init() {
        try {
            FileInit.langDirInit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
