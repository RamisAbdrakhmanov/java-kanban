package controller.TZ8;

import adapter.InstantAdapter;
import adapter.LocalDateTimeAdapter;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import controller.FileBackedTasksManager;
import model.task.Task;

import java.io.IOException;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class HttpTaskManager extends FileBackedTasksManager {
    final static String kTask = "tasks";
    final static String kHistory = "history";
    KVTaskClient client;
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTimeAdapter.class, new LocalDateTimeAdapter()).create();

    public HttpTaskManager() throws IOException, InterruptedException {
        super();
        client = new KVTaskClient("http://localhost:8078");
        try {
            JsonElement jsonTasks = JsonParser.parseString(client.load(kTask));
            if (!jsonTasks.isJsonNull()) {
                Type typeToken = new TypeToken<ArrayList<Task>>() {
                }.getType();
                List<Task> list = gson.fromJson(jsonTasks, typeToken);
                list.forEach(this::addTask);
            }

            JsonElement jsonHistoryList = JsonParser.parseString(client.load(kHistory));
            if (jsonHistoryList != null) {
                JsonArray jsonHistoryArray = jsonHistoryList.getAsJsonArray();
                for (JsonElement jsonTaskId : jsonHistoryArray) {
                    int taskId = jsonTaskId.getAsInt();
                    this.getTaskById(taskId);
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Попытка считать из пустого токина!");
        }

    }

    @Override
    public void save() {
        client.put(kTask, gson.toJson(getTaskHashMap().values()));
        client.put(kHistory, gson.toJson(new ArrayList<>(getHistory())));
    }
}
