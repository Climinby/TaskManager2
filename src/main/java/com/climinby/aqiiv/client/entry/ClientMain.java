package com.climinby.aqiiv.client.entry;

import com.climinby.aqiiv.client.ui.ContentArea;
import com.climinby.aqiiv.client.ui.TitleBar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;

import java.io.InputStream;

@Getter
public class ClientMain extends Application {
    public static ClientMain INSTANCE = null;
    private TitleBar titleBar = null;
    private ContentArea contentArea = null;

    @Override
    public void start(Stage stage) throws Exception {
        INSTANCE = this;

        this.titleBar = new TitleBar(stage);

        this.contentArea = new ContentArea(stage);

        VBox root = new VBox(this.titleBar.getTitlePane(), this.contentArea.getContentPane());

        Scene scene = new Scene(root, 800, 450);
        scene.setFill(Color.TRANSPARENT);

        Rectangle clip = new Rectangle(800, 450);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        root.setClip(clip);

        InputStream icon = getClass().getResourceAsStream("/images/icon/icon.png");
        if(icon != null) {
            stage.getIcons().add(new Image(icon));
        }
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Aqiiv");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
