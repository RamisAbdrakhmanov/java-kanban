package model.task;

import model.Status;

public class Subtask extends Task {
    private final int idEpic;

    public Subtask(String name, String info, int idEpic) {
        super(name, info);
        this.idEpic = idEpic;
    }

    public Subtask(String name, String info, Status status, int id, int idEpic) {
        super(name, info, status, id);
        this.idEpic = idEpic;
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
