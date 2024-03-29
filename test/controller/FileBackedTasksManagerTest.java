package controller;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    public void beforeEach() {
        taskManager = new FileBackedTasksManager();
    }

    @Test
    public void saveAndReadTest() {
        Task task123 = new Task("Task", "Task info", "22.01.2017 17:02", "1000");
        Epic starWars = new Epic("Хогвартс", "заданья на год", "22.01.2014 17:00", "0");
        taskManager.addTask(task123);
        taskManager.addTask(starWars);
        Subtask subtask12 = new Subtask("Звезда смерти"
                , "Построить звезду смерти"
                , "22.01.2018 17:00", "1000", starWars.getId());
        taskManager.addTask(subtask12);
        taskManager.getTaskById(3);
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        List<Task> tasksSave = taskManager.getTasks();
        FileBackedTasksManager manager = new FileBackedTasksManager();
        manager.read();
        List<Task> tasksRead = taskManager.getTasks();
        assertEquals(tasksRead, tasksSave, "Запись и чтение не совпадают");
    }

    @Test
    void saveNullTest() {
        FileBackedTasksManager taskManager = new FileBackedTasksManager();
        taskManager.save();
    }

    @Test
    void readNullTest() {
        FileBackedTasksManager taskManager = new FileBackedTasksManager();
        NullPointerException exception = assertThrows(NullPointerException.class, taskManager::read);
        assertEquals(new NullPointerException().getMessage(), exception.getMessage());
    }


}