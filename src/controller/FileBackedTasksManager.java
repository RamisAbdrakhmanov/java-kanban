package controller;


import exteptions.ManagerSaveExceptions;
import model.Status;
import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import utils.Manager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static final InMemoryHistoryManager historyManager = Manager.getDefaultHistory();

    private final String filename = "TasksBase";

    public void save() {
        try (FileOutputStream outputStream = new FileOutputStream(filename)) {
            Map<Integer, Task> taskMap = this.getTaskHashMap();
            LinkedHashMap<Integer, Task> taskLinkedHashMap = taskMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new));
            for (Map.Entry<Integer, Task> taskEntry : taskLinkedHashMap.entrySet()) {
                Task task = taskEntry.getValue();
                outputStream.write((toString(task) + "\n").getBytes(StandardCharsets.UTF_8));
            }
            outputStream.write("\n".getBytes(StandardCharsets.UTF_8));

            String history = historyToString(historyManager);

            outputStream.write(history.getBytes(StandardCharsets.UTF_8));


        } catch (ManagerSaveExceptions | IOException e) {
            e.printStackTrace();
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

    static String historyToString(InMemoryHistoryManager manager) {
        List<String> listId = new ArrayList<>();

        for (Task task : manager.getHistory()) {
            String id = "" + task.getId();
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
        historyManager.addTaskInHistory(task);
    }

    public Task fromString(String value) {
        String[] splitLine = value.split(",");

        int id = Integer.parseInt(splitLine[0]);
        String taskType = splitLine[1];
        String name = splitLine[2];
        Status status = Status.valueOf(splitLine[3]);
        String info = splitLine[4];
        String idEpicStr = splitLine[5];

        switch (taskType) {
            case "TASK":
                Task task = new Task(name, info, status, id);
                this.putTaskToTaskHashMap(id, task);
                this.setNextId(id);
                return task;
            case "EPIC":
                Epic epic = new Epic(name, info, status, id);
                this.putTaskToTaskHashMap(id, epic);
                this.setNextId(id);
                return epic;
            case "SUBTASK":
                int idEpicInt = Integer.parseInt(idEpicStr);
                Subtask subtask = new Subtask(name, info, status, id, idEpicInt);
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
        if (task instanceof Subtask) {
            taskStr = String.join(",",
                    "" + task.getId(),
                    TaskEnum.SUBTASK + "",
                    task.getName(),
                    task.getStatus() + "",
                    task.getInfo(),
                    ((Subtask) task).getIdEpic() + "");
            return taskStr;
        } else if (task instanceof Epic) {
            taskStr = String.join(",",
                    "" + task.getId(),
                    TaskEnum.EPIC + "",
                    task.getName(),
                    task.getStatus() + "",
                    task.getInfo(),
                    "-");
            return taskStr;
        } else {
            taskStr = String.join(",",
                    "" + task.getId(),
                    TaskEnum.TASK + "",
                    task.getName(),
                    task.getStatus() + "",
                    task.getInfo(),
                    "-");
            return taskStr;
        }

    }


    @Override
    public List<Task> getAllTask() {
        return super.getAllTask();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public Task getTaskById(int o) {
        return super.getTaskById(o);
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void changeTask(Task task) {
        super.changeTask(task);
        save();
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

                              

                              
                              