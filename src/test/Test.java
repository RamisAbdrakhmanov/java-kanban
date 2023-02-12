package test;

import controller.FileBackedTasksManager;
import controller.TZ8.HttpTaskManager;
import controller.TZ8.HttpTaskServer;
import controller.InMemoryHistoryManager;
import controller.TZ8.KVServer;
import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import utils.Manager;

import java.io.IOException;

public class Test {
    static String name ="/tasks";

    public static void main(String[] args) throws IOException, InterruptedException {
        new KVServer().start();
        InMemoryHistoryManager historyManager = Manager.getDefaultHistory();

        HttpTaskManager manager1 = Manager.isDefaultHttp();

        /*FileBackedTasksManager manager1 = Manager.isDefaultFile();*/

        boolean checkSaveOrRead = true; //переключатель для удобства проверки True записывает False считывает

        if (checkSaveOrRead) {

            Task task = new Task("Путеществие", "Добраться", "22.01.2019 17:00", "33333");
            manager1.addTask(task);

            Task task1 = new Task("Английский", "британ", "22.01.2012 17:01", "33333");
            manager1.addTask(task1);

            Task task2 = new Task("уборка", "Субботник", "22.01.2013 17:02", "10000");
            manager1.addTask(task2);

            Epic starWars = new Epic("Хогвартс", "заданья на год", "22.01.2014 17:00", "0");
            manager1.addTask(starWars);

            Subtask subtask12 = new Subtask("Звезда смерти"
                    , "Построить звезду смерти"
                    , "22.01.2018 17:00", "10000", starWars.getId());
            manager1.addTask(subtask12);

            Subtask subtask13 = new Subtask("Звезда смерти 2"
                    , "что все держится на одном объекте"
                    , "22.01.2015 17:00", "10000", starWars.getId());
            manager1.addTask(subtask13);

            Subtask subtask14 = new Subtask("Звезда смерти 3"
                    , "З финал через кучу лет"
                    , "22.01.2022 17:00", "10000", starWars.getId());
            manager1.addTask(subtask14);


            System.out.println(manager1.getTaskById(5));


            manager1.getTaskById(7);
            manager1.getTaskById(5);
            manager1.getTaskById(3);
            manager1.getTaskById(2);
            manager1.getTaskById(4);

            historyManager.getHistory().forEach(s -> System.out.print(s.getId() + ", "));
            System.out.println(" ");
        } else {

            manager1.read();
            System.out.println(manager1.getTaskHashMap());
            System.out.println(historyManager.getMapHistory());
        }
        HttpTaskServer taskServer = new HttpTaskServer(manager1,name);


    }
}
