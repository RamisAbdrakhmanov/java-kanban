package model.task;

import controller.InMemoryTaskManager;
import model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Manager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private static InMemoryTaskManager taskManagers;
    private Epic epic;
    private boolean marker = true;
    private Subtask subtask;
    private Subtask subtask1;
    private Subtask subtask2;
    private List<Subtask> subtasks;


    @BeforeEach
    public void createOneEpicAndThreeSubtask() {
        taskManagers = Manager.isDefault();
        epic = new Epic("Epic", "Epic test","22.01.2099 17:00","0");
        taskManagers.addNewTask(epic);

        subtask = new Subtask("Subtask", "test subtask","22.01.2019 17:00","1000", epic.getId());
        taskManagers.addNewTask(subtask);
        subtask1 = new Subtask("Subtask1","test subtask", "22.01.2019 17:00","1000", epic.getId());
        taskManagers.addNewTask(subtask1);
        subtask2 = new Subtask("Subtask2","test subtask", "22.01.2019 17:00","1000", epic.getId());
        taskManagers.addNewTask(subtask2);



        subtasks = epic.getSubtasks();
    }

    @Test
    public void allSubtaskStatusNEW() {
        for (Subtask value : subtasks) {
            if (epic.getStatus() != Status.NEW) {
                marker = false;
            }
            if (value.getStatus() != Status.NEW) {
                marker = false;
            }
        }
        assertTrue(marker,"All subtasks have status NEW, epic has status NEW. Result false." );
    }

    @Test
    public void allSubtaskStatusDone() {
        subtask.setStatus(Status.DONE);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);

        taskManagers.changeTask(subtask);
        taskManagers.changeTask(subtask1);
        taskManagers.changeTask(subtask2);

        for (Subtask value : subtasks) {
            if (epic.getStatus() != Status.DONE) {
                marker = false;
            }
            if (value.getStatus() != Status.DONE) {
                marker = false;
            }
        }
        assertTrue(marker,"All subtasks have status DONE, epic has status DONE. Result false." );
    }

    @Test
    public void subtaskStatusDoneAndNew() {
        subtask.setStatus(Status.DONE);

        taskManagers.changeTask(subtask);

        for (int i = 0; i < subtasks.size(); i++) {

            assertEquals(Status.IN_PROGRESS,
                    epic.getStatus(),
                    "Subtasks have status DONE and NEW, epic has status IN_PROGRESS. Result false." );
        }
    }

    @Test
    public void subtaskStatusIn_progressOrNew() {
        subtask.setStatus(Status.IN_PROGRESS);

        taskManagers.changeTask(subtask);

        for (int i = 0; i < subtasks.size(); i++) {

            assertEquals(Status.IN_PROGRESS, epic.getStatus(),"Subtask has status IN_PROGRESS, epic has status IN_PROGRESS. Result false." );
        }
    }


}