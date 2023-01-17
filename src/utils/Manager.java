package utils;

import controller.FileBackedTasksManager;
import controller.InMemoryHistoryManager;
import controller.InMemoryTaskManager;

public class Manager {

    public static InMemoryHistoryManager historyManager;
    public static InMemoryTaskManager taskManager;
    public static FileBackedTasksManager fileManager;

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
}
