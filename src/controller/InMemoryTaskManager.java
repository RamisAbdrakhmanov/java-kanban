package controller;

import exteptions.ManagerSaveExceptions;
import model.Status;
import model.task.*;
import utils.Manager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final InMemoryHistoryManager historyManager = Manager.getDefaultHistory();
    private Map<Integer, Task> taskHashMap = new HashMap<>(); //хранилище всех задач, где ключом является ID
    private AtomicInteger nextId = new AtomicInteger();


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
    public void addNewTask(Task task) {
        try {
            if (task == null) {
                throw new ManagerSaveExceptions("Попытка создать Task = null");
            }
            boolean check = checkIntersection(task);
            if (check) {
                throw new ManagerSaveExceptions("Задача пересекается");
            }


            if (task.getId() == 0) {
                int id = nextId.incrementAndGet();
                if (task.getId() != 0) { // если id занято, то ищем следующее пустое id.
                    addNewTask(task);
                }
                task.setId(id);
                taskHashMap.put(id, task);
                try {
                    if (task instanceof Subtask) {
                        Subtask subtask = (Subtask) task;

                        int idEpic = subtask.getIdEpic();

                        Epic epic = (Epic) taskHashMap.get(idEpic);

                        epic.addSubtaskInList(subtask);
                        checkStartAndEndTimeEpic(subtask, epic);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            } else {
                changeTask(task);
            }
        } catch (ManagerSaveExceptions e) {
            e.printStackTrace();
        }
    }

    @Override //перезаписываю задачу
    public void changeTask(Task task) {
        try {
            if (task == null) {
                throw new ManagerSaveExceptions("Попытка создать Task = null");
            }
            boolean check = checkIntersection(task);
            if (check) {
                throw new ManagerSaveExceptions("Задача пересекается");
            }
            if ((task.getId() != 0) || taskHashMap.containsKey(task.getId())) {
                taskHashMap.put(task.getId(), task);
                if (task instanceof Subtask) {
                    changeStatusTask((Epic) taskHashMap.get(((Subtask) task).getIdEpic()), (Subtask) task);
                }
            } else {
                System.out.println("Объект не имеет id или был когда-то удален.");
            }

        } catch (ManagerSaveExceptions e) {
            e.printStackTrace();
        }
    }

    @Override //удалю задачу по ID
    public void deleteTaskById(int id) {
        if (taskHashMap.get(id) instanceof Epic) {
            for (Subtask subtask : ((Epic) taskHashMap.get(id)).getSubtasks()) {
                if (historyManager.getMapHistory().containsKey(id)) {
                    historyManager.remove(subtask.getId());
                }
                taskHashMap.remove(subtask.getId());
            }
        }
        if (historyManager.getMapHistory().containsKey(id)) {
            historyManager.remove(id);
        }
        taskHashMap.remove(id);
    }

    //проверка пересечения
    private boolean checkIntersection(Task task) {
        List<Task> list = getPrioritizedTasks();
        for (Task taskSort : list) {
            if (task.getStartTime().isBefore(taskSort.getStartTime())) {
                if (task.getEndTime().isAfter(taskSort.getStartTime())) {
                    return true;
                }
            }

            if (task.getStartTime().isBefore(taskSort.getEndTime())) {
                if (task.getEndTime().isAfter(taskSort.getEndTime())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        List<Task> list = getAllTask().stream()
                .sorted(Comparator.comparing(Task::getStartTime))
                .collect(Collectors.toList());
        return list;
    }


    @Override //просмотр у эпика отдельного списка сабтасков
    public List<Subtask> subtasks(Epic epic) {
        return epic.getSubtasks();
    }

    //отслеживаю изменения статуса большой задачи, при изменении статуса у подзадачи
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

    private void checkStartAndEndTimeEpic(Subtask subtask, Epic epic) {
        LocalDateTime startTime = subtask.getStartTime();
        LocalDateTime startTimeEpic = epic.getStartTime();

        if (startTimeEpic.isAfter(startTime)) {
            epic.setStartTime(startTime);
        }
        epic.setDuration(epic.getDuration() + subtask.getDuration());
    }

    public Map<Integer, Task> getTaskHashMap() {
        return taskHashMap;
    }


    public void setTaskHashMap(Map<Integer, Task> taskHashMap) {
        this.taskHashMap = taskHashMap;
    }

    public void setNextId(int id) {
        this.nextId = new AtomicInteger(id);
    }

    protected void putTaskToTaskHashMap(int id, Task task) {
        taskHashMap.put(id, task);
    }
}
