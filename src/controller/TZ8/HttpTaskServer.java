package controller.TZ8;

import com.sun.net.httpserver.HttpServer;
import controller.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager, String nameServer) throws IOException {
        httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext(nameServer, new TaskHandler(taskManager));
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void serverStop(){
        httpServer.stop(0);
    }
}
