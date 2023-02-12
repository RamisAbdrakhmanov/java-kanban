package model.task;

import model.Status;
import model.TaskEnum;


import java.time.LocalDateTime;

import java.util.*;

public class Epic extends Task {
    transient private final List<Subtask> subtasks;
    transient LocalDateTime endTime;


    public Epic(String name, String info, String startTime, String during) {
        super(name, info, startTime, during);
        subtasks = new ArrayList<>();
        super.setTaskEnum(TaskEnum.EPIC);
    }

    public Epic(int id, String name, String info, Status status, String startTime, String duration) {
        super(id, name, info, status, startTime, duration);
        subtasks = new ArrayList<>();
        super.setTaskEnum(TaskEnum.EPIC);
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void addSubtaskInList(Subtask subtask) {
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
