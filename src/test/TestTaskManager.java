package test;

import model.Status;
import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import utils.Manager;


public class TestTaskManager {

    public static void main(String[] args) {

        Task task1 = new Task("TestTask1", "Info test");
        Manager.isDefault().createTask(task1);

        Task task2 = new Task("TestTask2", "Info test");
        Manager.isDefault().createTask(task2);

        Epic epic1 = new Epic("Epic1", "Info test");
        Manager.isDefault().createTask(epic1);

        Epic epic2 = new Epic("Epic2", "Info test");
        Manager.isDefault().createTask(epic2);

        Subtask subtask1 = new Subtask("Subtask1", "Info test", epic1.getId());
        Manager.isDefault().createTask(subtask1);

        Subtask subtask2 = new Subtask("Subtask2", "Info test", epic1.getId());
        Manager.isDefault().createTask(subtask2);

        Subtask subtask3 = new Subtask("Subtask2", "Info test", epic2.getId());
        Manager.isDefault().createTask(subtask3);

        epic1.getSubtasks().add(subtask1);
        epic1.getSubtasks().add(subtask2);
        epic2.getSubtasks().add(subtask3);
        Manager.isDefault().getAllTask();
        System.out.println(Manager.isDefault().getTaskHashMap().get(3));

        Epic epic = (Epic) Manager.isDefault().getTaskHashMap().get(3);
        subtask2.setStatus(Status.DONE);
        Manager.isDefault().changeTask(subtask2);
        System.out.println("Поменя 1 элемент в Эпике на " + Status.DONE);
        System.out.println("Статус у Эпика стал: " + epic.getStatus());


        subtask1.setStatus(Status.DONE);
        Manager.isDefault().changeTask(subtask1);
        System.out.println("Поменяли все статусы на " + Status.DONE);
        System.out.println("Статус у Эпика стал: " + epic.getStatus());

        Manager.isDefault().deleteTaskById(3);
        Manager.isDefault().getAllTask();

        Manager.isDefault().deleteAllTask();
        System.out.println("Пусто");
        Manager.isDefault().getAllTask();


    }
}
