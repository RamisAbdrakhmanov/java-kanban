package controller;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;

import java.util.List;
import java.util.Set;


public interface TaskManager {
    List<Task> getTasks();

    void deleteAllTasks();

    Task getTaskById(int o);

    void addTask(Task task);

    void updateTask(Task task);

    void deleteTaskById(int id);

    Set<Task> getPrioritizedTasks();

    List<Subtask> subtasks(Epic epic);

}
