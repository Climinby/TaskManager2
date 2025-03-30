package com.climinby.aqiiv.data;

import java.io.File;
import java.io.IOException;

public class FileInit {
    public static void langDirInit() throws IOException {
        if(!Paths.LANG_JSON.exists()) {
            Paths.LANG_JSON.createNewFile();
        }
        if(!Paths.DATA_JSON.exists()) {
            Paths.DATA_JSON.createNewFile();
        }
    }
}
