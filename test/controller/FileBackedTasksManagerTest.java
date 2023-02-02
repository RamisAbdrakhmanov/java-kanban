package controller;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import org.junit.jupiter.api.Test;
import utils.Manager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest {


    @Test
    void saveAndReadTest() {
        FileBackedTasksManager manager = Manager.isDefaultFile();
        Task task2 = new Task("уборка", "Субботник", "22.01.2017 17:02", "1000");
        manager.addNewTask(task2);

        Epic starWars = new Epic("Хогвартс", "заданья на год", "22.01.2014 17:00", "0");
        manager.addNewTask(starWars);

        Subtask subtask12 = new Subtask("Звезда смерти"
                , "Построить звезду смерти"
                , "22.01.2018 17:00", "1000", starWars.getId());
        manager.addNewTask(subtask12);

        manager.getTaskById(3);
        manager.getTaskById(2);
        manager.getTaskById(1);

        List<Task> tasksSave = manager.getAllTask();

        manager.read();
        List<Task> tasksRead = manager.getAllTask();

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

        NullPointerException exception = assertThrows(NullPointerException.class, () -> taskManager.read());

        assertEquals(new NullPointerException().getMessage(), exception.getMessage());
    }


}