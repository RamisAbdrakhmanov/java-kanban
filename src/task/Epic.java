package task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Epic extends Task{
    static AtomicInteger nextIdEpic = new AtomicInteger();
    public int idEpic ;
    public  List<Subtask> subtasks;

    public Epic(String name, String info) {
        super(name, info);
        idEpic=nextIdEpic.incrementAndGet();
        subtaskHashMap = new HashMap<>();
        subtasks = new ArrayList<>();
        subtaskHashMap.put(idEpic,subtasks);
    }

    @Override
    public String toString() {
        return "Epic{" +

                "name='" + name + '\'' +
                ", id=" + id +
                ", info='" + info + '\'' +
                ", status='" + status + '\'' +
                ", idEpic=" + idEpic +
                ", subtaskHashMap=" + subtaskHashMap +
                '}';
    }
}
