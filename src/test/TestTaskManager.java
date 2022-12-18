package test;

import controller.InMemoryTaskManager;
import model.Status;
import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import utils.Manager;

public class TestTaskManager {

    public static void main(String[] args) {

        InMemoryTaskManager manager1 = Manager.isDefault();

        Task task1 = new Task("TestTask1", "Info test");
        manager1.createTask(task1);

        Task task2 = new Task("TestTask2", "Info test");
        manager1.createTask(task2);

        Epic epic1 = new Epic("Epic1", "Info test");
        manager1.createTask(epic1);

        Epic epic2 = new Epic("Epic2", "Info test");
        manager1.createTask(epic2);

        Subtask subtask1 = new Subtask("Subtask1", "Info test", epic1.getId());
        manager1.createTask(subtask1);

        Subtask subtask2 = new Subtask("Subtask2", "Info test", epic1.getId());
        manager1.createTask(subtask2);

        Subtask subtask3 = new Subtask("Subtask2", "Info test", epic2.getId());
        manager1.createTask(subtask3);

        epic1.getSubtasks().add(subtask1);
        epic1.getSubtasks().add(subtask2);
        epic2.getSubtasks().add(subtask3);

        manager1.getAllTask().forEach(task -> System.out.print(task.getId() + ", "));
        System.out.println("\n" + "_______________________________________________________");

        System.out.println(manager1.getTaskHashMap().get(3));

        Epic epic = (Epic) (manager1.getTaskHashMap().get(3));
        subtask2.setStatus(Status.DONE);
        manager1.changeTask(subtask2);
        System.out.println("Поменя Subtask элемент в Epic на " + Status.DONE);
        System.out.println("Статус у Epic стал: " + epic.getStatus());


        subtask1.setStatus(Status.DONE);
        Manager.isDefault().changeTask(subtask1);
        System.out.println("Поменяли все статусы Subtask на " + Status.DONE);
        System.out.println("Статус у Epic стал: " + epic.getStatus());

        manager1.deleteTaskById(3);
        manager1.getAllTask().forEach(task -> System.out.print(task.getId() + ", "));
        System.out.println("\n" + "_______________________________________________________");

        manager1.deleteAllTask();
        System.out.println("Пусто");
        manager1.getAllTask().forEach(task -> System.out.print(task.getId() + ", "));
        System.out.println("\n" + "_______________________________________________________");
    }
}
