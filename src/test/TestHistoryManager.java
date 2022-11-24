package test;

import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import utils.Manager;

public class TestHistoryManager {
    public static void main(String[] args) {

        Task task = new Task("Путеществие","Добраться до центра Земли");
        Manager.isDefault().createTask(task);

        Task task1 = new Task("Английский","лондон оф зе кэпитал оф грейт британ");
        Manager.isDefault().createTask(task1);

        Task task2 = new Task("уборка","Субботник");
        Manager.isDefault().createTask(task2);

        Epic starWars = new Epic("Хогвартс","заданья на год");
        Manager.isDefault().createTask(starWars);

        Subtask subtask12 = new Subtask("Звезда смерти"
                ,"Построить звезду смерти"
                ,starWars.getId());
        Manager.isDefault().createTask(subtask12);

        Subtask subtask13 = new Subtask("Звезда смерти 2"
                ,"Сделать так, что все держится на одном объекте"
                ,starWars.getId());
        Manager.isDefault().createTask(subtask13);

        Subtask subtask14 = new Subtask("Звезда смерти 3"
                ,"Засрать финал через кучу лет"
                ,starWars.getId());
        Manager.isDefault().createTask(subtask14);

        Epic harryPotter = new Epic("Хогвартс","заданья на год");
        Manager.isDefault().createTask(harryPotter);
        Subtask subtask = new Subtask("Зельеваренье"
                ,"варим оборотное зелье"
                ,harryPotter.getId());
        Manager.isDefault().createTask(subtask);

        Subtask subtask1 = new Subtask("Дуэльный клуб"
                ,"научиться защищаться"
                ,harryPotter.getId());
        Manager.isDefault().createTask(subtask1);

        Subtask subtask2 = new Subtask("Посетить день рождения Николаса"
                ,"уговор дороже денег как говориться"
                ,harryPotter.getId());
        Manager.isDefault().createTask(subtask2);

        Subtask subtask3 = new Subtask("Квиддич"
                ,"треновка ВТ,ЧТ,СБ"
                ,harryPotter.getId());
        Manager.isDefault().createTask(subtask3);

        Manager.isDefault().getTaskById(7);
        Manager.isDefault().getTaskById(8);
        Manager.isDefault().getTaskById(9);
        Manager.isDefault().getTaskById(6);
        Manager.isDefault().getTaskById(8);
        Manager.isDefault().getTaskById(2);
        Manager.isDefault().getTaskById(5);
        Manager.isDefault().getTaskById(12);
        Manager.isDefault().getTaskById(1);
        Manager.isDefault().getTaskById(7);
        Manager.isDefault().getTaskById(8);
        Manager.isDefault().getTaskById(8);
        Manager.isDefault().getTaskById(4);
        Manager.isDefault().getTaskById(1);
        System.out.println("___________________________________________________________________");


        Manager.getDefaultHistory().getHistory().forEach(s-> System.out.println(s.getId()));


    }
}
