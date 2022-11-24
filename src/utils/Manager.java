package utils;

import controller.HistoryManager;
import controller.InMemoryHistoryManager;
import controller.InMemoryTaskManager;
import controller.TaskManager;
import model.Status;

public class Manager {

    public static InMemoryHistoryManager historyManager;
    public static InMemoryTaskManager taskManager;

    public static HistoryManager getDefaultHistory(){
        if(historyManager == null){
            historyManager = new InMemoryHistoryManager();
            return historyManager;
        }else {
            return historyManager;
        }
    }

    public static InMemoryTaskManager isDefault(){
        if(taskManager == null){
            taskManager = new InMemoryTaskManager();
            return taskManager;
        }else {
            return taskManager;
        }
    }
}
