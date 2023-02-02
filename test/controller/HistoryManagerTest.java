package controller;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.Manager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    private static InMemoryHistoryManager historyManager;
    private static Task task1;


    @BeforeAll
    static void addTaskInHistoryTest() {
        historyManager = Manager.getDefaultHistory();
        TaskManager taskManager = Manager.isDefault();
        task1 = new Task("Test1 addNewTask",
                "Test1 addNewTask description",
                "22.01.2019 17:20",
                "1000");
        taskManager.addNewTask(task1);
        Task task2 = new Task("Test2 addNewTask",
                "Test2 addNewTask description",
                "22.01.2021 17:20",
                "1000");
        taskManager.addNewTask(task2);
        Epic epic1 = new Epic("Epic1 addNewTask",
                "Epic1 addNewTask description", "22.01.2022 17:00", "1000");
        taskManager.addNewTask(epic1);
        Subtask subtask1 = new Subtask("Subtask1 addNewTask",
                "Subtask1 addNewTask description",
                "22.01.2023 17:20",
                "1000",
                epic1.getId());
        taskManager.addNewTask(subtask1);
        Subtask subtask2 = new Subtask("Subtask2 addNewTask",
                "Subtask2 addNewTask description",
                "22.01.2024 17:20",
                "1000",
                epic1.getId());
        taskManager.addNewTask(subtask2);
        Subtask subtask3 = new Subtask("Subtask2 addNewTask",
                "Subtask3 addNewTask description",
                "22.01.2025 17:20",
                "1000",
                epic1.getId());
        taskManager.addNewTask(subtask3);

        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(6);
        taskManager.getTaskById(1);
        taskManager.getTaskById(5);
        taskManager.getTaskById(3);
        taskManager.getTaskById(2);
        taskManager.getTaskById(4);

        assertNotNull(historyManager.getHistory(), "История пустая");
        assertEquals(4, historyManager.getHistory().get(0).getId(), "История не совпадает");
    }


    @Test
    void removeTest() {
        historyManager.remove(task1.getId());
        assertNull(historyManager.getMapHistory().get(task1.getId()), "Элемент не удален");

    }


    @Test
    void removeNullTest() {
        HistoryManager historyManagerNull = new InMemoryHistoryManager();

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> historyManagerNull.remove(task1.getId()));

        assertEquals("no message resource found for message id: " +
                        "Error remove by id in History: java.lang.NullPointerException"
                , runtimeException.getMessage(), "Удаления из пустого списка неверно");

    }

    @Test
    void getHistoryTest() {
        Integer[] testId = {4, 2, 3, 5, 1, 6};
        List<Integer> idList = new ArrayList<>(List.of(testId));
        List<Integer> list = historyManager.getHistory().stream().map(Task::getId).collect(Collectors.toList());
        assertEquals(idList, list, "История просмотров не совпадает");
    }

    @Test
    void getHistoryNullTest() {
        HistoryManager historyManagerNull = new InMemoryHistoryManager();
        Integer[] testId = {};
        List<Integer> idList = new ArrayList<>(List.of(testId));
        List<Integer> list = historyManagerNull.getHistory().stream().map(Task::getId).collect(Collectors.toList());
        assertEquals(idList, list, "Пустая история просмотров не совпадает");
    }


}