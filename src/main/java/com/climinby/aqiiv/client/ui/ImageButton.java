package com.climinby.aqiiv.client.ui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

public class ImageButton extends Button {
    public final Image general;
    public final Image enter;
    public final Image selected;

    public ImageButton(Image general, Image enter, Image selected) {
        super();
        this.general = general;
        this.enter = enter;
        this.selected = selected;
    }

    public ImageButton(Image general, Image enter, Image selected, String text) {
        super(text);
        this.general = general;
        this.enter = enter;
        this.selected = selected;
    }

    public ImageButton(Image general, Image enter, Image selected, String text, Node graphic) {
        super(text, graphic);
        this.general = general;
        this.enter = enter;
        this.selected = selected;
    }
}
