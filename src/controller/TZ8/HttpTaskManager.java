package controller.TZ8;
import adapter.InstantAdapter;
import com.google.gson.*;
import controller.FileBackedTasksManager;
import model.task.Task;

import java.io.IOException;

import java.time.Instant;
import java.util.ArrayList;


public class HttpTaskManager extends FileBackedTasksManager {
    final static String kTask = "tasks";
    final static String kHistory = "history";
    KVTaskClient client;
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();

    public HttpTaskManager(String uriString) throws IOException, InterruptedException {
        super();
        client = new KVTaskClient(uriString);

        JsonElement jsonTasks = JsonParser.parseString(client.load(kTask));
        if (!jsonTasks.isJsonNull()) {
            JsonArray jsonTasksArray = jsonTasks.getAsJsonArray();
            for (JsonElement jsonTask : jsonTasksArray) {
                Task task = gson.fromJson(jsonTask, Task.class);
                this.addTask(task);
            }
        }

        JsonElement jsonHistoryList = JsonParser.parseString(client.load(kHistory));
        if (jsonHistoryList != null){
        JsonArray jsonHistoryArray = jsonHistoryList.getAsJsonArray();
        for (JsonElement jsonTaskId : jsonHistoryArray) {
            int taskId = jsonTaskId.getAsInt();
            this.getTaskById(taskId);
        }
    }

    }

    @Override
    public void save() {
        client.put(kTask, gson.toJson(getTaskHashMap().values()));
        client.put(kHistory, gson.toJson(new ArrayList<>(getHistory())));
    }
}
