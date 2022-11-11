package task;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Task {
    static AtomicInteger nextId = new AtomicInteger();
    String name;
    public int id;
    String info;
    String status;
    public HashMap<Integer, List<Subtask>> subtaskHashMap;

    public static void setNextId(AtomicInteger nextId) {
        Task.nextId = nextId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSubtaskHashMap(HashMap<Integer, List<Subtask>> subtaskHashMap) {
        this.subtaskHashMap = subtaskHashMap;
    }

    public static AtomicInteger getNextId() {
        return nextId;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getInfo() {
        return info;
    }

    public String getStatus() {
        return status;
    }


    public Task(String name, String info) {
        this.name = name;
        this.info = info;
        this.status = "NEW";

        id = nextId.incrementAndGet();
        subtaskHashMap=null;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", info='" + info + '\'' +
                ", status='" + status + '\'' +
                ", subtaskHashMap=" + subtaskHashMap +
                '}';
    }
}
