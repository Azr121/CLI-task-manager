package com.azr121.taskmanager.service;

import com.azr121.taskmanager.model.Priority;
import com.azr121.taskmanager.model.Task;
import com.azr121.taskmanager.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Task addTask(String title, Priority priority) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be null or blank");
        }

        List<Task> existingTasks = repository.loadAllTasks();
        for (Task task : existingTasks) {
            if (task.getTitle().equalsIgnoreCase(title)) {
                throw new IllegalArgumentException("A task with the same title already exists");
            }
        }

        Task task = new Task(title, priority);
        repository.saveTask(task);
        return task;
    }

    public List<Task> getAllTasks() {
        return repository.loadAllTasks();
    }

    public Task completeTask(String id) {
        Task task = findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found for id: " + id));
        if (task.isCompleted()) {
            throw new IllegalStateException("Task is already completed");
        }

        task.setCompleted(true);
        repository.deleteTask(id);
        repository.saveTask(task);
        return task;
    }

    public void deleteTask(String id) {
        Task task = findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found for id: " + id));
        repository.deleteTask(task.getId());
    }

    private Optional<Task> findById(String id) {
        return repository.loadAllTasks().stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();
    }
}
