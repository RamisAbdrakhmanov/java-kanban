package controller;

import model.Status;
import model.task.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Manager {
    private Map<Integer, Object> taskHashMap = new HashMap<>();
    private final AtomicInteger nextId = new AtomicInteger();

    public void getAllTask() {
        for (Map.Entry<Integer, Object> task : taskHashMap.entrySet()) {
            System.out.print(task.getKey() + ", ");
        }
        System.out.println("");
    }

    public void deleteAllTask() {
        taskHashMap.clear();
    }

    public Object getTaskById(int o) {
        return taskHashMap.getOrDefault(o, "Нет такой задачи");
    }

    public void createTask(Object task) {
        if (((Task) task).getId() == 0) {
            int id = nextId.incrementAndGet();
            ((Task) task).setId(id);
            taskHashMap.put(id, task);
            try {
                if (task instanceof Subtask) {
                    ((Epic) taskHashMap.get(((Subtask) task).getIdEpic())).getSubtasks().add((Subtask) task);
                }
            } catch (Exception e) {
                System.out.println("Нульпоинтер = " + e);
            }
        } else {
            changeTask((Task) task);
        }

    }

    public void changeTask(Task task) {
        if ((task.getId() != 0) || taskHashMap.containsKey(task.getId())) {
            taskHashMap.put(task.getId(), task);
            if (task instanceof Subtask) {
                changeStatusTask((Epic) taskHashMap.get(((Subtask) task).getIdEpic()), (Subtask) task);

            }
        } else {
            System.out.println("Объект не имеет id или был когда-то удален.");
        }
    }

    private void changeStatusTask(Epic epic, Subtask subtask) {
        if ((subtask.getStatus().equals(Status.IN_PROGRESS) || subtask.getStatus().equals(Status.DONE))
                && epic.getStatus().equals(Status.NEW)) {
            epic.setStatus(Status.IN_PROGRESS);
        }
        if (subtask.getStatus() == Status.DONE) {
            for (Subtask subtaskEpic : epic.getSubtasks()) {
                if (!(subtaskEpic.getStatus().equals(Status.DONE))) {
                    return;
                }
            }
            epic.setStatus(Status.DONE);
        }


    }

    public List<Subtask> subtasks(Epic epic) {
        return epic.getSubtasks();
    }


    public void deleteTaskById(int id) {
        if (taskHashMap.get(id) instanceof Epic) {
            for (Subtask subtask : ((Epic) taskHashMap.get(id)).getSubtasks()) {
                taskHashMap.remove(subtask.getId());
            }
            taskHashMap.remove(id);
        } else {
            taskHashMap.remove(id);
        }
    }

    public Map<Integer, Object> getTaskHashMap() {
        return taskHashMap;
    }

    public void setTaskHashMap(Map<Integer, Object> taskHashMap) {
        this.taskHashMap = taskHashMap;
    }
}
