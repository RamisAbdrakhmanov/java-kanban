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

        epic1.subtasks.add(subtask1);
        epic1.subtasks.add(subtask2);
        epic2.subtasks.add(subtask3);
        manager.getAllTask();

        subtask2.setStatus(Status.DONE);
        manager.changeTask(subtask2);
        manager.getAllTask();

        subtask1.setStatus(Status.DONE);
        manager.changeTask(subtask1);
        manager.getAllTask();

        manager.deleteTaskById(3);
        manager.getAllTask();

        manager.deleteAllTask();
        System.out.println("Пусто");
        manager.getAllTask();


    }
}
