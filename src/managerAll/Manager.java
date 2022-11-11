package managerAll;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {
    public static Map<Integer, Task> taskHashMap = new HashMap<>();

    public static void getAllTask() {
        for (Map.Entry<Integer, Task> a : taskHashMap.entrySet()
        ) {
            System.out.println(a);
        }

    }

    public static void deleteAllTask() {
        taskHashMap.clear();
    }

    public static Object getTaskById(Integer o) {
        if (taskHashMap.containsKey(o)) {
            return taskHashMap.get(o);
        } else {
            return "Нет такой задачи";
        }
    }

    public static void createTask(String name, String info, String typeTask) {
        switch (typeTask) {
            case ("Task"):
                Task task = new Task(name, info);
                taskHashMap.put(task.getId(), task);
                break;
            case ("Epic"):
                Epic epic = new Epic(name, info);
                taskHashMap.put(epic.getId(), epic);
                break;
            case ("Subtask"):
                Subtask subtask = new Subtask(name, info);
                taskHashMap.put(subtask.getId(), subtask);
                for (int i = (subtask.getId() - 1); i > 0; i--) {
                    if (taskHashMap.get(i) instanceof Epic) {
                        ((Epic) taskHashMap.get(i)).subtasks.add(subtask);
                        break;
                    }
                }
                break;
            default:
                System.out.println("Неправильно выбран тип задачи");
                break;
        }

    }

    void changeTask(Task task) {
        taskHashMap.put(task.id, task);
        if (task instanceof Subtask) {
            if (task.getStatus().equals("DONE")) {
                for (int i = (task.getId() - 1); i > 0; i--) {
                    if (taskHashMap.get(i) instanceof Epic) {
                        taskHashMap.get(i).setStatus("DONE");
                        break;
                    }
                    taskHashMap.get(i).setStatus("DONE");
                }
            }
        }
    }

    public static List<Subtask> subtasks(Epic epic) {
        return epic.subtaskHashMap.get(epic.getId());
    }


    void deleteTaskById(int id) {
        taskHashMap.remove(id);
    }
}
