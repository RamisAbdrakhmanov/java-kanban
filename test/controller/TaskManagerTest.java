package controller;

import model.Status;
import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/*abstract */class TaskManagerTest /* <T extends TaskManager> */ {
    //1.для проверки статуса Epic создал отдельный класс и там все что с ним связано проверил.

    //2. Не могу понять как сделать систему на основе дженериков правильно.
    //
    // Все, что в /* */ если раскомментировать и закомментировать 26 строку, удалить 37, получится как просят в тз,
    // но я не могу понять почему у меня не работает, что я делаю не так там. В классе InMemoryTaskManager тоже все ок.


    /* protected T taskManager;*/
    protected TaskManager taskManager;
    protected Task task1;
    protected Task task2;
    protected Epic epic1;
    protected Subtask subtask1;
    protected Subtask subtask2;
    protected Subtask subtask3;


    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
        task1 = new Task("Test1 addNewTask",
                "Test1", "22.11.1919 17:00",
                "1000");
        taskManager.addNewTask(task1);
        task2 = new Task("Test2 addNewTask",
                "Test2",
                "22.12.1909 17:00",
                "1000");
        taskManager.addNewTask(task2);

        epic1 = new Epic("Epic1 addNewTask",
                "Epic1 addNewTask description",
                "22.02.1899 17:00",
                "1000");
        taskManager.addNewTask(epic1);

        subtask1 = new Subtask("Subtask1 addNewTask",
                "Subtask1",
                "22.03.1011 17:00",
                "1000", epic1.getId());
        taskManager.addNewTask(subtask1);
        subtask2 = new Subtask("Subtask2 addNewTask",
                "Subtask2",
                "21.03.3312 17:00",
                "1000", epic1.getId());
        taskManager.addNewTask(subtask2);
        subtask3 = new Subtask("Subtask2 addNewTask",
                "Subtask3", "23.03.1419 17:00",
                "1000", epic1.getId());
        taskManager.addNewTask(subtask3);
    }


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
        assertEquals(7, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(6), "Задачи не совпадают.");
    }

    @Test
    void addNewTaskSameTimeTest() {
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
        Set<Task> tasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(subtask1);
        tasks.add(epic1);
        tasks.add(subtask2);
        tasks.add(subtask3);

        Set<Task> tasksGetPrior = taskManager.getPrioritizedTasks();

        assertEquals(tasks, tasksGetPrior, "Списки отсортированы неверно");
    }

}
