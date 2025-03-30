package com.climinby.aqiiv.util;

import com.climinby.aqiiv.data.Paths;
import com.climinby.aqiiv.module.task.Task;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private static final List<Task> TASKS = new ArrayList<>();

    public static List<Task> getTasks() {
        List<Task> clone = new ArrayList<>(TASKS);
        return clone;
    }

    public static void updateTasks(List<Task> tasks) {
        TASKS.clear();
        TASKS.addAll(tasks);
        try {
            JsonOperator.writeObject(Paths.DATA_JSON, TASKS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        List<Task> tasks = null;
        try {
            tasks = JsonOperator.readObject(Paths.DATA_JSON, new TypeReference<List<Task>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(tasks != null) {
            TASKS.clear();
            TASKS.addAll(tasks);
        }
        List<Task> nonNull = TASKS.stream().filter(task -> !task.empty()).toList();
        TASKS.clear();
        TASKS.addAll(nonNull);
    }
}
