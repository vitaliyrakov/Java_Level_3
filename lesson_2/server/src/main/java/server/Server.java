package server;
import commands.Command;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket server;
    private Socket socket;
    private final int PORT = 8189;
    private List<ClientHandler> clients;
    private AuthService authService;
    private final int COUNT_THREAD = 10;
    static final Logger log = Logger.getLogger(String.valueOf(Server.class));

    public Server() {
        clients = new CopyOnWriteArrayList<>();
        authService = new SimpleAuthService();
        ExecutorService pool = Executors.newFixedThreadPool(COUNT_THREAD);

        try {
            server = new ServerSocket(PORT);
            log.info("server started");

            while (true) {
                socket = server.accept();
                log.info("client connected" + socket.getRemoteSocketAddress());
                pool.execute(new ClientHandler(this, socket));
            }

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                pool.shutdown();
                server.close();
                log.info("server close");
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public void broadcastMsg(ClientHandler sender, String msg) {
        String message = String.format("[ %s ] : %s", sender.getNickname(), msg);
        for (ClientHandler c : clients) {
            c.sendMsg(message);
            log.trace("Отправка всем сообщения");
        }
    }

    public void privateMsg(ClientHandler sender, String receiver, String msg) {
        String message = String.format("[ %s ] to [ %s ]: %s", sender.getNickname(), receiver, msg);
        for (ClientHandler c : clients) {
            if (c.getNickname().equals(receiver)) {
                c.sendMsg(message);
                if (!c.equals(sender)) {
                    sender.sendMsg(message);
                    log.trace("Отправка приватного сообщения");
                }
                return;
            }
        }
        sender.sendMsg("not found user: " + receiver);
    }

    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        log.trace("Подписка клиента на получение сообщений");
        broadcastClientList();
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        log.trace("Отписка клиента от получения сообщений");
        broadcastClientList();
    }

    public AuthService getAuthService() {
        return authService;
    }

    public boolean isLoginAuthenticated(String login) {
        for (ClientHandler c : clients) {
            if (c.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    public void broadcastClientList() {
        StringBuilder sb = new StringBuilder(Command.CLIENT_LIST);
        for (ClientHandler c : clients) {
            sb.append(" ").append(c.getNickname());
        }

        String message = sb.toString();

        for (ClientHandler c : clients) {
            c.sendMsg(message);
        }
    }
}
