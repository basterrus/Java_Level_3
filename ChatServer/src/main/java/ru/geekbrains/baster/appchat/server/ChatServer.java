package ru.geekbrains.baster.appchat.server;

import ru.geekbrains.baster.appchat.server.authentication.JDBCAuthService;
import ru.geekbrains.baster.appchat.common.MessageDTO;
import ru.geekbrains.baster.appchat.common.MessageType;
import ru.geekbrains.baster.appchat.server.authentication.AuthService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChatServer {
    private static final Logger LOG = LogManager.getLogger(ChatServer.class.getName());
    private static final int PORT = 65500;
    private List<ClientHandler> clientHandlerList;
    private AuthService authService;
    private ExecutorService executorService;

    public AuthService getAuthService() {
        return authService;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public ChatServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            LOG.trace("Server started");
            authService = new JDBCAuthService();
            authService.start();
            clientHandlerList = new LinkedList<>();
            executorService = Executors.newCachedThreadPool();
            while (true) {
                LOG.trace("Waiting new connection...");
                Socket socket = serverSocket.accept();
                LOG.trace("Client connected");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            LOG.error("Server error", e);
        } finally {
            if (executorService != null) {
                executorService.shutdown();
            }
            if (authService != null) {
                authService.stop();
            }
        }
    }

    public synchronized boolean isNickBusy(String nick) {
        for (ClientHandler client : clientHandlerList) {
            if (client.getName().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void broadcastClientsOnline() {
        MessageDTO dto = new MessageDTO();
        dto.setMessageType(MessageType.CLIENTS_LIST_MESSAGE);
        List<String> usersOnLine = new LinkedList<>();
        for (ClientHandler client : clientHandlerList) {
            usersOnLine.add(client.getName());
        }
        dto.setUsersOnline(usersOnLine);
        broadcastMessage(dto);
    }

    public synchronized void broadcastMessage(MessageDTO dto) {
        for (ClientHandler client : clientHandlerList) {
            client.sendMessage(dto);
        }
    }

    public synchronized void privateMessage(MessageDTO dto) {
        for (ClientHandler client : clientHandlerList) {
            if (client.getName().equals(dto.getTo())) {
                client.sendMessage(dto);
                break;
            }
        }
    }

    public synchronized void subscribe(ClientHandler client) {
        clientHandlerList.add(client);
        broadcastClientsOnline();
        LOG.info("{} subscribed", client.getName());
    }

    public synchronized void unsubscribe(ClientHandler client) {
        clientHandlerList.remove(client);
        broadcastClientsOnline();
        LOG.info("{} unsubscribed", client.getName());
    }
}