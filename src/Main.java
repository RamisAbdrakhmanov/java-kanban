import managerAll.Manager;
import task.Epic;
import task.Task;

public class Main {

    public static void main(String[] args) {
        Manager.createTask("Имя", "Фио", "Task");
        Manager.createTask("Epic", "Фио", "Epic");
        Manager.createTask("Subtask", "Фио", "Subtask");
        Manager.createTask("Subtask", "Фио", "Subtask");

        Manager.getAllTask();

        System.out.println(Manager.getTaskById(3));




    }
}
