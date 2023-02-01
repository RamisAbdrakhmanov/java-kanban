package test;

import controller.InMemoryHistoryManager;
import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import utils.Manager;

public class FileBackedTasksManager {
    public static void main(String[] args) {
        InMemoryHistoryManager historyManager = Manager.getDefaultHistory();
        controller.FileBackedTasksManager manager1 = Manager.isDefaultFile();

        boolean checkSaveOrRead = true; //переключатель для удобства проверки True записывает False считывает

        if(checkSaveOrRead) {

            Task task = new Task("Путеществие", "Добраться","22.01.2019 17:00","33333");
            manager1.addNewTask(task);

            Task task1 = new Task("Английский", "британ","22.01.2012 17:01","33333");
            manager1.addNewTask(task1);

            Task task2 = new Task("уборка", "Субботник","22.01.2013 17:02","10000");
            manager1.addNewTask(task2);

            Epic starWars = new Epic("Хогвартс", "заданья на год","22.01.2014 17:00","0");
            manager1.addNewTask(starWars);

            Subtask subtask12 = new Subtask("Звезда смерти"
                    , "Построить звезду смерти"
                    ,"22.01.2018 17:00","10000", starWars.getId());
            manager1.addNewTask(subtask12);

            Subtask subtask13 = new Subtask("Звезда смерти 2"
                    , "что все держится на одном объекте"
                    ,"22.01.2015 17:00","10000", starWars.getId());
            manager1.addNewTask(subtask13);

            Subtask subtask14 = new Subtask("Звезда смерти 3"
                    , "З финал через кучу лет"
                    ,"22.01.2022 17:00","10000", starWars.getId());
            manager1.addNewTask(subtask14);



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




    }
}
