package com.azr121.taskmanager.model;

import java.time.LocalDate;
import java.util.UUID;

public class Task {
    private final String id;
    private final String title;
    private final Priority priority;
    private boolean completed;
    private final LocalDate createdDate;

    public Task(String title, Priority priority) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.priority = priority;
        this.completed = false;
        this.createdDate = LocalDate.now();
    }

    private Task(String id, String title, Priority priority, boolean completed, LocalDate createdDate) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.completed = completed;
        this.createdDate = createdDate;
    }

    public static Task fromData(String id, String title, Priority priority, boolean completed, LocalDate createdDate) {
        return new Task(id, title, priority, completed, createdDate);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Priority getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return String.format(
                "Task{id='%s', title='%s', priority=%s, completed=%s, createdDate=%s}",
                id, title, priority, completed, createdDate);
    }
}
