package controller;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import org.junit.jupiter.api.Test;
import utils.Manager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest {
    controller.FileBackedTasksManager manager = Manager.isDefaultFile();

    @Test
    void saveAndReadTest() {
        Task task2 = new Task("уборка", "Субботник", "22.01.2019 17:02", "10000");
        manager.addNewTask(task2);

        Epic starWars = new Epic("Хогвартс", "заданья на год", "22.01.2019 17:00", "0");
        manager.addNewTask(starWars);

        Subtask subtask12 = new Subtask("Звезда смерти"
                , "Построить звезду смерти"
                , "22.01.2018 17:00", "10000", starWars.getId());
        manager.addNewTask(subtask12);

        manager.getTaskById(3);
        manager.getTaskById(2);
        manager.getTaskById(1);

        List<Task> tasksSave = manager.getAllTask();

        manager.read();
        List<Task> tasksRead = manager.getAllTask();

        assertEquals(tasksRead, tasksSave, "Запись и чтение не совпадают");


    }

}