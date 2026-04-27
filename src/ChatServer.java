import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {

    private static final int PORT = 12345;
    private static Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {
        System.out.println("Server starting on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message, ClientHandler excludeUser) {
        for (ClientHandler client : clients) {
            if(client != excludeUser){
                client.sendMessage(message);
            }
        }
    }

    static void sendPrivate(String recipient, String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client.getUsername().equalsIgnoreCase(recipient)) {
                client.sendMessage("[Private from " + sender.getUsername() + "]: " + message);
                sender.sendMessage("[You → " + recipient + "]: " + message);
                return;
            }
        }
        sender.sendMessage("[Server]: User '" + recipient + "' not found.");
    }

    static void removeClient(ClientHandler client) {
        clients.remove(client);
        broadcast("[Server]: " + client.getUsername() + " has left the chat.", null);
        System.out.println("User disconnected: " + client.getUsername());
    }

    static String listUsers() {
        if (clients.isEmpty()) return "[Server]: No users connected.";
        StringBuilder sb = new StringBuilder("[Server]: Connected users: ");
        for (ClientHandler client : clients) {
            sb.append(client.getUsername()).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }



    static class ClientHandler implements Runnable {
        private Socket socket;
        private String username;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public String getUsername() {
            return username;
        }


        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);


                out.println("[Server]: Enter your username: ");
                username = in.readLine();
                System.out.println("New user connected: " + username);
                broadcast("[Server]: " + username + " has joined the chat", this);
                out.println("[Server]: Welcome, " + username + "! Type /help for commands.");

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.trim().isEmpty())
                        continue;

                    if (message.startsWith("/")) {
                        handleCommand(message);
                    } else {
                        broadcast("[" + username + " → all]: " + message, this);
                    }
                }

            } catch (IOException e) {
                System.out.println("Connection error: " + e.getMessage());
            } finally {
                ChatServer.removeClient(this);
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



        public void sendMessage(String message) {
            out.println(message);
        }

        private void handleCommand(String command) {
            if(command.equalsIgnoreCase("/help")) {
                out.println("[Server]: Commands: /list, /msg <user> <text>, /all <text>, /quit");
            }else if (command.equalsIgnoreCase("/list")){
                out.println(ChatServer.listUsers());
            }else if (command.startsWith("/msg ")) {
                String[] parts = command.split(" ", 3);
                if (parts.length < 3) {
                    out.println("[Server]: Usage: /msg <username> <message>");
                } else {
                    ChatServer.sendPrivate(parts[1], parts[2], this);
                }
            }else if (command.startsWith("/all ")) {
                String msg = command.substring(5);
                ChatServer.broadcast("[" + username + " → all]: " + msg, this);
            }else if (command.equalsIgnoreCase("/quit")) {
                out.println("[Server]: You have been disconnected.");
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                out.println("[Server]: Unknown command. Try /help");
            }
        }
    }
}