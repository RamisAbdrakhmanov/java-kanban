package model.task;


import model.Status;
import model.TaskEnum;

public class Subtask extends Task {
    private final int idEpic;

    public Subtask(String name, String info, String startTime, String duration, int idEpic) {
        super(name, info, startTime, duration);
        this.idEpic = idEpic;
        super.setTaskEnum(TaskEnum.SUBTASK);
    }

    public Subtask(int id, String name, String info, Status status, String startTime, String duration, int idEpic) {
        super(id, name, info, status, startTime, duration);
        this.idEpic = idEpic;
        super.setTaskEnum(TaskEnum.SUBTASK);
    }


    public int getIdEpic() {
        return idEpic;
    }


    @Override
    public String toString() {
        return super.toString() +
                "Subtask{" +
                "idEpic=" + idEpic +
                '}';
    }
}
