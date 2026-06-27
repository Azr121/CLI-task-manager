package com.azr121.taskmanager.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    void newTaskIsNotCompletedOnCreation() {
        Task task = new Task("Test task", Priority.MEDIUM);

        assertFalse(task.isCompleted());
    }

    @Test
    void newTaskHasNonNullId() {
        Task task = new Task("Test task", Priority.MEDIUM);

        assertNotNull(task.getId());
        assertFalse(task.getId().isBlank());
    }

    @Test
    void newTaskHasCreatedDateSetToToday() {
        Task task = new Task("Test task", Priority.HIGH);

        assertEquals(LocalDate.now(), task.getCreatedDate());
    }

    @Test
    void newTaskStoresTitleAndPriorityCorrectly() {
        Task task = new Task("Write unit tests", Priority.LOW);

        assertEquals("Write unit tests", task.getTitle());
        assertEquals(Priority.LOW, task.getPriority());
    }
}
