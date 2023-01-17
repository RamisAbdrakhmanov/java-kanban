package test;

import controller.InMemoryHistoryManager;
import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import utils.Manager;

import java.util.List;

public class FileBackedTasksManager {
    public static void main(String[] args) {
        InMemoryHistoryManager historyManager = Manager.getDefaultHistory();
        controller.FileBackedTasksManager manager1 = Manager.isDefaultFile();

        boolean checkSaveOrRead = true; //переключатель для удобства проверки True записывает False считывает

        if(checkSaveOrRead) {
            Task task = new Task("Путеществие", "Добраться до центра Земли");
            manager1.createTask(task);

            Task task1 = new Task("Английский", "лондон оф зе кэпитал оф грейт британ");
            manager1.createTask(task1);

            Task task2 = new Task("уборка", "Субботник");
            manager1.createTask(task2);

            Epic starWars = new Epic("Хогвартс", "заданья на год");
            manager1.createTask(starWars);

            Subtask subtask12 = new Subtask("Звезда смерти"
                    , "Построить звезду смерти"
                    , starWars.getId());
            manager1.createTask(subtask12);

            Subtask subtask13 = new Subtask("Звезда смерти 2"
                    , "Сделать так, что все держится на одном объекте"
                    , starWars.getId());
            manager1.createTask(subtask13);

            Subtask subtask14 = new Subtask("Звезда смерти 3"
                    , "Засрать финал через кучу лет"
                    , starWars.getId());
            manager1.createTask(subtask14);

            Epic harryPotter = new Epic("Хогвартс", "заданья на год");
            manager1.createTask(harryPotter);

            Subtask subtask = new Subtask("Зельеваренье"
                    , "варим оборотное зелье"
                    , harryPotter.getId());
            manager1.createTask(subtask);

            Subtask subtask1 = new Subtask("Дуэльный клуб"
                    , "научиться защищаться"
                    , harryPotter.getId());
            manager1.createTask(subtask1);

            Subtask subtask2 = new Subtask("Посетить день рождения Николаса"
                    , "уговор дороже денег как говориться"
                    , harryPotter.getId());
            manager1.createTask(subtask2);


            System.out.println(manager1.getTaskById(5));
            manager1.getTaskById(11);
            manager1.getTaskById(8);
            manager1.getTaskById(7);
            manager1.getTaskById(9);
            manager1.getTaskById(5);
            manager1.getTaskById(4);
            manager1.getTaskById(10);
            manager1.getTaskById(2);
            manager1.getTaskById(14);
            manager1.getTaskById(1);
            manager1.getTaskById(6);
            manager1.getTaskById(6);
            manager1.getTaskById(15);
            manager1.getTaskById(8);
            manager1.getTaskById(3);
            manager1.getTaskById(2);
            manager1.getTaskById(4);

            Task task14 = new Task("Путеществие", "Добраться до центра Земли");
            manager1.createTask(task);

            historyManager.getHistory().forEach(s -> System.out.print(s.getId() + ", "));
            System.out.println(" ");

            manager1.deleteTaskById(4);
            historyManager.getHistory().forEach(s -> System.out.print(s.getId() + ", "));
            System.out.println(" ");

            historyManager.remove(8);
            historyManager.getHistory().forEach(s -> System.out.print(s.getId() + ", "));
        } else {

            manager1.read();
            System.out.println(manager1.getTaskHashMap());
            System.out.println(historyManager.getMapHistory());
        }




    }
}
