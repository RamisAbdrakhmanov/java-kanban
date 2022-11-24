package controller;

import model.task.Task;

import java.util.List;

public interface HistoryManager {
    List<Task> getHistory();

    void addTaskInHistory(Task task);
}
