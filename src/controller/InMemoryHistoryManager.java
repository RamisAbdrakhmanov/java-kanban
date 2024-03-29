package controller;

import exteptions.ManagerSaveExceptions;
import model.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static jdk.nashorn.internal.runtime.ECMAErrors.getMessage;

public class InMemoryHistoryManager implements HistoryManager {
    transient InMemoryHistoryManager.Node first;
    transient InMemoryHistoryManager.Node last;

    private final HashMap<Integer, Node> history = new HashMap<>();

    public HashMap<Integer, Node> getMapHistory() {
        return history;
    }


    @Override //удалить элемент из истории просмотра
    public void remove(int id) {
        try {
            Node x = history.get(id);
            Node next = x.next;
            Node prev = x.prev;
            if (prev == null) {
                this.first = next;
            } else {
                prev.next = next;
                x.prev = null;
            }
            if (next == null) {
                last = prev;
            } else {
                next.prev = prev;
                x.next = null;
            }
            x.item = null;
            history.remove(id); // добавил удаление из истории при удалении из списка
        } catch (Exception o) {
            getMessage("Error remove by id in History: " + o);
        }

    }

    @Override //показать историю поиска задач
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override //записать в список истории поиска задач
    public void addTaskInHistory(Task task) {
        try {

            if (task == null) {
                System.out.println(task);
                throw new ManagerSaveExceptions("Попытка записать null в историю");
            }
        } catch (ManagerSaveExceptions e) {
            System.out.println(e.getMessage());
        }
        if (history.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
        history.put(task.getId(), last);
    }

    private void linkLast(Task task) {
        try {
            Node l = last;
            Node newNode = new Node(l, task, null);
            last = newNode;
            if (l == null) {
                first = new Node(null, task, null);
            } else {
                l.next = newNode;
            }
        } catch (Exception e) {
            getMessage("Error adding in History: " + e);
        }

    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node l = last;
        while (l != null) {
            if (l.prev != null) {
                tasks.add(l.item);
                l = l.prev;
            } else {
                tasks.add(l.item);
                return tasks;
            }
        }
        return tasks;
    }

    private static class Node {
        public Task item;
        public Node next;
        public Node prev;

        Node(InMemoryHistoryManager.Node prev, Task element, InMemoryHistoryManager.Node next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }


}
