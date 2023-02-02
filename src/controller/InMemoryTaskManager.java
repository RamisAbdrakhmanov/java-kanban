package controller;

import exteptions.ManagerSaveExceptions;
import model.Status;
import model.TaskEnum;
import model.task.*;
import utils.Manager;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryTaskManager implements TaskManager {
    private final InMemoryHistoryManager historyManager = Manager.getDefaultHistory();
    private Map<Integer, Task> taskHashMap = new HashMap<>();//хранилище всех задач, где ключом является ID
    private AtomicInteger nextId = new AtomicInteger();
    private Set<Task> priorTask = new TreeSet<>(Comparator.comparing(Task::getStartTime));


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
                throw new ManagerSaveExceptions("Задача пересекается по времени");
            }


            if (task.getId() == 0) {
                int id = nextId.incrementAndGet();

                // если id занято, то ищем следующее пустое id.
                if (task.getId() != 0) {
                    addNewTask(task);
                }
                task.setId(id);
                taskHashMap.put(id, task);
                try {
                    if (task.getTaskEnum() == TaskEnum.SUBTASK) {
                        Subtask subtask = (Subtask) task;

                        int idEpic = subtask.getIdEpic();

                        Epic epic = (Epic) taskHashMap.get(idEpic);

                        epic.addSubtaskInList(subtask);

                        changeStatusAndTimeEpic(subtask, epic);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            } else {
                changeTask(task);
            }
        } catch (ManagerSaveExceptions e) {
            System.out.println(e.getMessage());
        }
    }

    @Override //перезаписываю задачу
    public void changeTask(Task task) {
        try {
            if (task == null) {
                throw new ManagerSaveExceptions("Попытка перезаписать Task = null");
            }

            boolean check = checkIntersection(task);

            if (check) {
                throw new ManagerSaveExceptions("Задача пересекается по времени");
            }

            if ((task.getId() != 0) || taskHashMap.containsKey(task.getId())) {
                taskHashMap.put(task.getId(), task);

                if (task.getTaskEnum() == TaskEnum.SUBTASK) {
                    Subtask subActual = (Subtask) task;
                    Epic epic = (Epic) taskHashMap.get(subActual.getIdEpic());

                    Subtask subMarker = null;
                    for (Subtask subtask : epic.getSubtasks()) { //честно не знаю как более адекватно поменять список
                        if (subtask.getId() == task.getId()) {    // сабтасков у эпика.
                            subMarker = subtask;
                        }
                    }
                    if (subMarker != null) {
                        epic.getSubtasks().remove(subMarker);
                        epic.getSubtasks().add(subActual);
                    } //очень не нравится кусок с 105 строчки и до сюда, но пока не могу придумать как поменять


                    changeStatusAndTimeEpic((Subtask) task, epic);
                }

            } else {
                System.out.println("Объект не имеет id или был когда-то удален.");
            }

        } catch (ManagerSaveExceptions e) {
            System.out.println(e.getMessage());
        }
    }

    @Override //удалю задачу по ID
    public void deleteTaskById(int id) {
        Task task = taskHashMap.get(id);

        switch (task.getTaskEnum()) {
            case EPIC:

                for (Subtask subtask : ((Epic) taskHashMap.get(id)).getSubtasks()) {
                    if (historyManager.getMapHistory().containsKey(id)) {
                        historyManager.remove(subtask.getId());
                    }
                    taskHashMap.remove(subtask.getId());
                }
                break;
            case SUBTASK:

                Subtask subtask = (Subtask) task;
                Epic epic = (Epic) taskHashMap.get(subtask.getIdEpic());

                epic.getSubtasks().remove(subtask);

                changeStatusAndTimeEpic(subtask, epic);
                break;

        }
        if (historyManager.getMapHistory().containsKey(id)) {
            historyManager.remove(id);
        }
        taskHashMap.remove(id);

    }

    //проверка пересечения
    private boolean checkIntersection(Task task) {
        Set<Task> list = getPrioritizedTasks();

        for (Task taskSort : list) {
            boolean targetZone1 = (
                    task.getStartTime().isBefore(taskSort.getStartTime())
                            &&
                            task.getEndTime().isAfter(taskSort.getStartTime())
            );

            boolean targetZone2 = (
                    task.getStartTime().isBefore(taskSort.getEndTime())
                            &&
                            task.getEndTime().isAfter(taskSort.getEndTime())
            );

            boolean targetZone3 = (
                    task.getStartTime().equals(taskSort.getStartTime())
                            &&
                            task.getEndTime().equals(taskSort.getEndTime())
            );

            if (targetZone1 || targetZone2 || targetZone3) {
                if (task.getId() != taskSort.getId()) {
                    return true;
                }
            }


        }
        return false;
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        priorTask.addAll(taskHashMap.values());
        return priorTask;
    }


    @Override //просмотр у epic отдельного списка subtask
    public List<Subtask> subtasks(Epic epic) {
        return epic.getSubtasks();
    }

    //отслеживаю изменения большой задачи, при изменении подзадачи
    private void changeStatusAndTimeEpic(Subtask subtask, Epic epic) {

        for (Subtask sub : epic.getSubtasks()) {
            if (sub.getStartTime().isBefore(epic.getStartTime())) {
                epic.setStartTime(sub.getStartTime());
            }
        }

        for (Subtask sub : epic.getSubtasks()) {
            if (sub.getEndTime().isAfter(epic.getEndTime())) {
                long duration = Duration.between(epic.getStartTime(), subtask.getEndTime()).getSeconds() * 60;
                epic.setDuration(duration);
            }

        }

        switch (epic.getSubtasks().get(0).getStatus()) { // переписал получение статуса
            case IN_PROGRESS:
                epic.setStatus(Status.IN_PROGRESS);
                break;
            case NEW:
                for (Subtask sub : epic.getSubtasks()) {
                    epic.setStatus(Status.NEW);
                    if (sub.getStatus() != Status.NEW) {
                        epic.setStatus(Status.IN_PROGRESS);
                        break;
                    }
                }
                break;
            case DONE:
                for (Subtask sub : epic.getSubtasks()) {

                    if (sub.getStatus() != Status.DONE) {
                        epic.setStatus(Status.IN_PROGRESS);
                        break;
                    }
                    epic.setStatus(Status.DONE);
                }
                break;

            default:
                break;
        }

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

