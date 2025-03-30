package com.climinby.aqiiv.client.entry;

import com.climinby.aqiiv.client.ui.ContentArea;
import com.climinby.aqiiv.client.ui.TitleBar;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.Getter;

import java.io.InputStream;

public class ClientMain extends Application {
    public static ClientMain INSTANCE = null;
    @Getter
    private TitleBar titleBar = null;
    @Getter
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
