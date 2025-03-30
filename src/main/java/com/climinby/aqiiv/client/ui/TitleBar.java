package com.climinby.aqiiv.client.ui;

import com.climinby.aqiiv.client.entry.ClientMain;
import com.climinby.aqiiv.util.Schedule;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;

import java.io.InputStream;

public class TitleBar {
    private double xOffset = 0;
    private double yOffset = 0;
    @Getter
    private StackPane titlePane = null;
    @Getter
    private Button closeButton = null;
    @Getter
    private Button minimizationButton = null;
    @Getter
    private ImageButton settingsButton = null;
    @Getter
    private ImageButton tasksButton = null;
    private int titleOptionButtonCount = 0;

    public TitleBar(Stage stage) {
        this.titlePane = initTitleBar(stage);
        this.tasksButton.fire();
    }

    private StackPane initTitleBar(Stage stage) {
        //title bar
        StackPane titleBar = new StackPane();
        titleBar.setPrefHeight(50);
        titleBar.setStyle("-fx-background-color: #3D8ADD; -fx-background-radius: 10 10 0 0;");

        //main button(icon)
        Button mainButton = new Button();
        mainButton.setTranslateX(-370);
        InputStream iconImageStream = getClass().getResourceAsStream("/images/icon/icon.png");
        Image iconImage = null;
        if(iconImageStream != null) {
            iconImage = new Image(iconImageStream);
        }
        ImageView iconImageView = new ImageView(iconImage);
        iconImageView.setFitWidth(25);
        iconImageView.setFitHeight(25);
        mainButton.setGraphic(iconImageView);
        mainButton.setStyle("-fx-background-color: transparent;");
        titleBar.getChildren().add(mainButton);

        //close button(close the window)
        InputStream closeImageStream = getClass().getResourceAsStream("/images/ui/close.png");
        InputStream closeEnterImageStream = getClass().getResourceAsStream("/images/ui/close_enter.png");
        this.closeButton = addButton(
                titleBar,
                closeImageStream,
                closeEnterImageStream,
                null,
                50,
                50,
                370,
                0,
                0,
                Duration.millis(200),
                event -> {
                    Schedule.updateTasks(Schedule.getTasks());
                    stage.close();
                }
        );

        //minimization button(hide the window to back)
        InputStream minimizationImageStream = getClass().getResourceAsStream("/images/ui/minimize.png");
        InputStream minimizationEnterImageStream = getClass().getResourceAsStream("/images/ui/minimize_enter.png");
        this.minimizationButton = addButton(
                titleBar,
                minimizationImageStream,
                minimizationEnterImageStream,
                null,
                50,
                50,
                320,
                0,
                0,
                Duration.millis(200),
                event -> {
                    stage.setIconified(true);
                }
        );

        InputStream settingsImageStream = getClass().getResourceAsStream("/images/ui/settings.png");
        InputStream settingsEnterImageStream = getClass().getResourceAsStream("/images/ui/settings_enter.png");
        InputStream settingsSelectedImageStream = getClass().getResourceAsStream("/images/ui/settings_selected.png");
        this.settingsButton = addImageButton(
                titleBar,
                settingsImageStream,
                settingsEnterImageStream,
                settingsSelectedImageStream,
                50,
                50,
                30,
                0,
                0,
                Duration.millis(200),
                event -> {
                    ClientMain instance = ClientMain.INSTANCE;
                    if(instance != null) {
                        ContentArea contentArea = instance.getContentArea();
                        if(contentArea != null) {
                            contentArea.updateStatus(ContentArea.ContentStatus.SETTINGS);
                        }
                    }
                }
        );
        this.titleOptionButtonCount += 2;

        InputStream tasksImageStream = getClass().getResourceAsStream("/images/ui/tasks.png");
        InputStream tasksEnterImageStream = getClass().getResourceAsStream("/images/ui/tasks_enter.png");
        InputStream tasksSelectedImageStream = getClass().getResourceAsStream("/images/ui/tasks_selected.png");
        this.tasksButton = addImageButton(
                titleBar,
                tasksImageStream,
                tasksEnterImageStream,
                tasksSelectedImageStream,
                50,
                50,
                -30,
                0,
                0,
                Duration.millis(200),
                event -> {
                    ClientMain instance = ClientMain.INSTANCE;
                    if(instance != null) {
                        ContentArea contentArea = instance.getContentArea();
                        if(contentArea != null) {
                            contentArea.updateStatus(ContentArea.ContentStatus.TASKS);
                        }
                    }
                }
        );
        this.titleOptionButtonCount += 2;

        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        titleBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        return titleBar;
    }

    private ImageButton addImageButton(
            StackPane pane,
            InputStream generalImageStream,
            InputStream enterImageStream,
            InputStream selectedImageStream,
            double width,
            double height,
            double translateX,
            double translateY,
            double translateZ,
            Duration animationDuration,
            EventHandler<ActionEvent> onAction
    ) {
        Button fakeButton = new Button();
        fakeButton.setTranslateX(translateX);
        fakeButton.setTranslateY(translateY);
        fakeButton.setTranslateZ(translateZ);
        fakeButton.setShape(new Circle(1));
        fakeButton.setScaleX(0.8);
        fakeButton.setScaleY(0.8);
        final Image generalImage = generalImageStream != null ? new Image(generalImageStream) : null;
        final Image enterImage = enterImageStream != null ? new Image(enterImageStream) : null;
        final Image selectedImage = selectedImageStream != null ? new Image(selectedImageStream) : null;
        ImageButton button = new ImageButton(
                generalImage,
                enterImage,
                selectedImage
        );
        button.setShape(new Circle(1));
        button.setScaleX(0.8);
        button.setScaleY(0.8);
        button.setTranslateX(translateX);
        button.setTranslateY(translateY);
        button.setTranslateZ(translateZ);
        ImageView fakeImageView = new ImageView(generalImage);
        ImageView imageView = new ImageView(generalImage);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        fakeImageView.setFitWidth(width);
        fakeImageView.setFitHeight(height);
        fakeButton.setGraphic(fakeImageView);
        fakeButton.setStyle("-fx-background-color: transparent;");
        button.setGraphic(imageView);
        button.setStyle("-fx-background-color: transparent;");

        FadeTransition fadeIn = new FadeTransition(animationDuration, imageView);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        FadeTransition fadeOut = new FadeTransition(animationDuration, imageView);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        button.setOnMouseEntered(event -> {
            imageView.setImage(enterImage);
            fadeIn.play();
        });
        button.setOnMouseExited(event -> {
            fadeOut.play();
        });

        if(selectedImageStream == null) {
            button.setOnAction(onAction);
        } else {
            button.setOnAction(e -> {
                onAction.handle(e);
                ObservableList<Node> children = pane.getChildren();
                int size = children.size();
                for(int i = 0; i < size; i++) {
                    if(i >= size - this.titleOptionButtonCount) {
                        Node node = children.get(i);
                        if(node instanceof ImageButton imageButton) {
                            Image general = imageButton.general;

                            ImageView graphic = (ImageView) imageButton.getGraphic();
                            graphic.setImage(general);

                            if(node.isDisable()) {
                                node.setDisable(false);
                            }

                            Button fakeNode = (Button) children.get(i - 1);
                            ImageView fakeGraphic = (ImageView) fakeNode.getGraphic();
                            fakeGraphic.setImage(general);
                        }
                    }
                }
                button.setDisable(true);
                fakeImageView.setImage(selectedImage);
                imageView.setImage(selectedImage);
            });
        }
        pane.getChildren().add(fakeButton);
        pane.getChildren().add(button);

        return button;
    }

    private Button addButton(
            StackPane pane,
            InputStream generalImageStream,
            InputStream enterImageStream,
            InputStream selectedImageStream,
            double width,
            double height,
            double translateX,
            double translateY,
            double translateZ,
            Duration animationDuration,
            EventHandler<ActionEvent> onAction
    ) {
        Button fakeButton = new Button();
        fakeButton.setTranslateX(translateX);
        fakeButton.setTranslateY(translateY);
        fakeButton.setTranslateZ(translateZ);
        fakeButton.setShape(new Circle(1));
        fakeButton.setScaleX(0.8);
        fakeButton.setScaleY(0.8);
        Button button = new Button();
        button.setShape(new Circle(1));
        button.setScaleX(0.8);
        button.setScaleY(0.8);
        button.setTranslateX(translateX);
        button.setTranslateY(translateY);
        button.setTranslateZ(translateZ);
        final Image generalImage = generalImageStream != null ? new Image(generalImageStream) : null;
        final Image enterImage = enterImageStream != null ? new Image(enterImageStream) : null;
        ImageView fakeImageView = new ImageView(generalImage);
        ImageView imageView = new ImageView(generalImage);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        fakeImageView.setFitWidth(width);
        fakeImageView.setFitHeight(height);
        fakeButton.setGraphic(fakeImageView);
        fakeButton.setStyle("-fx-background-color: transparent;");
        button.setGraphic(imageView);
        button.setStyle("-fx-background-color: transparent;");

        FadeTransition fadeIn = new FadeTransition(animationDuration, imageView);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        FadeTransition fadeOut = new FadeTransition(animationDuration, imageView);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        button.setOnMouseEntered(event -> {
            imageView.setImage(enterImage);
            fadeIn.play();
        });
        button.setOnMouseExited(event -> {
            fadeOut.play();
        });

        if(selectedImageStream == null) {
            button.setOnAction(onAction);
        } else {
//            Button selectedButton = new Button();
            final Image selectedImage = new Image(selectedImageStream);
//            button.setPickOnBounds(false);
            button.setOnAction(e -> {
                onAction.handle(e);
                ObservableList<Node> children = pane.getChildren();
                int size = children.size();
                for(int i = 0; i < size; i++) {
                    if(i >= size - this.titleOptionButtonCount) {
                        Node node = children.get(i);
                        if(node.isDisable()) {
                            node.setDisable(false);
                        }
                    }
                }
                button.setDisable(true);
                fakeImageView.setImage(selectedImage);
                imageView.setImage(selectedImage);
            });
        }
        pane.getChildren().add(fakeButton);
        pane.getChildren().add(button);

        return button;
    }
}
