import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    final static int ServerPort = 1234;

    public static void main(String args[]) {
        try (Scanner scn = new Scanner(System.in);
             Socket s = new Socket("localhost", ServerPort);
             DataInputStream dis = new DataInputStream(s.getInputStream());
             DataOutputStream dos = new DataOutputStream(s.getOutputStream())) {

            System.out.println("Connected to the server. Type 'logout' to quit.");

            // Thread for sending messages
            Thread sendMessage = new Thread(() -> {
                while (true) {
                    String msg = scn.nextLine();
                    try {
                        dos.writeUTF(msg);
                        dos.flush();
                        if (msg.equalsIgnoreCase("logout")) {
                            System.out.println("Disconnecting from server...");
                            s.close();
                            break;
                        }
                    } catch (IOException e) {
                        System.out.println("Connection closed.");
                        break;
                    }
                }
            });

            // Thread for reading messages
            Thread readMessage = new Thread(() -> {
                while (true) {
                    try {
                        String msg = dis.readUTF();
                        System.out.println("Server: " + msg);
                    } catch (IOException e) {
                        System.out.println("Disconnected from server.");
                        break;
                    }
                }
            });

            sendMessage.start();
            readMessage.start();

            // Wait for threads to finish before closing
            sendMessage.join();
            readMessage.join();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
