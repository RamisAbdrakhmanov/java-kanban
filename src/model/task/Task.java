package model.task;

import model.Status;
import model.TaskEnum;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private int id;
    private final String name;
    private final String info;
    private Status status;
    private TaskEnum taskEnum;
    private long duration;
    private LocalDateTime startTime;
    private transient final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task(String name, String info, String startTime, String duration) {
        this.name = name;
        this.info = info;
        this.duration = Long.parseLong(duration);
        this.startTime = LocalDateTime.parse(startTime, formatter);
        this.status = Status.NEW;
        this.taskEnum = TaskEnum.TASK;

    }

    public Task(int id, String name, String info, Status status, String startTime, String duration) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.status = status;
        this.duration = Long.parseLong(duration);
        this.startTime = LocalDateTime.parse(startTime, formatter);

        this.taskEnum = TaskEnum.TASK;
    }

    public int getId() {
        return id;
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

    public long getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskEnum getTaskEnum() {
        return taskEnum;
    }

    public void setTaskEnum(TaskEnum taskEnum) {
        this.taskEnum = taskEnum;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }


    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", status=" + status +
                ", taskEnum=" + taskEnum +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return id == task.id && Objects.equals(info, task.info) && Objects.equals(name, task.name) &&
                status == task.status && Objects.equals(startTime, task.startTime) &&
                Objects.equals(duration, task.duration);
    }
}

