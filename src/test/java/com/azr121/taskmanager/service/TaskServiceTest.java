package com.azr121.taskmanager.service;

import com.azr121.taskmanager.model.Priority;
import com.azr121.taskmanager.model.Task;
import com.azr121.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {
    private static final Path TASKS_FILE = Path.of("tasks.txt");
    private TaskRepository repository;
    private TaskService service;

    @BeforeEach
    void setUp() throws IOException {
        Files.deleteIfExists(TASKS_FILE);
        repository = new TaskRepository();
        service = new TaskService(repository);
    }

    @Test
    void addTaskReturnsTaskWithCorrectTitleAndPriority() {
        Task task = service.addTask("Test task", Priority.HIGH);

        assertNotNull(task.getId());
        assertEquals("Test task", task.getTitle());
        assertEquals(Priority.HIGH, task.getPriority());
        assertFalse(task.isCompleted());
    }

    @Test
    void addTaskThrowsWhenTitleIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> service.addTask("   ", Priority.LOW));
    }

    @Test
    void addTaskThrowsWhenTitleIsDuplicateIgnoringCase() {
        service.addTask("Duplicate", Priority.MEDIUM);

        assertThrows(IllegalArgumentException.class, () -> service.addTask("duplicate", Priority.HIGH));
    }

    @Test
    void completeTaskMarksTaskAsCompleted() {
        Task task = service.addTask("Complete me", Priority.LOW);

        Task completed = service.completeTask(task.getId());

        assertTrue(completed.isCompleted());
        List<Task> loaded = service.getAllTasks();
        assertTrue(loaded.stream().anyMatch(t -> t.getId().equals(task.getId()) && t.isCompleted()));
    }

    @Test
    void completeTaskThrowsWhenIdUnknown() {
        assertThrows(IllegalArgumentException.class, () -> service.completeTask("unknown-id"));
    }

    @Test
    void completeTaskThrowsWhenAlreadyCompleted() {
        Task task = service.addTask("Already done", Priority.MEDIUM);
        service.completeTask(task.getId());

        assertThrows(IllegalStateException.class, () -> service.completeTask(task.getId()));
    }

    @Test
    void deleteTaskRemovesTaskSuccessfully() {
        Task task = service.addTask("Delete me", Priority.HIGH);

        service.deleteTask(task.getId());

        List<Task> loaded = service.getAllTasks();
        assertTrue(loaded.isEmpty());
    }

    @Test
    void deleteTaskThrowsWhenIdUnknown() {
        assertThrows(IllegalArgumentException.class, () -> service.deleteTask("no-id"));
    }
}
