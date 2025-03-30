package com.climinby.aqiiv.module.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Getter @Setter
public class Task {
    private String title = "";
    private String description = "";

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime startTime;

    private Duration duration = Duration.ZERO;
    private List<DayOfWeek> availableDays = new ArrayList<>();
    @JsonIgnore
    private Predicate<LocalDate> dateChooser = date -> true;

    public Task() {
        this.startTime = LocalTime.now();
    }

    public Task(String title, String description, List<DayOfWeek> availableDays) {
        if(title != null) this.title = title;
        if(description != null) this.description = description;
        else this.startTime = LocalTime.now();
        if(availableDays != null) this.availableDays.addAll(availableDays);
    }

    public Task(String title, String description, LocalTime startTime, Duration duration, List<DayOfWeek> availableDays) {
        if(title != null) this.title = title;
        if(description != null) this.description = description;
        if(startTime != null) this.startTime = startTime;
        else this.startTime = LocalTime.now();
        if(duration != null) this.duration = duration;
        if(availableDays != null) this.availableDays.addAll(availableDays);
    }

    public Task(String title, String description, LocalTime startTime, Duration duration, Predicate<LocalDate> dateChooser, List<DayOfWeek> availableDays) {
        if(title != null) this.title = title;
        if(description != null) this.description = description;
        if(startTime != null) this.startTime = startTime;
        else this.startTime = LocalTime.now();
        if(duration != null) this.duration = duration;
        if(dateChooser != null) this.dateChooser = dateChooser;
        if(availableDays != null) this.availableDays.addAll(availableDays);
    }

    public boolean empty() {
        if(this.title.isBlank()) return true;
        return false;
    }

    @JsonGetter("duration")
    public String getDurationString() {
        return duration.toString();
    }

    @JsonSetter("duration")
    public void setDurationFromString(String durationStr) {
        this.duration = Duration.parse(durationStr);
    }
}
