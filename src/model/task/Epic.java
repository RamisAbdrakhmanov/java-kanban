package model.task;

import model.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Subtask> subtasks;

    public Epic(String name, String info) {
        super(name, info);
        subtasks = new ArrayList<>();
    }

    public Epic(String name, String info, Status status, int id) {
        super(name, info, status, id);
        subtasks = new ArrayList<>();
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtaskInList(Subtask subtask){
        subtasks.add(subtask);
    }

    @Override
    public String toString() {
        return super.toString() +
                "Epic{" +
                "subtasks=" + subtasks +
                '}';
    }
}
