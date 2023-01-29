package controller;

import model.Status;
import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {

    private TaskManager taskManager;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Subtask subtask1;
    private Subtask subtask2;
    private Subtask subtask3;


    @BeforeEach
    void beforeEach() {
        taskManager = Manager.isDefault();
        task1 = new Task("Test1 addNewTask",
                "Test1", "22.01.2019 17:00",
                "1000");
        taskManager.addNewTask(task1);
        task2 = new Task("Test2 addNewTask",
                "Test2",
                "22.02.2019 17:00",
                "1000");
        taskManager.addNewTask(task2);

        epic1 = new Epic("Epic1 addNewTask",
                "Epic1 addNewTask description",
                "22.02.2099 17:00",
                "1000");
        taskManager.addNewTask(epic1);

        subtask1 = new Subtask("Subtask1 addNewTask",
                "Subtask1",
                "22.03.2019 17:00",
                "1000", epic1.getId());
        taskManager.addNewTask(subtask1);
        subtask2 = new Subtask("Subtask2 addNewTask",
                "Subtask2",
                "22.03.2019 17:00",
                "1000", epic1.getId());
        taskManager.addNewTask(subtask2);
        subtask3 = new Subtask("Subtask2 addNewTask",
                "Subtask3", "22.03.2019 17:00",
                "1000", epic1.getId());
        taskManager.addNewTask(subtask3);
    }


    @Test
    void AddNewTaskTest() {
        Task task = new Task("Test123 addNewTask",
                "Test123", "22.01.2019 17:00",
                "1000");
        taskManager.addNewTask(task);
        int taskId = task.getId();

        Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTask();

        assertNotNull(tasks, "Задачи нe возвращаются.");
        assertEquals(7, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(6), "Задачи не совпадают.");

    }

    @Test
    void AddNewTaskNullTest() {
        assertDoesNotThrow(() -> taskManager.addNewTask(null), "Некорректная обработка null");
    }

    @Test
    void ChangeTaskTest() {
        Task task = new Task("Test123 addNewTask",
                "Test123", "22.01.2019 17:00",
                "1000");
        taskManager.addNewTask(task);
        task.setStatus(Status.DONE);
        taskManager.changeTask(task);

        assertEquals(task, taskManager.getTaskById(task.getId()), "Задачи не совпадают");
    }

    @Test
    void ChangeTaskNullTest() {
        assertDoesNotThrow(() -> taskManager.changeTask(null), "Некорректная обработка null");
    }

    @Test
    void ChangeTaskWrongTest() {
        Task task = new Task(14, "Test addNewTask",
                "Test", Status.DONE,
                "22.01.2019 17:00", "1000");
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
        taskManager.deleteTaskById(task1.getId());
        assertNull(taskManager.getTaskById(task1.getId()), "Находит удаленную задачу");
    }

    @Test
    void deleteTaskByWrongId() {
        assertNull(taskManager.getTaskById(123), "Находит удаленную задачу");
    }

    @Test
    void getPrioritizedTasks() {
        List<Task> tasks = List.of(task1, task2, epic1, subtask1, subtask2, subtask3);

        List<Task> tasksGetPrior = taskManager.getPrioritizedTasks();

        tasks.stream().sorted(Comparator.comparing(Task::getStartTime)).collect(Collectors.toList());

        assertEquals(tasks, tasksGetPrior, "Списки отсортированы неверно");
    }


}
