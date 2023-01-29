package controller;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTask();

    void deleteAllTask();

    Task getTaskById(int o);

    void addNewTask(Task task);

    void changeTask(Task task);

    void deleteTaskById(int id);

    List<Task> getPrioritizedTasks();

    List<Subtask> subtasks(Epic epic);

}
