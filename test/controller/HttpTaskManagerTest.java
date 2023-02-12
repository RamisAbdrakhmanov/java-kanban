package controller;

import controller.TZ8.HttpTaskManager;
import controller.TZ8.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import utils.Manager;

import java.io.IOException;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    @BeforeAll
    public static void before() throws IOException {
        new KVServer().start();
    }

    @BeforeEach
    public void beforeEach() throws IOException {

        try {
            taskManager = new HttpTaskManager();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    @AfterEach
    public void afterEach(){
        HistoryManager historyManager = Manager.getDefaultHistory();
        if(!(historyManager.getHistory().isEmpty())) {
            taskManager.clearHistory();
        }
    }
}
