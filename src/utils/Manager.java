package utils;

import controller.FileBackedTasksManager;
import controller.InMemoryHistoryManager;
import controller.InMemoryTaskManager;
import controller.TZ8.HttpTaskManager;

import java.io.IOException;

public class Manager {

    public static InMemoryHistoryManager historyManager;
    public static InMemoryTaskManager taskManager;
    public static FileBackedTasksManager fileManager;
    public static HttpTaskManager httpTaskManager;

    public static InMemoryHistoryManager getDefaultHistory() {
        if (historyManager == null) {
            historyManager = new InMemoryHistoryManager();
        }
        return historyManager;
    }

    public static InMemoryTaskManager isDefault() {
        if (taskManager == null) {
            taskManager = new InMemoryTaskManager();
        }
        return taskManager;
    }

    public static FileBackedTasksManager isDefaultFile() {
        if (fileManager == null) {
            fileManager = new FileBackedTasksManager();
        }
        return fileManager;
    }

    public static HttpTaskManager isDefaultHttp() {
        if (httpTaskManager == null) {
            try {
                httpTaskManager = new HttpTaskManager();
            } catch (IOException | InterruptedException e) {
                System.out.println("Ошибка создания HttpTaskManager: ");
                e.printStackTrace();
            }
        }
        return httpTaskManager;
    }
}
