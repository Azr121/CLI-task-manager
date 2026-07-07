package com.azr121.taskmanager.ui;

import com.azr121.taskmanager.model.Priority;
import com.azr121.taskmanager.model.Task;
import com.azr121.taskmanager.service.TaskService;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final TaskService service;
    private final Scanner scanner;

    public ConsoleUI(TaskService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1" -> addTask();
                case "2" -> viewAllTasks();
                case "3" -> completeTask();
                case "4" -> deleteTask();
                case "5" -> running = false;
                default -> System.out.println("Invalid option, please try again.");
            }
        }

        scanner.close();
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Add task");
        System.out.println("2. View all tasks");
        System.out.println("3. Complete task");
        System.out.println("4. Delete task");
        System.out.println("5. Quit");
        System.out.print("Choose an option: ");
    }

    private void addTask() {
        System.out.print("Enter title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter priority (LOW/MEDIUM/HIGH): ");
        String p = scanner.nextLine().trim().toUpperCase();
        Priority priority;
        try {
            priority = Priority.valueOf(p);
        } catch (IllegalArgumentException e) {
            priority = Priority.MEDIUM;
            System.out.println("Invalid priority, defaulting to MEDIUM.");
        }

        try {
            Task task = service.addTask(title, priority);
            System.out.println("Task added: " + task.getTitle() + " (id=" + task.getId() + ")");
        } catch (IllegalArgumentException ex) {
            System.out.println("Error adding task: " + ex.getMessage());
        }
    }

    private void viewAllTasks() {
        List<Task> tasks = service.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        for (Task t : tasks) {
            String status = t.isCompleted() ? "Completed" : "Pending";
            System.out.printf("%s (id=%s) - %s - %s - %s%n",
                    t.getTitle(), t.getId(), t.getPriority(), status, t.getCreatedDate());
        }
    }

    private void completeTask() {
        System.out.print("Enter task id to complete: ");
        String id = scanner.nextLine().trim();
        try {
            Task t = service.completeTask(id);
            System.out.println("Task marked completed: " + t.getTitle());
        } catch (IllegalArgumentException | IllegalStateException ex) {
            System.out.println("Could not complete task: " + ex.getMessage());
        }
    }

    private void deleteTask() {
        System.out.print("Enter task id to delete: ");
        String id = scanner.nextLine().trim();
        try {
            service.deleteTask(id);
            System.out.println("Task deleted: " + id);
        } catch (IllegalArgumentException ex) {
            System.out.println("Could not delete task: " + ex.getMessage());
        }
    }
}

