package model.task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    public    List<Subtask> subtasks;

    public Epic(String name, String info) {
        super(name, info);
        subtasks = new ArrayList<>();
    }



    @Override
    public String toString() {
        return super.toString() +
                "Epic{" +
                "subtasks=" + subtasks +
                '}';
    }
}
