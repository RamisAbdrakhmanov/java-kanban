package model.task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasks;

    public Epic(String name, String info) {
        super(name, info);
        subtasks = new ArrayList<>();
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        return super.toString() +
                "Epic{" +
                "subtasks=" + subtasks +
                '}';
    }
}