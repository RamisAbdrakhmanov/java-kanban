package controller;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;

import java.util.List;

public interface TaskManager {
    void getAllTask();

    void deleteAllTask();

    void getTaskById(int o);

    void createTask(Task task);

    void changeTask(Task task);

    void deleteTaskById(int id);

    public List<Subtask> subtasks(Epic epic);

}
