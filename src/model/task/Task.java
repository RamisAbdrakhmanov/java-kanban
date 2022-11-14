package model.task;

import model.Status;


public class Task {
    private String name;
    private String info;
    private Status status;
    private int id;


    public Task(String name, String info) {
        this.name = name;
        this.info = info;
        this.status = Status.NEW;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setInfo(String info) {
        this.info = info;
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

