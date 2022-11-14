import controller.Manager;
import model.Status;
import model.task.Epic;
import model.task.Subtask;
import model.task.Task;


public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task1 = new Task("TestTask1", "Info test");
        manager.createTask(task1);

        Task task2 = new Task("TestTask2", "Info test");
        manager.createTask(task2);

        Epic epic1 = new Epic("Epic1", "Info test");
        manager.createTask(epic1);

        Epic epic2 = new Epic("Epic2", "Info test");
        manager.createTask(epic2);

        Subtask subtask1 = new Subtask("Subtask1", "Info test", epic1.getId());
        manager.createTask(subtask1);

        Subtask subtask2 = new Subtask("Subtask2", "Info test", epic1.getId());
        manager.createTask(subtask2);

        Subtask subtask3 = new Subtask("Subtask2", "Info test", epic2.getId());
        manager.createTask(subtask3);

        epic1.getSubtasks().add(subtask1);
        epic1.getSubtasks().add(subtask2);
        epic2.getSubtasks().add(subtask3);
        manager.getAllTask();
        System.out.println(manager.getTaskHashMap().get(3));

        Epic epic = (Epic) manager.getTaskHashMap().get(3);
        subtask2.setStatus(Status.DONE);
        manager.changeTask(subtask2);
        System.out.println("Поменя 1 элемент в Эпике на " + Status.DONE);
        System.out.println("Статус у Эпика стал: " + epic.getStatus());


        subtask1.setStatus(Status.DONE);
        manager.changeTask(subtask1);
        System.out.println("Поменяли все статусы на " + Status.DONE);
        System.out.println("Статус у Эпика стал: " + epic.getStatus());

        manager.deleteTaskById(3);
        manager.getAllTask();

        manager.deleteAllTask();
        System.out.println("Пусто");
        manager.getAllTask();


    }
}
