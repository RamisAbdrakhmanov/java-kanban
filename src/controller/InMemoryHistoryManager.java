package controller;

import model.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager  implements HistoryManager{
    private List<Task> history = new ArrayList<>(); //история поиска задач

    @Override //показать историю поиска задач
    public List<Task> getHistory() {
        return history;
    }

    @Override //записать в список истории поиска задач
    public void addTaskInHistory(Task task) {
        if(history.size()<=10) {
            history.add(history.size(),task);
        }else {
            List<Task> tasks = new ArrayList<>();
            history.stream().skip(1).forEach(tasks::add);
            history =tasks;
            history.add(history.size(),task);
        }
    }
}
