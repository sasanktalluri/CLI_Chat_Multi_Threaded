import java.io.*;
import java.net.*;
import java.util.*;

// Server class
public class Server {
    // Synchronized list to store active clients
    private static final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket);

                // Create a new handler for this client
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);

                // Start a new thread for this client
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    // Broadcast message to all active clients
    public static void broadcast(String message, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != sender && client.isLoggedIn()) {
                    client.sendMessage(message);
                }
            }
        }
    }

    // Remove a client from the active list
    public static void removeClient(ClientHandler client) {
        synchronized (clients) {
            clients.remove(client);
            System.out.println(client.getName() + " has left the chat.");
        }
    }
}

// ClientHandler class
class ClientHandler implements Runnable {
    private final Socket socket;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private boolean isLoggedIn;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.dis = new DataInputStream(socket.getInputStream());
        this.dos = new DataOutputStream(socket.getOutputStream());
        this.isLoggedIn = true;
    }

    @Override
    public void run() {
        try {
            dos.writeUTF("Welcome to the chat! Type 'logout' to exit.");

            while (isLoggedIn) {
                String received = dis.readUTF();
                if (received.equalsIgnoreCase("logout")) {
                    isLoggedIn = false;
                    break;
                }

                System.out.println("Client: " + received);
                Server.broadcast(received, this);
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + socket);
        } finally {
            closeConnection();
        }
    }

    public void sendMessage(String message) {
        try {
            dos.writeUTF(message);
        } catch (IOException e) {
            System.out.println("Error sending message to client: " + socket);
        }
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public String getName() {
        return socket.toString();
    }

    private void closeConnection() {
        try {
            isLoggedIn = false;
            dis.close();
            dos.close();
            socket.close();
            Server.removeClient(this);
        } catch (IOException e) {
            System.out.println("Error closing client connection: " + socket);
        }
    }
}
