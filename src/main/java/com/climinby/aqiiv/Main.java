package com.climinby.aqiiv;

import com.climinby.aqiiv.client.entry.ClientMain;
import com.climinby.aqiiv.data.Initializer;

public class Main {
    public static void main(String[] args) {
        Initializer.init();
        ClientMain.main(args);
    }
}
