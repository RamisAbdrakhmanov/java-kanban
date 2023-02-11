package controller.TZ8;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;


import adapter.LocalDateTimeAdapter;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controller.FileBackedTasksManager;
import exteptions.ManagerSaveExceptions;
import model.task.Task;
import utils.Manager;


public class TaskHandler implements HttpHandler {
    //Извиняюсь, что я отправил полусырой проект, но мне очень многое непонятно, поэтому хотел спросить
    // 1:мне делать ендпоинтеры отдельные для каждого типа тасков?
    // 2:Как я могу написать тесты для локалхоста, мне свой клиент и для тестов писать?
    // 3:Мне вообще может переписать всю логику тогда с разделением эпиков сабтасков и тасков, потому-что я не понимаю,
    // как сохранять их на сервер.
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTimeAdapter.class, new LocalDateTimeAdapter()).create();
    private FileBackedTasksManager manager = Manager.isDefaultFile();



    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_PRIOR_TASKS:
                getPrioritizedTasks(exchange);
                break;
            case GET_TASKS:
                getTasks(exchange);
                break;
            case GET_TASKS_BY_ID:
                getTaskById(exchange);
                break;
            case DELETE_TASKS:
                deleteAllTasks(exchange);
                break;
            case DELETE_TASK_BY_ID:
                deleteTaskById(exchange);
                break;
            case POST_TASK:
                addOrUpdateTask(exchange);
                break;
            case GET_TASKS_HISTORY:
                getHistory(exchange);
                break;
            default:
                writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }


    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");


        if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
            return Endpoint.GET_PRIOR_TASKS;
        }

        if (pathParts.length == 3
                && pathParts[1].equals("tasks")
                && pathParts[2].equals("task")) {
            switch (requestMethod) {
                case "GET":
                    return Endpoint.GET_TASKS;
                case "POST":
                    return Endpoint.POST_TASK;
                case "DELETE":
                    return Endpoint.DELETE_TASKS;
            }
        }

        if (pathParts.length == 4
                && pathParts[1].equals("tasks")
                && pathParts[2].equals("task")
                && !(pathParts[3].isBlank())) {
            switch (requestMethod) {
                case "GET":
                    return Endpoint.GET_TASKS_BY_ID;
                case "DELETE":
                    return Endpoint.DELETE_TASK_BY_ID;
            }
        }

        if (pathParts.length == 3
                && pathParts[1].equals("tasks")
                && pathParts[2].equals("history")) {
          return Endpoint.GET_TASKS_HISTORY;
        }


        return Endpoint.UNKNOWN;
    }


    public void getTaskById(HttpExchange exchange) {
        Optional<Integer> taskIdOpt = getTaskId(exchange);
        if (taskIdOpt.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор задачи", 400);
            return;
        }
        int taskId = taskIdOpt.get();
        Task task = manager.getTaskById(taskId);
        if (task == null) {
            writeResponse(exchange, "Задача с идентификатором " + taskId + " не найден", 404);
            return;
        }
        writeResponse(exchange, gson.toJson(task), 200);
    }

    public void getTasks(HttpExchange exchange) {
        writeResponse(exchange, gson.toJson(manager.getTasks()), 200);

    }

    public void getPrioritizedTasks(HttpExchange exchange) {
        writeResponse(exchange, gson.toJson(manager.getPrioritizedTasks()), 200);
    }



    public void deleteTaskById(HttpExchange exchange) {
        Optional<Integer> taskIdOpt = getTaskId(exchange);
        if(taskIdOpt.isEmpty()){
            writeResponse(exchange,"Некорректный идентификатор задачи",400);
        }
        int taskId = taskIdOpt.get();
        Task task = manager.getTaskById(taskId);
        if (task == null) {
            writeResponse(exchange, "Задача с идентификатором " + taskId + " не найден", 404);
            return;
        }
        manager.deleteTaskById(taskId);
        writeResponse(exchange, "Задача с идентификатором " + taskId + " удалена", 200);
    }

    public void deleteAllTasks(HttpExchange exchange) {
        manager.deleteAllTasks();
        writeResponse(exchange, "Список задач очищен", 200);
    }

    public void addOrUpdateTask(HttpExchange exchange){
        try {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(),DEFAULT_CHARSET);

            Task task = gson.fromJson(body,Task.class);
            manager.updateTask(task);
            writeResponse(exchange,"Задача добавлена",201);
        }catch (JsonSyntaxException e){
            writeResponse(exchange,"Получен некорректный JSON",400);
            return;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ManagerSaveExceptions("Ошибка при считывание body: ");
        }

    }

    public void getHistory(HttpExchange exchange) {
        writeResponse(exchange, gson.toJson(manager.getHistory()),200);
    }




    private Optional<Integer> getTaskId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[3]));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) {
        try {
            if (responseString.isBlank()) {
                exchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
                exchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
            exchange.close();
        } catch (IOException e) {
            System.out.println("Ошибка в выводе (метод writeResponse): " + e.getMessage());
        }
    }


    enum Endpoint {GET_TASKS, GET_TASKS_BY_ID, GET_PRIOR_TASKS, DELETE_TASKS, DELETE_TASK_BY_ID, POST_TASK
        ,GET_TASKS_HISTORY, UNKNOWN}
}
