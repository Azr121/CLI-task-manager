package com.azr121.taskmanager.repository;

import com.azr121.taskmanager.model.Priority;
import com.azr121.taskmanager.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskRepositoryTest {
    private static final Path TASKS_FILE = Path.of("tasks.txt");
    private TaskRepository repository;

    @BeforeEach
    void setUp() throws IOException {
        Files.deleteIfExists(TASKS_FILE);
        repository = new TaskRepository();
    }

    @Test
    void saveTaskWritesTaskAndLoadAllTasksReturnsIt() {
        Task task = new Task("Write tests", Priority.MEDIUM);
        repository.saveTask(task);

        List<Task> loaded = repository.loadAllTasks();

        assertEquals(1, loaded.size());
        Task loadedTask = loaded.get(0);
        assertEquals(task.getId(), loadedTask.getId());
        assertEquals(task.getTitle(), loadedTask.getTitle());
        assertEquals(task.getPriority(), loadedTask.getPriority());
        assertEquals(task.isCompleted(), loadedTask.isCompleted());
        assertEquals(task.getCreatedDate(), loadedTask.getCreatedDate());
    }

    @Test
    void loadAllTasksReturnsEmptyListWhenFileDoesNotExist() {
        List<Task> loaded = repository.loadAllTasks();

        assertTrue(loaded.isEmpty());
    }

    @Test
    void deleteTaskRemovesCorrectTaskById() {
        Task first = new Task("First task", Priority.LOW);
        Task second = new Task("Second task", Priority.HIGH);
        repository.saveTask(first);
        repository.saveTask(second);

        repository.deleteTask(first.getId());

        List<Task> loaded = repository.loadAllTasks();
        assertEquals(1, loaded.size());
        assertEquals(second.getId(), loaded.get(0).getId());
    }

    @Test
    void deleteTaskKeepsRemainingTasks() {
        Task first = new Task("First task", Priority.LOW);
        Task second = new Task("Second task", Priority.HIGH);
        repository.saveTask(first);
        repository.saveTask(second);

        repository.deleteTask(first.getId());

        List<Task> loaded = repository.loadAllTasks();
        assertEquals(1, loaded.size());
        assertEquals("Second task", loaded.get(0).getTitle());
        assertEquals(Priority.HIGH, loaded.get(0).getPriority());
    }
}
