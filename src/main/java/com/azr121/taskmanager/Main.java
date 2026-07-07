package com.azr121.taskmanager;

import com.azr121.taskmanager.repository.TaskRepository;
import com.azr121.taskmanager.service.TaskService;
import com.azr121.taskmanager.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        TaskRepository repository = new TaskRepository();
        TaskService service = new TaskService(repository);
        ConsoleUI ui = new ConsoleUI(service);
        ui.start();
    }
}
