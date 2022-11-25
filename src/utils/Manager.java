package utils;

import controller.HistoryManager;
import controller.InMemoryHistoryManager;
import controller.InMemoryTaskManager;

public class Manager {

    public static InMemoryHistoryManager historyManager;
    public static InMemoryTaskManager taskManager;

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
}
