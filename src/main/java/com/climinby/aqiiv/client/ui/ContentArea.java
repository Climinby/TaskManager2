package com.climinby.aqiiv.client.ui;

import com.climinby.aqiiv.module.lang.Language;
import com.climinby.aqiiv.module.lang.Text;
import com.climinby.aqiiv.module.task.Task;
import com.climinby.aqiiv.util.Languages;
import com.climinby.aqiiv.util.Schedule;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;

import java.io.InputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ContentArea {
    @Getter
    private StackPane contentPane = null;
    @Getter
    private StackPane sideColumn = null;
    private StackPane mainColumn = null;
    private ContentStatus status = ContentStatus.TASKS;
    private VBox mainBox = null;
    private VBox tasksBox = null;
    private List<VBox> settingsBoxes = new ArrayList<>();
    private javafx.scene.text.Text titleText = null;
    private javafx.scene.text.Text descText = null;
    private javafx.scene.text.Text timeScreen = null;
    private javafx.scene.text.Text durationText = null;
    private HBox timeChooser = null;
    private Slider durationHourSlider = null;
    private Slider durationMinuteSlider = null;
    private Button deleteButton;
    private TextField titleEdit = null;
    private TextArea describeEdit = null;
    private List<ToggleButton> taskButtons = new ArrayList<>();
    private Button editButton = null;
    private Button cancelButton = null;
    private Button saveButton = null;
    private Task currTask = null;
    private ToggleButton languageButton = null;
    private ToggleButton reminderButton = null;
    private ToggleButton configButton = null;
    private double currSideWidth = 200;

    public ContentArea(Stage stage) {
        this.contentPane = initContentArea(stage);
    }

    public void updateStatus(ContentStatus status) {
        if(status != this.status) {
            this.status = status;
            Node first = ((StackPane) ((ScrollPane) this.sideColumn.getChildren().getFirst()).getContent()).getChildren().getFirst();
            FadeTransition boxFadeOutTransition = new FadeTransition(Duration.millis(100), first);
            boxFadeOutTransition.setFromValue(1);
            boxFadeOutTransition.setToValue(0);
            boxFadeOutTransition.play();
            TranslateTransition sideColumnTransition = new TranslateTransition(Duration.millis(250), this.sideColumn);
            sideColumnTransition.setByX(status.sideColumnWidth - this.sideColumn.getTranslateX() - 200);
            sideColumnTransition.setOnFinished(e -> {
                FadeTransition boxFadeInTransition = new FadeTransition(Duration.millis(300), status.box);
                boxFadeInTransition.setFromValue(0);
                boxFadeInTransition.setToValue(1);
                ((StackPane) ((ScrollPane) this.sideColumn.getChildren().getFirst()).getContent()).getChildren().set(0, status.box);
                boxFadeInTransition.play();
                int index = 0;
                if(status == ContentStatus.TASKS) {
                    if(!Schedule.getTasks().isEmpty()) {
                        index = 1;
                    }
                }
                ((ToggleButton) ((StackPane) status.box.getChildren().get(index)).getChildren().getLast()).fire();
            });
            sideColumnTransition.play();
            this.currSideWidth = status.sideColumnWidth;

            if(status == ContentStatus.TASKS) updateMainBox(this.mainBox);
            if(status == ContentStatus.SETTINGS) updateMainBox(this.settingsBoxes.getFirst());
        }
    }

    private void updateMainBox(VBox newBox) {
        TranslateTransition moveOut = new TranslateTransition(Duration.millis(200), this.mainColumn);
        moveOut.setFromX(0);
        moveOut.setFromX(-800);
        moveOut.setOnFinished(event -> {
            this.mainColumn.getChildren().set(0, newBox);
        });

        TranslateTransition moveIn = new TranslateTransition(Duration.millis(200), this.mainColumn);
        moveIn.setFromX(800);
        moveIn.setToX(0);

        SequentialTransition transition = new SequentialTransition(moveOut, moveIn);

        transition.play();
//        TranslateTransition moveOut = new TranslateTransition(Duration.millis(200), this.mainBox);
//        moveOut.setFromX(0);
//        moveOut.setFromX(-800);
//        moveOut.setOnFinished(event -> {
//            this.mainBox.getChildren().set(0, newBox);
//        });
//
//        TranslateTransition moveIn = new TranslateTransition(Duration.millis(200), this.mainBox);
//        moveIn.setFromX(800);
//        moveIn.setToX(0);
//
//        SequentialTransition transition = new SequentialTransition(moveOut, moveIn);
//
//        transition.play();
    }

    private void updateTask(Task newTask) {
        if(this.currTask == newTask && !newTask.empty()) return;
        this.currTask = newTask;

//        this.mainBox
        TranslateTransition moveOut = new TranslateTransition(Duration.millis(200), this.mainBox);
        moveOut.setFromX(0);
        moveOut.setToX(-800);
        moveOut.setOnFinished(event -> {
            if(!this.cancelButton.isDisable()) {
                cancelButton.fire();
            }
            this.titleText.setText(this.currTask.getTitle());
            this.descText.setText(cutDescString(this.currTask.getDescription()));
            if(this.currTask != null && this.currTask.getStartTime() != null) {
                timeScreen.setText(this.currTask.getStartTime().format(DateTimeFormatter.ofPattern("HH : mm : ss")));
            }
            if(this.currTask != null && this.currTask.getDuration() != null) {
                java.time.Duration duration = this.currTask.getDuration();
                this.durationHourSlider.setValue(duration.toHours());
                this.durationMinuteSlider.setValue(duration.toMinutesPart());
                this.durationText.setText(Text.literal(duration.toHours() + "hrs " + duration.toMinutesPart() + "min").toString());
            }
        });
        TranslateTransition moveIn = new TranslateTransition(Duration.millis(200), this.mainBox);
        moveIn.setFromX(800);
        moveIn.setToX(0);

        SequentialTransition transition = new SequentialTransition(moveOut, moveIn);

        transition.play();
    }

    private void initButtons(Stage stage) {
        ObservableList<Node> taskChildren = ContentStatus.TASKS.box.getChildren();
        int taskSize = taskChildren.size();
        for(int i = 0; i < taskSize; i++) {
            Node node = taskChildren.get(i);
            if(node instanceof StackPane pane) {
                if(pane.getChildren().getLast() instanceof ToggleButton taskButton) {
                    EventHandler<ActionEvent> onAction = taskButton.getOnAction();
                    if(i == 0) {
                        taskButton.setOnAction(event -> {
                            this.currTask = new Task();
                            List<Task> tasks = Schedule.getTasks();
                            tasks.add(this.currTask);
                            Schedule.updateTasks(tasks);
                            Font font = Font.loadFont(
                                    getClass().getResourceAsStream(Languages.getCurrLang().getFontPath()),
                                    Languages.getCurrLang().getFontSizeFactor() * 18
                            );
                            StackPane newOptionPane = ContentStatus.getOptionPane(
                                    160, 40, ContentStatus.TASKS.box, Text.translatable("ui.tasks.new_task"), font
                            );
                            ToggleButton newOptionButton = (ToggleButton) newOptionPane.getChildren().getLast();
                            EventHandler<ActionEvent> optionOnAction = newOptionButton.getOnAction();
                            newOptionButton.setOnAction(e -> {
                                int index = taskChildren.indexOf(newOptionPane);
                                updateTask(Schedule.getTasks().get(index - 1));
                                optionOnAction.handle(e);
                            });
                            taskChildren.add(newOptionPane);
                            this.taskButtons.add(newOptionButton);
                            newOptionButton.fire();
                            onAction.handle(event);
                        });
                    } else {
                        taskButton.setOnAction(e -> {
                            int index = taskChildren.indexOf(pane);
                            updateTask(Schedule.getTasks().get(index - 1));
                            onAction.handle(e);
                        });
                    }
                    this.taskButtons.add(taskButton);
                }
            }
        }
        VBox settingsBox = ContentStatus.SETTINGS.box;
        ObservableList<Node> settingsChildren = settingsBox.getChildren();
        int size = settingsChildren.size();
        for(int i = 0; i < size; i++) {
            ToggleButton button = (ToggleButton) ((StackPane) settingsChildren.get(i)).getChildren().getLast();
            if(i == 0) {
                this.languageButton = button;
            } else if(i == 1) {
                this.reminderButton = button;
            } else if(i == 2) {
                this.configButton = button;
            }
        }
    }

    private Rectangle getStyledRect(
            double width,
            double height,
            Color color
    ) {
        Rectangle rect = new Rectangle(width, height);
        rect.setArcWidth(20);
        rect.setArcHeight(20);
        rect.setFill(color);

        return rect;
    }

    private void setButtonSelection(Button button, Shape select, EventHandler<ActionEvent> onAction) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), select);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), select);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeIn.setOnFinished(event -> {
            if(button.isDisable()) fadeOut.play();
        });
        button.setOnMouseEntered(event -> {
            if(!button.isDisable()) {
                fadeIn.play();
            }
        });
        button.setOnMouseExited(event -> {
            if(!button.isDisable()) {
                fadeOut.play();
            }
        });
        button.setOnAction(event -> {
            button.setDisable(true);
            button.setOpacity(0);
            select.setOpacity(0);
            onAction.handle(event);
        });
    }

    private Button getSmallButton(
            double radius,
            Circle select,
            String imagePath,
            EventHandler<ActionEvent> onAction
    ) {
        Button smallButton = new Button();
        smallButton.setMaxSize(radius * 2, radius * 2);
        smallButton.setStyle("-fx-background-color: transparent;");
        InputStream resourceAsStream = imagePath == null ? null : getClass().getResourceAsStream(imagePath);
        Image image = resourceAsStream != null ? new Image(resourceAsStream) : null;
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(radius * 2);
        imageView.setFitHeight(radius * 2);
        smallButton.setGraphic(imageView);
        smallButton.setShape(new Circle(radius));

        setButtonSelection(smallButton, select, onAction);

        return smallButton;
    }

    private Button getMediumButton(
            Rectangle select,
            String imagePath,
            EventHandler<ActionEvent> onAction
    ) {
        Button mediumButton = new Button();
        double width = select.getWidth();
        double height = select.getHeight();
        mediumButton.setMaxSize(width, height);
        mediumButton.setStyle("-fx-background-color: transparent;");
        InputStream imageStream = imagePath == null ? null : getClass().getResourceAsStream(imagePath);
        Image image = imageStream == null ? null : new Image(imageStream);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        setButtonSelection(mediumButton, select, onAction);

        return mediumButton;
    }

    private List<VBox> initSettingsBoxes() {
        List<VBox> settingsBoxes = new ArrayList<>();

        settingsBoxes.add(initLanguageBox());

        return settingsBoxes;
    }

    private VBox initLanguageBox() {
        VBox langContentBox = new VBox(20);
        langContentBox.setPadding(new Insets(20, 0, 20, 0));
        langContentBox.setAlignment(Pos.CENTER);

        StackPane langsPane = new StackPane(getStyledRect(580, 240, Color.valueOf("acd7e2")));
        langsPane.setStyle("-fx-background-color: transparent;");
        langsPane.setEffect(getShadow(
                10, 0.5, 0, Color.rgb(106, 104, 104)
        ));
        langContentBox.getChildren().add(langsPane);

        VBox langBox = new VBox(1);
        langBox.setAlignment(Pos.CENTER);
        langBox.setPadding(new Insets(20, 0, 20, 0));
        langsPane.getChildren().add(langBox);

        for(Language language : Language.values()) {
            Font font = Font.loadFont(
                    getClass().getResourceAsStream(language.getFontPath()),
                    language.getFontSizeFactor() * 18
            );
            StackPane langPane = ContentStatus.getOptionPane(520, 30, langBox, Text.literal(language.getName()), font);
            ToggleButton langButton = (ToggleButton) langPane.getChildren().getLast();
            EventHandler<ActionEvent> onAction = langButton.getOnAction();
            langButton.setOnAction(event -> {
                Languages.changeLanguage(language);
                Font newSmallFont = Font.loadFont(getClass().getResourceAsStream(language.getFontPath()), language.getFontSizeFactor() * 12);
                Font newMediumFont = Font.loadFont(getClass().getResourceAsStream(language.getFontPath()), language.getFontSizeFactor() * 15);
                Font newFont = Font.loadFont(getClass().getResourceAsStream(language.getFontPath()), language.getFontSizeFactor() * 18);
                Font newBigFont = Font.loadFont(getClass().getResourceAsStream(language.getFontPath()), language.getFontSizeFactor() * 20);
                Font newLargeFont = Font.loadFont(getClass().getResourceAsStream(language.getFontPath()), language.getFontSizeFactor() * 25);
                this.languageButton.setText(Text.translatable("ui.settings.language").toString());
                this.languageButton.setFont(newFont);
                this.reminderButton.setText(Text.translatable("ui.settings.reminder").toString());
                this.reminderButton.setFont(newFont);
                this.configButton.setText(Text.translatable("ui.settings.config").toString());
                this.configButton.setFont(newFont);
                this.taskButtons.getFirst().setText(Text.translatable("ui.tasks.create").toString());
                for(ToggleButton button : this.taskButtons) {
                    button.setFont(newFont);
                }
                this.cancelButton.setText(Text.translatable("ui.tasks.cancel").toString());
                this.cancelButton.setFont(newSmallFont);
                this.saveButton.setText(Text.translatable("ui.tasks.save").toString());
                this.saveButton.setFont(newSmallFont);
                this.titleText.setFont(newLargeFont);
                this.descText.setFont(newMediumFont);
                this.titleEdit.setPromptText(Text.translatable("ui.tasks.enter.title").toString());
                this.titleEdit.setFont(newMediumFont);
                this.describeEdit.setPromptText(Text.translatable("ui.tasks.enter.desc").toString());
                this.describeEdit.setFont(newSmallFont);
                this.deleteButton.setText(Text.translatable("ui.tasks.delete").toString());
                this.deleteButton.setFont(newBigFont);
                onAction.handle(event);
            });
            if(language == Languages.getCurrLang()) langButton.fire();
            langBox.getChildren().add(langPane);
        }

        return langContentBox;
    }

    private void initTimePane(StackPane timePane) {
        VBox timeContentBox = new VBox(20);
        timeContentBox.setPadding(new Insets(20, 0, 20, 0));
        timeContentBox.setAlignment(Pos.TOP_CENTER);
        timePane.getChildren().add(timeContentBox);

        Font numberFont = Font.loadFont(
                getClass().getResourceAsStream(Language.ENGLISH_US.getFontPath()),
                Language.ENGLISH_US.getFontSizeFactor() * 10
        );
        Font numberLargeFont = Font.loadFont(
                getClass().getResourceAsStream(Language.ENGLISH_US.getFontPath()),
                Language.ENGLISH_US.getFontSizeFactor() * 30
        );

        javafx.scene.text.Text timeScreen = new javafx.scene.text.Text();
        timeScreen.setFont(numberLargeFont);
        timeContentBox.getChildren().add(timeScreen);
        this.timeScreen = timeScreen;

        HBox timeChooser = new HBox(30);
        timeChooser.setAlignment(Pos.CENTER);
        timeContentBox.getChildren().add(timeChooser);
        this.timeChooser = timeChooser;

        ScrollPane hourChooser = new ScrollPane();
        hourChooser.setMaxSize(70, 50);
        hourChooser.setStyle("-fx-background-color: transparent;");
        hourChooser.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        hourChooser.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        timeChooser.getChildren().add(hourChooser);

        VBox hourChooserBox = new VBox(1);
        hourChooserBox.setMaxSize(50, 50);
        hourChooserBox.setAlignment(Pos.CENTER);
        hourChooserBox.setPadding(new Insets(20, 0, 20, 0));
        hourChooser.setContent(hourChooserBox);

        for(int i = 0; i < 24; i++) {
            StackPane hourPane = ContentStatus.getOptionPane(50, 20, hourChooserBox, Text.literal(String.valueOf(i)), numberFont);
            ToggleButton hourButton = (ToggleButton) hourPane.getChildren().getLast();
            EventHandler<ActionEvent> onAction = hourButton.getOnAction();
            int hour = i;
            hourButton.setOnAction(event -> {
                if(this.currTask != null && this.currTask.getStartTime() != null) {
                    LocalTime startTime = this.currTask.getStartTime();
                    LocalTime newStartTime = LocalTime.of(hour, startTime.getMinute(), startTime.getSecond());
                    this.currTask.setStartTime(newStartTime);
                    timeScreen.setText(this.currTask.getStartTime().format(DateTimeFormatter.ofPattern("HH : mm : ss")));
                }
                onAction.handle(event);
            });
            hourChooserBox.getChildren().add(hourPane);
        }

        ScrollPane minuteChooser = new ScrollPane();
        minuteChooser.setMaxSize(70, 50);
        minuteChooser.setStyle("-fx-background-color: transparent;");
        minuteChooser.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        minuteChooser.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        timeChooser.getChildren().add(minuteChooser);

        VBox minuteChooserBox = new VBox(1);
        minuteChooserBox.setMaxSize(50, 50);
        minuteChooserBox.setAlignment(Pos.CENTER);
        minuteChooserBox.setPadding(new Insets(20, 0, 20, 0));
        minuteChooser.setContent(minuteChooserBox);

        for(int i = 0; i < 60; i++) {
            StackPane minutePane = ContentStatus.getOptionPane(50, 20, minuteChooserBox, Text.literal(String.valueOf(i)), numberFont);
            ToggleButton minuteButton = (ToggleButton) minutePane.getChildren().getLast();
            EventHandler<ActionEvent> onAction = minuteButton.getOnAction();
            int minute = i;
            minuteButton.setOnAction(event -> {
                if(this.currTask != null && this.currTask.getStartTime() != null) {
                    LocalTime startTime = this.currTask.getStartTime();
                    LocalTime newStartTime = LocalTime.of(startTime.getHour(), minute, startTime.getSecond());
                    this.currTask.setStartTime(newStartTime);
                    timeScreen.setText(this.currTask.getStartTime().format(DateTimeFormatter.ofPattern("HH : mm : ss")));
                }
                onAction.handle(event);
            });
            minuteChooserBox.getChildren().add(minutePane);
        }

        ScrollPane secondChooser = new ScrollPane();
        secondChooser.setMaxSize(70, 50);
        secondChooser.setStyle("-fx-background-color: transparent;");
        secondChooser.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        secondChooser.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        timeChooser.getChildren().add(secondChooser);

        VBox secondChooserBox = new VBox(1);
        secondChooserBox.setMaxSize(50, 50);
        secondChooserBox.setAlignment(Pos.CENTER);
        secondChooserBox.setPadding(new Insets(20, 0, 20, 0));
        secondChooser.setContent(secondChooserBox);

        for(int i = 0; i < 60; i++) {
            StackPane secondPane = ContentStatus.getOptionPane(50, 20, secondChooserBox, Text.literal(String.valueOf(i)), numberFont);
            ToggleButton secondButton = (ToggleButton) secondPane.getChildren().getLast();
            EventHandler<ActionEvent> onAction = secondButton.getOnAction();
            int second = i;
            secondButton.setOnAction(event -> {
                if(this.currTask != null && this.currTask.getStartTime() != null) {
                    LocalTime startTime = this.currTask.getStartTime();
                    LocalTime newStartTime = LocalTime.of(startTime.getHour(), startTime.getMinute(), second);
                    this.currTask.setStartTime(newStartTime);
                    timeScreen.setText(this.currTask.getStartTime().format(DateTimeFormatter.ofPattern("HH : mm : ss")));
                }
                onAction.handle(event);
            });
            secondChooserBox.getChildren().add(secondPane);
        }

        javafx.scene.text.Text durationText = new javafx.scene.text.Text();
        durationText.setText("");
        durationText.setFont(numberLargeFont);
        timeContentBox.getChildren().add(durationText);
        this.durationText = durationText;

        Slider durationHourSlider = new Slider(0, 23, 0);
        durationHourSlider.setShowTickMarks(true);
        durationHourSlider.setShowTickLabels(true);
        durationHourSlider.setSnapToTicks(true);
        durationHourSlider.setBlockIncrement(1);
        durationHourSlider.setMajorTickUnit(1);
        durationHourSlider.setMaxWidth(480);
        durationHourSlider.setOnMouseReleased(event -> {
            if(this.currTask != null && this.currTask.getDuration() != null) {
                int originHour = (int) this.currTask.getDuration().toHours();
                java.time.Duration newDuration = this.currTask.getDuration().plusHours((int) durationHourSlider.getValue() - originHour);
                this.currTask.setDuration(newDuration);
                this.durationText.setText(Text.literal(newDuration.toHours() + "hrs " + newDuration.toMinutesPart() + "min").toString());
            }
        });
        timeContentBox.getChildren().add(durationHourSlider);
        this.durationHourSlider = durationHourSlider;

        Slider durationMinuteSlider = new Slider(0, 59, 0);
        durationMinuteSlider.setShowTickMarks(true);
        durationMinuteSlider.setShowTickLabels(true);
        durationMinuteSlider.setSnapToTicks(true);
        durationMinuteSlider.setBlockIncrement(1);
        durationMinuteSlider.setMajorTickUnit(1);
        durationMinuteSlider.setMaxWidth(480);
        durationMinuteSlider.setOnMouseReleased(event -> {
            if(this.currTask != null && this.currTask.getDuration() != null) {
                int originMinutes = this.currTask.getDuration().toMinutesPart();
                java.time.Duration newDuration = this.currTask.getDuration().plusMinutes((int) durationMinuteSlider.getValue() - originMinutes);
                this.currTask.setDuration(newDuration);
                this.durationText.setText(Text.literal(newDuration.toHours() + "hrs " + newDuration.toMinutesPart() + "min").toString());
            }
        });
        timeContentBox.getChildren().add(durationMinuteSlider);
        this.durationMinuteSlider = durationMinuteSlider;
    }

    private void initDeletePane(StackPane deletePane) {
        StackPane deleteButtonPane = new StackPane();
        deleteButtonPane.setAlignment(Pos.CENTER);
        deletePane.getChildren().add(deleteButtonPane);

        Rectangle deleteBackground = new Rectangle(540, 40, Color.valueOf("c82727"));
        deleteBackground.setArcWidth(20);
        deleteBackground.setArcHeight(20);
        Rectangle deleteSelect = new Rectangle(540, 40, Color.valueOf("d54949"));
        deleteSelect.setOpacity(0);
        deleteSelect.setArcWidth(20);
        deleteSelect.setArcHeight(20);
        Button deleteButton = getMediumButton(deleteSelect, null, event -> {});
        EventHandler<ActionEvent> onAction = deleteButton.getOnAction();
        deleteButton.setOnAction(event -> {
            ObservableList<Node> children = ContentStatus.TASKS.box.getChildren();
            int size = children.size();
            for(int i = 0; i < size; i++) {
                ToggleButton last = (ToggleButton) ((StackPane) children.get(i)).getChildren().getLast();
                if(last.isSelected()) {
                    if(i != 0) {
                        children.remove(i);
                        this.taskButtons.remove(i);
                        List<Task> tasks = Schedule.getTasks();
                        tasks.remove(i - 1);
                        Schedule.updateTasks(tasks);
                        if(children.size() > 1) {
                            ((ToggleButton) ((StackPane) children.get(i)).getChildren().getLast()).fire();
                        } else {
                            ((ToggleButton) ((StackPane) children.get(0)).getChildren().getLast()).fire();
                        }
                        break;
                    }
                }
            }
            onAction.handle(event);
            deleteButton.setDisable(false);
            deleteButton.setOpacity(1);
        });
        Font font = Font.loadFont(
                getClass().getResourceAsStream(Languages.getCurrLang().getFontPath()),
                Languages.getCurrLang().getFontSizeFactor() * 20
        );
        deleteButton.setText(Text.translatable("ui.tasks.delete").toString());
        deleteButton.setFont(font);
        deleteButton.setTextFill(Color.valueOf("f6feff"));

        deleteButtonPane.getChildren().addAll(deleteBackground, deleteSelect, deleteButton);
        this.deleteButton = deleteButton;
    }

    private VBox initMainBox() {
        VBox mainBox = new VBox(30);
        mainBox.setPadding(new Insets(20, 0, 20, 0));
        mainBox.setAlignment(Pos.CENTER);

        StackPane describePane = new StackPane(getStyledRect(540, 480, Color.valueOf("acd7e2")));
        describePane.setTranslateX(20);
        describePane.setMaxSize(540, 480);
        describePane.setStyle("-fx-background-color: transparent;");
        describePane.setEffect(getShadow(
                10, 0.5, 0, Color.rgb(106, 104, 104)
        ));

        initDescPane(describePane);

        StackPane timePane = new StackPane(getStyledRect(540, 360, Color.valueOf("acd7e2")));
        timePane.setTranslateX(20);
        timePane.setMaxSize(540, 360);
        timePane.setStyle("-fx-background-color: transparent;");
        timePane.setEffect(getShadow(
                10, 0.5, 0, Color.rgb(106, 104, 104)
        ));

        initTimePane(timePane);

        StackPane deletePane = new StackPane(getStyledRect(540, 40, Color.valueOf("acd7e2")));
        deletePane.setTranslateX(20);
        deletePane.setMaxSize(540, 40);
        deletePane.setStyle("-fx-background-color: transparent;");
        deletePane.setEffect(getShadow(
                10, 0.5, 0, Color.rgb(106, 104, 104)
        ));

        initDeletePane(deletePane);

        mainBox.getChildren().addAll(describePane, timePane, deletePane);

        return mainBox;
    }

    private String cutDescString(String text) {
        String tempText = text;
        for(int i = 0; i < 4; i++) {
            if(!text.contains("\n")) break;
            tempText = tempText.replaceFirst("\n", "!");
            if(i == 3 && tempText.contains("\n")) {
                int index = tempText.indexOf("\n");
                return text.substring(0, index) + "\n...";
            }
        }
        return text;
    }

    private void initDescPane(StackPane descPane) {
        VBox describeBox = new VBox(20);
        describeBox.setPadding(new Insets(20, 0, 20, 0));
        describeBox.setAlignment(Pos.TOP_CENTER);
        descPane.getChildren().add(describeBox);

        String fontPath = Languages.getCurrLang().getFontPath();

        VBox title = new VBox(10);
        title.setMaxWidth(500);
        title.setAlignment(Pos.CENTER_LEFT);
        describeBox.getChildren().add(title);

        StackPane titleTextPane = new StackPane();
        titleTextPane.setAlignment(Pos.CENTER_LEFT);
        titleTextPane.setTranslateX(10);
        titleTextPane.setMaxSize(100, 40);
        javafx.scene.text.Text titleText = new javafx.scene.text.Text("TITLE");
        titleText.setFont(Font.loadFont(getClass().getResourceAsStream(fontPath), 25));
        titleTextPane.getChildren().add(titleText);
        this.titleText = titleText;
        title.getChildren().add(titleTextPane);

        StackPane editPane = new StackPane();
        editPane.setTranslateX(445);
        editPane.setMaxSize(40, 40);
        Circle select = new Circle(20);
        select.setOpacity(0);
        select.setFill(Color.valueOf("cde3e8"));
        Button editButton = getSmallButton(20, select, "/images/ui/edit.png", event -> {

        });
        editButton.setStyle("-fx-background-color: transparent;");
        editPane.getChildren().addAll(select, editButton);
        this.editButton = editButton;
        title.getChildren().add(editPane);

        StackPane descTextPane = new StackPane();
        descTextPane.setAlignment(Pos.TOP_LEFT);
        descTextPane.setTranslateX(10);
        descTextPane.setMaxWidth(520);
        descTextPane.setMaxHeight(100);
        String text = "This is the description of the task.......\n234234\nsdfsdf\nsdfasdf\nsdfsdf\nsdfsd\nsdfsdfsfsafsdfdsf";
        javafx.scene.text.Text descText = new javafx.scene.text.Text(cutDescString(text));
        descText.setFont(Font.loadFont(getClass().getResourceAsStream(fontPath), 15));
        descTextPane.getChildren().add(descText);
        this.descText = descText;
        title.getChildren().add(descTextPane);

        TextField titleEdit = new TextField();
        titleEdit.setDisable(true);
        titleEdit.setMaxWidth(500);
        titleEdit.setPromptText(Text.translatable("ui.tasks.enter.title").toString());
        titleEdit.setFont(Font.loadFont(getClass().getResourceAsStream(fontPath), 15));
        describeBox.getChildren().add(titleEdit);
        this.titleEdit = titleEdit;

        TextArea describeEdit = new TextArea();
        describeEdit.setDisable(true);
        describeEdit.setPromptText(Text.translatable("ui.tasks.enter.desc").toString());
        describeEdit.setFont(Font.loadFont(getClass().getResourceAsStream(fontPath), 12));
        describeEdit.setMaxWidth(500);
//        describeEdit.setMaxHeight(120);
//        describeEdit.setPrefRowCount(5);
        describeEdit.setWrapText(true);
        describeBox.getChildren().add(describeEdit);
        this.describeEdit = describeEdit;

//        StackPane editControlPane = new StackPane();
//        editControlPane.setMaxSize(150, 30);
//        editControlPane.setAlignment(Pos.CENTER_LEFT);
//        describeBox.getChildren().add(editControlPane);

        HBox editControlBox = new HBox(30);
        editControlBox.setTranslateX(165);
        editControlBox.setMaxSize(150, 30);
        editControlBox.setAlignment(Pos.CENTER);
        describeBox.getChildren().add(editControlBox);

        Font mediumFont = Font.loadFont(
                getClass().getResourceAsStream(fontPath),
                Languages.getCurrLang().getFontSizeFactor() * 12
        );

        StackPane savePane = new StackPane();
        savePane.setOpacity(0);
        savePane.setDisable(true);
        savePane.setMaxSize(60, 30);

        Rectangle saveBackGround = new Rectangle(60, 30, Color.valueOf("3d8add"));
        saveBackGround.setArcWidth(10);
        saveBackGround.setArcHeight(10);
        Rectangle saveSelect = new Rectangle(60, 30, Color.valueOf("63a0e2"));
        saveSelect.setOpacity(0);
        saveSelect.setArcWidth(10);
        saveSelect.setArcHeight(10);
        Button saveButton = getMediumButton(saveSelect, null, event -> {
            if(this.currTask != null) {
                String newTitleStr = titleEdit.getText();
                String newDescStr = describeEdit.getText();
                if(newTitleStr != null && !newTitleStr.isBlank()) this.currTask.setTitle(newTitleStr);
                if(newDescStr != null && !newDescStr.isBlank()) this.currTask.setDescription(newDescStr);
                titleText.setText(this.currTask.getTitle());
                descText.setText(cutDescString(this.currTask.getDescription()));
                int index = Schedule.getTasks().indexOf(this.currTask) + 1;
                this.taskButtons.get(index).setText(this.currTask.getTitle());
                Schedule.updateTasks(Schedule.getTasks());
            }

            titleEdit.setText("");
            describeEdit.setText("");
            editButton.setOpacity(1);
            editButton.setDisable(false);
            titleEdit.setDisable(true);
            describeEdit.setDisable(true);
            savePane.setOpacity(0);
            savePane.setDisable(true);
        });
        saveButton.setText(Text.translatable("ui.tasks.save").toString());
        saveButton.setFont(mediumFont);
        saveButton.setTextFill(Color.valueOf("f6feff"));

        savePane.getChildren().addAll(saveBackGround, saveSelect, saveButton);
        this.saveButton = saveButton;

        StackPane cancelPane = new StackPane();
        cancelPane.setOpacity(0);
        cancelPane.setDisable(true);
        cancelPane.setMaxSize(60, 30);

        Rectangle cancelBackGround1 = new Rectangle(60, 30, Color.valueOf("3d8add"));
        cancelBackGround1.setArcWidth(10);
        cancelBackGround1.setArcHeight(10);
        Rectangle cancelBackGround2 = new Rectangle(58, 28, Color.valueOf("acd7e2"));
        cancelBackGround2.setArcWidth(10);
        cancelBackGround2.setArcHeight(10);
        Rectangle cancelSelect = new Rectangle(60, 30, Color.valueOf("63a0e2"));
        cancelSelect.setOpacity(0);
        cancelSelect.setArcWidth(10);
        cancelSelect.setArcHeight(10);
        Button cancelButton = getMediumButton(cancelSelect, null, event -> {
            titleEdit.setText("");
            describeEdit.setText("");
            editButton.setOpacity(1);
            editButton.setDisable(false);
            titleEdit.setDisable(true);
            describeEdit.setDisable(true);
            cancelPane.setOpacity(0);
            cancelPane.setDisable(true);
            savePane.setOpacity(0);
            savePane.setDisable(true);
        });
        cancelButton.setText(Text.translatable("ui.tasks.cancel").toString());
        cancelButton.setFont(mediumFont);
        cancelButton.setTextFill(Color.valueOf("1b649c"));

        cancelPane.getChildren().addAll(cancelBackGround1, cancelBackGround2, cancelSelect, cancelButton);
        this.cancelButton = cancelButton;

        editControlBox.getChildren().addAll(cancelPane, savePane);

        EventHandler<ActionEvent> saveOnAction = saveButton.getOnAction();
        saveButton.setOnAction(event -> {
            saveOnAction.handle(event);
            cancelPane.setOpacity(0);
            cancelPane.setDisable(true);
        });

        EventHandler<ActionEvent> editOnAction = editButton.getOnAction();
        editButton.setOnAction(event -> {
            editOnAction.handle(event);
            titleEdit.setDisable(false);
            describeEdit.setDisable(false);
            savePane.setDisable(false);
            savePane.setOpacity(1);
            saveButton.setDisable(false);
            saveButton.setOpacity(1);
            cancelPane.setDisable(false);
            cancelPane.setOpacity(1);
            cancelButton.setDisable(false);
            cancelButton.setOpacity(1);
        });
    }

    private StackPane initContentArea(Stage stage) {
        StackPane content = new StackPane();
        content.setStyle("-fx-background-color: #C8EEF9; -fx-background-radius: 0 0 10 10;");
        content.setMaxSize(800, 400);
        content.setAlignment(Pos.CENTER_LEFT);

//        VBox contentBox = new VBox();
//        contentBox.setAlignment(Pos.CENTER_LEFT);
//        contentBox.setMaxSize(300, 400);
//        content.getChildren().add(contentBox);

        //the sidePane area
        StackPane sidePane = new StackPane();
        sidePane.setStyle("-fx-background-color: #C8EEF9; -fx-background-radius: 0 0 10 10;");
        sidePane.setMaxSize(200, 400);
        sidePane.setEffect(getShadow(
                10, 0.5, 0, Color.rgb(106, 104, 104)
        ));

        StackPane childBoard = new StackPane();
        childBoard.setMinHeight(390);
        childBoard.setStyle("-fx-background-color: #acd7e2; -fx-background-radius: 0 0 0 0;");
        childBoard.setMaxWidth(this.currSideWidth);
        StackPane.setAlignment(childBoard, Pos.CENTER_LEFT);

        childBoard.getChildren().add(ContentStatus.TASKS.box);

        ScrollPane scrollChildBoard = new ScrollPane(childBoard);
        scrollChildBoard.setFitToWidth(true);
        scrollChildBoard.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollChildBoard.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sidePane.getChildren().addAll(scrollChildBoard);

        //the main pane area
        StackPane mainPane = new StackPane();
        mainPane.setStyle("-fx-background-color: #e3f4f8; -fx-background-radius: 0 0 10 10;");
        mainPane.setMaxSize(640, 400);
        mainPane.setTranslateX(160);
        content.getChildren().addAll(mainPane, sidePane);

        ScrollPane scrollMainBoard = new ScrollPane();
        scrollMainBoard.setFitToWidth(true);
        scrollMainBoard.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollMainBoard.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        mainPane.getChildren().addAll(scrollMainBoard);

        StackPane mainBoard = new StackPane();
        scrollMainBoard.setContent(mainBoard);
        this.mainColumn = mainBoard;

        VBox mainBox = initMainBox();
        mainBoard.getChildren().add(mainBox);
        this.mainBox = mainBox;
        this.tasksBox = mainBox;

        initButtons(stage);

        this.settingsBoxes.addAll(initSettingsBoxes());

        int index = 0;
        if(status == ContentStatus.TASKS) {
            if(this.taskButtons.size() > 1) {
                index = 1;
            }
        }
        ((ToggleButton) ((StackPane) status.box.getChildren().get(index)).getChildren().getLast()).fire();

//        sidePane.getChildren().addAll(scrollChildBoard);
        this.sideColumn = sidePane;

//        content.getChildren().addAll(mainPane, sidePane);

        return content;
    }

    private DropShadow getShadow(double radius, double xOffset, double yOffset, Color color) {
        DropShadow shadow = new DropShadow();
        shadow.setRadius(radius);
        shadow.setOffsetX(xOffset);
        shadow.setOffsetY(yOffset);
        shadow.setColor(color);

        return shadow;
    }

    @Getter
    public enum ContentStatus {
        TASKS(0, 200, getBox(0)),
        SETTINGS(1, 160, getBox(1));

        private final int value;
        private final double sideColumnWidth;
        private final VBox box;

        ContentStatus(int value, double sideColumnWidth, VBox box) {
            this.value = value;
            this.sideColumnWidth = sideColumnWidth;
            this.box = box;
        }

        private static StackPane getOptionPane(
                double width,
                double height,
                VBox parent,
                Text text,
                Font font
        ) {
            Language currLang = Languages.getCurrLang();
            String fontPath = currLang.getFontPath();
            double fontSizeFactor = currLang.getFontSizeFactor();

            StackPane stackPane = new StackPane();
            stackPane.setPrefSize(width, height);
            stackPane.setStyle("-fx-background-color: transparent;");
            stackPane.setAlignment(Pos.CENTER);

            ToggleButton toggleButton = new ToggleButton(text.toString());
            toggleButton.setPrefSize(width, height);
            toggleButton.setFont(font);
//            toggleButton.setFont(Font.loadFont(ContentStatus.class.getResourceAsStream(fontPath), fontSizeFactor * 18));
            toggleButton.setStyle("-fx-background-color: transparent;");
            toggleButton.setTextFill(Color.rgb(36, 112, 174, 1));
            toggleButton.setTextAlignment(TextAlignment.LEFT);
            toggleButton.setAlignment(Pos.CENTER);

            Rectangle enterRect = new Rectangle(width, height);
            enterRect.setArcWidth(20);
            enterRect.setArcHeight(20);
            enterRect.setFill(Color.valueOf("cde3e8"));
            enterRect.setOpacity(0);
            FadeTransition rectFadeIn = new FadeTransition(Duration.millis(200), enterRect);
            rectFadeIn.setFromValue(0);
            rectFadeIn.setToValue(1);
            FadeTransition rectFadeOut = new FadeTransition(Duration.millis(200), enterRect);
            rectFadeOut.setFromValue(1);
            rectFadeOut.setToValue(0);

            toggleButton.setOnMouseEntered(event -> {
                if(!toggleButton.isSelected()) {
                    rectFadeIn.play();
                }
            });
            toggleButton.setOnMouseExited(event -> {
                if(!toggleButton.isSelected()) {
                    rectFadeOut.play();
                }
            });

            toggleButton.setOnAction(event -> {
                ObservableList<Node> children = parent.getChildren();
                int size = children.size();
                for(int i = 0; i < size; i++) {
                    StackPane pane = (StackPane) children.get(i);
                    ToggleButton button = (ToggleButton) pane.getChildren().getLast();
                    if(button.isSelected()) {
                        Rectangle rectangle = (Rectangle) pane.getChildren().getFirst();
                        rectangle.setOpacity(0);
                        rectangle.setFill(Color.valueOf("cde3e8"));
                        button.setTextFill(Color.rgb(36, 112, 174, 1));
                        button.setSelected(false);
                    }
                }
                enterRect.setFill(Color.valueOf("3D8ADD"));
                enterRect.setOpacity(1);
                toggleButton.setTextFill(Color.valueOf("F3FDFCFF"));
                toggleButton.setSelected(true);
            });

            toggleButton.setOnMouseClicked(event -> {
                if(toggleButton.isSelected()) event.consume();
            });

            stackPane.getChildren().addAll(enterRect, toggleButton);

            return stackPane;
        }

        private static VBox getBox(int val) {
            String fontPath = Languages.getCurrLang().getFontPath();
            double fontSizeFactor = Languages.getCurrLang().getFontSizeFactor();
            Font font = Font.loadFont(ContentStatus.class.getResourceAsStream(fontPath), fontSizeFactor * 18);
            if(val == 0) {
                //Tasks Box
                VBox tasksBox = new VBox(20);
                tasksBox.setTranslateY(0);
                tasksBox.setTranslateX(0);
                tasksBox.setPadding(new Insets(90, 0, 90, 0));
                tasksBox.setAlignment(Pos.CENTER);

                StackPane createTaskPane = getOptionPane(160, 40, tasksBox, Text.translatable("ui.tasks.create"), font);

                tasksBox.getChildren().add(createTaskPane);

                List<Task> tasks = Schedule.getTasks();
                for (Task task : tasks) {
                    StackPane taskPane = getOptionPane(160, 40, tasksBox, Text.literal(task.getTitle()), font);

                    tasksBox.getChildren().add(taskPane);
                }

                return tasksBox;
            }

            if(val == 1) {
                //Settings Box
                VBox settingsBox = new VBox(40);
                settingsBox.setTranslateY(0);
                settingsBox.setTranslateX(20);
                settingsBox.setMinHeight(390);
                settingsBox.setPadding(new Insets(90, 0, 90, 0));
                settingsBox.setAlignment(Pos.CENTER);

                for(int i = 0; i < 3; i++) {
                    String translateKey = "";
                    switch (i) {
                        case 0 -> {
                            translateKey = "ui.settings.language";
                            break;
                        }
                        case 1 -> {
                            translateKey = "ui.settings.reminder";
                            break;
                        }
                        case 2 -> {
                            translateKey = "ui.settings.config";
                            break;
                        }
                        default -> {

                        }
                    }

                    StackPane optionPane = getOptionPane(120, 40, settingsBox, Text.translatable(translateKey), font);

                    settingsBox.getChildren().add(optionPane);
                }

//                ((ToggleButton) ((StackPane) settingsBox.getChildren().getFirst()).getChildren().getLast()).fire();

                return settingsBox;
            }
            return null;
        }
    }
}
