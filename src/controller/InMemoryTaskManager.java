package controller;

import model.Status;
import model.task.*;
import utils.Manager;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryTaskManager implements TaskManager {
    private final InMemoryHistoryManager historyManager = Manager.getDefaultHistory();
    private Map<Integer, Task> taskHashMap = new HashMap<>(); //хранилище всех задач, где ключем является ID
    private final AtomicInteger nextId = new AtomicInteger();


    @Override // показываю все задачи
    public List<Task> getAllTask() {
        return new ArrayList<>(taskHashMap.values());
    }

    @Override //очищаю мапу задач
    public void deleteAllTask() {
        taskHashMap.clear();
    }

    @Override //показываю задачу по ID
    public Task getTaskById(int o) {
        if (taskHashMap.containsKey(o)) {
            historyManager.addTaskInHistory(taskHashMap.get(o));
            return taskHashMap.get(o);
        } else {
            System.out.println("Возможно данного ID нет в базе");
            return null;
        }
    }

    @Override //создаю задачу
    public void createTask(Task task) {
        if (task.getId() == 0) {
            int id = nextId.incrementAndGet();
            task.setId(id);
            taskHashMap.put(id, task);
            try {
                if (task instanceof Subtask) {
                    ((Epic) taskHashMap.get(((Subtask) task).getIdEpic())).getSubtasks().add((Subtask) task);
                }
            } catch (Exception e) {
                System.out.println("Нульпоинтер = " + e);
            }
        } else {
            changeTask(task);
        }

    }

    @Override //перезаписываю задачу
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

    @Override //удалю задачу по ID
    public void deleteTaskById(int id) {
        if (taskHashMap.get(id) instanceof Epic) {
            for (Subtask subtask : ((Epic) taskHashMap.get(id)).getSubtasks()) {
                historyManager.remove(subtask.getId());
                taskHashMap.remove(subtask.getId());
            }
        }
        historyManager.remove(id);
        taskHashMap.remove(id);

    }

    @Override //просмотр у эпика отдельного спика сабтаска
    public List<Subtask> subtasks(Epic epic) {
        return epic.getSubtasks();
    }

    //отслеживаю изменения стаса большой задачи, при изменение статуса у подзадачи
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

    public Map<Integer, Task> getTaskHashMap() {
        return taskHashMap;
    }

    public void setTaskHashMap(Map<Integer, Task> taskHashMap) {
        this.taskHashMap = taskHashMap;
    }
}
