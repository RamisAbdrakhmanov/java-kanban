package controller;

import model.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    transient int size;
    transient InMemoryHistoryManager.Node first;
    transient InMemoryHistoryManager.Node last;

    HashMap<Integer, Node> history = new HashMap<>(); //история поиска задач*/

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
            --size;
        } catch (Exception o) {
            System.out.println("Error remove by id in History: " + o);
        }

    }

    @Override //показать историю поиска задач
    public List<Task> getHistory() {
        ArrayList<Task> tasks = new ArrayList<>();
        return getTasks(tasks, last);
    }

    @Override //записать в список истории поиска задач
    public void addTaskInHistory(Task task) {
        if (history.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
        history.put(task.getId(), last);
    }

    void linkLast(Task task) {
        try {
            Node l = last;
            Node newNode = new Node(l, task, null);
            last = newNode;
            if (l == null) {
                first = new Node(null, task, null);
            } else {
                l.next = newNode;
            }
            ++size;
        } catch (Exception e) {
            System.out.println("Error adding ti history: " + e);
        }

    }

    ArrayList<Task> getTasks(ArrayList<Task> tasks, Node node) {
        if (node.prev != null) {
            tasks.add(node.item);
            Node next = node.prev;
            getTasks(tasks, next);
        } else {
            tasks.add(node.item);
            return tasks;
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
