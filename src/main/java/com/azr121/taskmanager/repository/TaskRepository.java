package com.azr121.taskmanager.repository;

import com.azr121.taskmanager.model.Priority;
import com.azr121.taskmanager.model.Task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private static final String FILE_NAME = "tasks.txt";

    public void saveTask(Task task) {
        Path path = Path.of(FILE_NAME);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(formatTask(task));
            writer.newLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Task> loadAllTasks() {
        List<Task> tasks = new ArrayList<>();
        Path path = Path.of(FILE_NAME);

        if (!Files.exists(path)) {
            return tasks;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (FileNotFoundException e) {
            return tasks;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }

        return tasks;
    }

    public void deleteTask(String id) {
        List<Task> tasks = loadAllTasks();
        List<Task> remaining = new ArrayList<>();

        for (Task task : tasks) {
            if (!task.getId().equals(id)) {
                remaining.add(task);
            }
        }

        rewriteFile(remaining);
    }

    private String formatTask(Task task) {
        return String.join(",",
                task.getId(),
                task.getTitle(),
                task.getPriority().name(),
                Boolean.toString(task.isCompleted()),
                task.getCreatedDate().toString());
    }

    private Task parseTask(String line) {
        String[] parts = line.split(",", 5);
        if (parts.length != 5) {
            return null;
        }

        try {
            String id = parts[0];
            String title = parts[1];
            Priority priority = Priority.valueOf(parts[2]);
            boolean completed = Boolean.parseBoolean(parts[3]);
            LocalDate createdDate = LocalDate.parse(parts[4]);
            return Task.fromData(id, title, priority, completed, createdDate);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private void rewriteFile(List<Task> tasks) {
        Path path = Path.of(FILE_NAME);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Task task : tasks) {
                writer.write(formatTask(task));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
