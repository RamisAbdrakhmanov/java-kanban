package controller;

import adapter.LocalDateTimeAdapter;
import com.google.gson.*;
import controller.TZ8.HttpTaskManager;
import controller.TZ8.HttpTaskServer;
import controller.TZ8.KVServer;
import model.task.Epic;
import model.task.Subtask;
import model.task.Task;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {

    HttpTaskManager manager1;
    String uriString = "http://localhost:8080/tasks";
    HttpTaskServer taskServer;
    static KVServer kvServer;

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTimeAdapter.class, new LocalDateTimeAdapter())
            .create();


    @BeforeEach
    public void before() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        manager1 = new HttpTaskManager();
        taskServer = new HttpTaskServer(manager1, "/tasks");

        Task task = new Task("Путеществие", "Добраться", "22.01.2019 17:00", "1000");
        manager1.addTask(task);
        Epic starWars = new Epic("Хогвартс", "заданья на год", "22.01.2014 17:00", "0");
        manager1.addTask(starWars);
        Subtask subtask12 = new Subtask("Звезда смерти"
                , "Построить звезду смерти"
                , "22.01.2018 17:00", "1000", starWars.getId());
        manager1.addTask(subtask12);

        manager1.getTaskById(3);
        manager1.getTaskById(2);
        manager1.getTaskById(1);
    }

    @AfterEach
    public void after() {
        taskServer.serverStop();
        kvServer.stop();
    }

    @Test
    public void endpointTasksGET() throws IOException, InterruptedException {
        URI uri = URI.create(uriString);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        Set<Task> list2 = manager1.getPrioritizedTasks();
        String responseTasks = gson.toJson(list2);

        assertEquals(response.body(), responseTasks, "Не совпадают список отправленный, список прешедший");
    }

    @Test
    public void endpointTasksTaskGET() throws IOException, InterruptedException {
        URI uri = URI.create(uriString + "/task");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        List<Task> list2 = manager1.getTasks();
        String responseTasks = gson.toJson(list2);
        assertEquals(response.body(), responseTasks, "Не совпадают список отправленный, список прешедший");
    }

    @Test
    public void endpointTasksEpicSubtasksGET() throws IOException, InterruptedException {
        int id = 2;
        URI uri = URI.create(uriString + "/epic/" + id);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        List<Subtask> list2 = manager1.subtasks((Epic) manager1.getTaskById(2));
        String responseTasks = gson.toJson(list2);
        assertEquals(response.body(), responseTasks, "Не совпадают список отправленный, список прешедший");
    }

    @Test
    public void endpointTasksTaskIdGET() throws IOException, InterruptedException {
        int id = 1;
        URI uri = URI.create(uriString + "/task" + "/" + id);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        String responseTasks = gson.toJson(manager1.getTaskById(id));

        assertEquals(response.body(), responseTasks, "Не совпадают список отправленный, список прешедший");
    }

    @Test
    public void endpointTasksTaskIdAddPOST() throws IOException, InterruptedException {
        URI uri = URI.create(uriString + "/task");

        String task1 = "4,TASK,Путеществие,NEW,Добраться,-,22.01.2119 17:00,1000";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(task1))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        Task task = manager1.getTaskById(4);
        assertNotNull(task, "Не совпадают список отправленный, список прешедший");
    }

    @Test
    public void endpointTasksTaskIdDELETE() throws IOException, InterruptedException {
        int id = 1;
        URI uri = URI.create(uriString + "/task" + "/" + id);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        assertNull(manager1.getTaskById(id), "Ошибка удаления по id");
    }

    @Test
    public void endpointTasksDELETE() throws IOException, InterruptedException {
        URI uri = URI.create(uriString + "/task");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        assertEquals(new ArrayList<>(), manager1.getTasks(), "Ошибка удаления по id");
    }
}
