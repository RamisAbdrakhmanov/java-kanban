package model.task;

import model.Status;
import model.TaskEnum;


public class Task {
    private final String name;
    private final String info;
    private Status status;
    private int id;
    private final TaskEnum taskEnum;


    public Task(String name, String info) {
        this.name = name;
        this.info = info;
        this.status = Status.NEW;
        this.taskEnum = TaskEnum.TASK;

    }

    public Task(String name, String info, Status status, int id) {
        this.name = name;
        this.info = info;
        this.status = status;
        this.id = id;
        if (this instanceof Subtask) {
            this.taskEnum = TaskEnum.SUBTASK;
        } else if (this instanceof Epic) {           // не совсем уверен правильно ли это так делать
            this.taskEnum = TaskEnum.EPIC;
        } else {
            this.taskEnum = TaskEnum.TASK;
        }
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskEnum getTaskEnum() {
        return taskEnum;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}';
    }
}

