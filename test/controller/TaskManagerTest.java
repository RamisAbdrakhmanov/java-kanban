package controller;

import model.Status;
import model.task.Task;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    @Test
    void addNewTaskTest() {
        Task task = new Task("Test123 addNewTask",
                "Test123", "22.01.2113 17:00",
                "1000");
        taskManager.addNewTask(task);
        int taskId = task.getId();

        Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTask();

        assertNotNull(tasks, "Задачи нe возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewTaskSameTimeTest() {
        Task task1 = new Task("Test123 addNewTask",
                "Test123", "22.12.1909 17:00",
                "1000");
        taskManager.addNewTask(task1);
        Task task = new Task("Test123 addNewTask",
                "Test123", "22.12.1909 17:00",
                "1000");
        taskManager.addNewTask(task);
        assertFalse(taskManager.getAllTask().contains(task), "Некорректное добавление пересекающихся задач");
    }

    @Test
    void addNewTaskNullTest() {
        assertDoesNotThrow(() -> taskManager.addNewTask(null), "Некорректная обработка null");
    }

    @Test
    void changeTaskTest() {
        Task task = new Task("Test123 addNewTask",
                "Test123", "22.11.1514 17:00",
                "100");
        taskManager.addNewTask(task);
        task.setStatus(Status.DONE);
        taskManager.changeTask(task);

        assertEquals(task, taskManager.getTaskById(task.getId()), "Задачи не совпадают");
    }

    @Test
    void changeTaskSameTimeTest() {
        Task task1 = new Task("Test123 addNewTask",
                "Test123", "22.12.1909 17:00",
                "1000");
        taskManager.addNewTask(task1);
        Task task = new Task("Test123 addNewTask",
                "Test123", "22.12.1909 17:00",
                "1000");
        taskManager.changeTask(task);
        assertFalse(taskManager.getAllTask().contains(task), "Некорректное добавление пересекающихся задач");
    }

    @Test
    void changeTaskNullTest() {
        assertDoesNotThrow(() -> taskManager.changeTask(null), "Некорректная обработка null");
    }

    @Test
    void changeTaskWrongTest() {
        Task task = new Task(14, "Test addNewTask",
                "Test", Status.DONE,
                "22.01.2222 17:00", "1000");
        taskManager.changeTask(task);

        assertEquals(task, taskManager.getTaskById(task.getId()), "Некорректная обработка незаписанного id");
    }

    @Test
    void deleteAllTaskTest() {
        taskManager.deleteAllTask();
        assertEquals(new ArrayList<>(), taskManager.getAllTask(), "Неверное удаление списка");
    }

    @Test
    void getTaskByIdTest() {
        Task task1 = new Task("Test1 addNewTask",
                "Test1", "22.11.1919 17:00",
                "1000");
        taskManager.addNewTask(task1);
        assertEquals(task1, taskManager.getTaskById(task1.getId()), "Задачи не совпадают");
    }

    @Test
    void getTaskByIdNullTest() {
        int id = 0;
        assertEquals(null, taskManager.getTaskById(id), "Приходит неверное значение");
        assertDoesNotThrow(() -> taskManager.getTaskById(id), "Некорректная обработка null");
    }

    @Test
    void getTaskByIdWrongTest() {
        assertNull(taskManager.getTaskById(123), "Приходит неверное значение");
    }


    @Test
    void deleteTaskById() {
        Task task1 = new Task("Test1 addNewTask",
                "Test1", "22.11.1919 17:00",
                "1000");
        taskManager.addNewTask(task1);
        taskManager.deleteTaskById(task1.getId());
        assertNull(taskManager.getTaskById(task1.getId()), "Находит удаленную задачу");
    }

    @Test
    void deleteTaskByWrongId() {
        assertNull(taskManager.getTaskById(123), "Находит удаленную задачу");
    }

    @Test
    void getPrioritizedTasks() {
        Task task1 = new Task("Test1 addNewTask",
                "Test1", "22.11.1919 17:00",
                "1000");
        taskManager.addNewTask(task1);
        Task task2 = new Task("Test2 addNewTask",
                "Test2",
                "22.12.1909 17:00",
                "1000");
        taskManager.addNewTask(task2);
        Set<Task> tasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        tasks.add(task1);
        tasks.add(task2);


        Set<Task> tasksGetPrior = taskManager.getPrioritizedTasks();

        assertEquals(tasks, tasksGetPrior, "Списки отсортированы неверно");
    }

}
