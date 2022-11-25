package controller;

import model.task.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new LinkedList<>(); //история поиска задач

    @Override //показать историю поиска задач
    public List<Task> getHistory() {
        return history;
    }

    @Override //записать в список истории поиска задач
    public void addTaskInHistory(Task task) {
        if (history.size() >= 10) {
            history.remove(0);
            history.add(task);
        } else {
            history.add(history.size(), task);
        }
    }
}
