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


public class ChatServer {
    private static final int PORT = 65500;
    private List<ClientHandler> clientHandlerList;
    private AuthService authService;

    public AuthService getAuthService() {
        return authService;
    }

    public ChatServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Server started");
            /**
             * здесь выбрать нужный сервис аутентификации
             */
//            authService = new BaseAuthService();
            authService = new JDBCAuthService();
            authService.start();
            clientHandlerList = new LinkedList<>();
            while (true) {
                System.out.println("Waiting new connection...");
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            System.out.println("Server error");
            e.printStackTrace();
        } finally {
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
        System.out.println(client.getName() + " subscribed");
    }

    public synchronized void unsubscribe(ClientHandler client) {
        clientHandlerList.remove(client);
        broadcastClientsOnline();
        System.out.println(client.getName() + " unsubscribed");
    }
}