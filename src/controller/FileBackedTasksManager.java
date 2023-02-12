package controller;


import exteptions.ManagerSaveExceptions;
import model.Status;
import model.TaskEnum;
import model.task.Epic;
import model.task.Subtask;
import model.task.Task;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final String filename = "TasksBase";


    public void save() {
        try (BufferedWriter outputStream = new BufferedWriter(new FileWriter(filename))) {

            Map<Integer, Task> taskMap = this.getTaskHashMap();

            List<Task> tasks = taskMap.values().stream()
                    .sorted((Comparator.comparing(Task::getId)))
                    .collect(Collectors.toList());
            for (Task task : tasks) {
                outputStream.write((toString(task)));
                outputStream.newLine();
            }
            outputStream.newLine();

            String history = historyToString();

            outputStream.write(history);


        } catch (IOException e) {
            throw new ManagerSaveExceptions(e.getMessage());
        }
    }

    public void read() {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while (!("".equals(line = br.readLine()))) {
                this.putTaskToTaskHashMap(fromString(line).getId(), fromString(line));
            }

            String history = br.readLine();


            List<Integer> historyId = historyFromString(history);
            historyId.forEach(this::addTaskInHistory);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String historyToString() {
        List<String> listId = new ArrayList<>();

        for (Task task : super.historyManager.getHistory()) {
            String id = String.valueOf(task.getId());
            listId.add(id);
        }

        return String.join(",", listId);
    }

    static List<Integer> historyFromString(String value) {
        String[] idStr = value.split(",");

        List<Integer> historyList = new ArrayList<>();
        for (String s : idStr) {
            int id = Integer.parseInt(s);
            historyList.add(id);
        }

        return historyList;
    }

    public void addTaskInHistory(int id) {
        Task task = this.getTaskById(id);
        super.historyManager.addTaskInHistory(task);
    }

    public Task fromString(String value) {
        String[] splitLine = value.split(",");

        int id = Integer.parseInt(splitLine[0]);
        TaskEnum taskType = TaskEnum.valueOf(splitLine[1]);
        String name = splitLine[2];
        Status status = Status.valueOf(splitLine[3]);
        String info = splitLine[4];
        String idEpicStr = splitLine[5];
        String startTime = splitLine[6];
        String duration = splitLine[7];

        switch (taskType) {
            case TASK:
                Task task = new Task(id, name, info, status, startTime, duration);
                this.putTaskToTaskHashMap(id, task);
                this.setNextId(id);
                return task;
            case EPIC:
                Epic epic = new Epic(id, name, info, status, startTime, duration);
                this.putTaskToTaskHashMap(id, epic);
                this.setNextId(id);
                return epic;
            case SUBTASK:
                int idEpicInt = Integer.parseInt(idEpicStr);
                Subtask subtask = new Subtask(id, name, info, status, startTime, duration, idEpicInt);
                this.putTaskToTaskHashMap(id, subtask);
                Epic epicSubtask = (Epic) (this.getTaskById(idEpicInt));
                epicSubtask.addSubtaskInList(subtask);
                this.setNextId(id);
                return subtask;
            default:
                return null;
        }
    }

    //Create line
    public String toString(Task task) {
        String taskStr;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        String id = String.valueOf(task.getId());
        String taskEnum = String.valueOf(task.getTaskEnum());
        String name = task.getName();
        String status = String.valueOf(task.getStatus());
        String info = task.getInfo();
        String startTime = task.getStartTime().format(formatter);
        String duration = String.valueOf(task.getDuration());

        switch (task.getTaskEnum()) {
            case SUBTASK:
                taskStr = String.join(",",
                        id,
                        taskEnum,
                        name,
                        status,
                        info,
                        String.valueOf(((Subtask) task).getIdEpic()),
                        startTime,
                        duration);

                return taskStr;
            case EPIC:
            case TASK:
                taskStr = String.join(",",
                        id,
                        taskEnum,
                        name,
                        status,
                        info,
                        "-",
                        startTime,
                        duration);

                return taskStr;
            default:
                return null;

        }


    }


    @Override
    public List<Task> getTasks() {
        return super.getTasks();
    }



    @Override
    public Task getTaskById(int o) {
        Task task = super.getTaskById(o);
        save();
        return task;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }
    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        clearHistory();
        save();
    }

    // очистка истории
    public void clearHistory() {
      super.historyManager.last = null;
      super.historyManager.first = null;
    }
    public List<Integer> getHistory(){
        List<Integer> history =  super.historyManager.getHistory().stream().map(Task::getId).collect(Collectors.toList());
        return history;
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public List<Subtask> subtasks(Epic epic) {
        return super.subtasks(epic);

    }

    @Override
    public Map<Integer, Task> getTaskHashMap() {
        return super.getTaskHashMap();
    }

    @Override
    public void setTaskHashMap(Map<Integer, Task> taskHashMap) {
        super.setTaskHashMap(taskHashMap);
        save();
    }
}

                              

                              
                              