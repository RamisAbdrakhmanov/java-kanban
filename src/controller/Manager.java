package controller;

import model.Status;
import model.task.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Manager {
    public Map<Integer, Object> taskHashMap = new HashMap<>();
    int id = 0;
    static AtomicInteger nextId = new AtomicInteger();

    public void getAllTask() {
        for (Map.Entry<Integer, Object> a : taskHashMap.entrySet()) {
            System.out.println(a);
        }

    }

    public void deleteAllTask() {
        taskHashMap.clear();
    }

    public Object getTaskById(int o) {
        return taskHashMap.getOrDefault(o, "Нет такой задачи");
    }

    public void createTask(Object task) {
        id = nextId.incrementAndGet();
        ((Task)task).setId(id);
        taskHashMap.put(id, task);
        try {
            if(task instanceof Subtask){
                ((Epic)taskHashMap.get(((Subtask) task).getIdEpic())).subtasks.add((Subtask) task);
            }
        } catch (Exception e){
            System.out.println("Нульпоинтер = " + e);
        }

    }

    public void changeTask(Task task) {
      try {
          taskHashMap.put(task.getId(), task);
          if(task instanceof Subtask){
              changeStatusTask((Epic) taskHashMap.get(((Subtask) task).getIdEpic()), (Subtask) task);

          }
      } catch (Exception e){
          System.out.println("NullPoint в блоке changeTask");
      }
    }

    private void changeStatusTask(Epic epic, Subtask subtask) {
        if ((subtask.getStatus().equals(Status.IN_PROGRESS)|| subtask.getStatus().equals(Status.DONE))
                && epic.getStatus().equals(Status.NEW)) {
            epic.setStatus(Status.IN_PROGRESS);
        } if (subtask.getStatus() == Status.DONE) {
            for (Subtask subtaskEpic : epic.subtasks) {
                if (!(subtaskEpic.getStatus().equals(Status.DONE))) {
                    return;
                }
            }
            epic.setStatus(Status.DONE);
        }


    }

    public List<Subtask> subtasks(Epic epic) {
        return epic.subtasks;
    }


    public void deleteTaskById(int id) {
        if (taskHashMap.get(id) instanceof Epic) {
            for (Subtask subtask : ((Epic) taskHashMap.get(id)).subtasks) {
                taskHashMap.remove(subtask.getId());
            }
            taskHashMap.remove(id);
        } else {
            taskHashMap.remove(id);
        }
    }

}
