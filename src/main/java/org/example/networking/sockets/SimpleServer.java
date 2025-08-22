package org.example.networking.sockets;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {

//    This version:
//    Runs a server on port 12345.
//    Each client gets a new thread that handles input/output.
//    Multiple clients can talk to the server at once.
    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(5000)) { // Step 1: bind to port
            System.out.println("Server running on port 5000...");

            while (true) { // Step 2: accept clients forever
                Socket clientSocket = server.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Step 3: handle client in new thread
                new Thread(() -> handleClient(clientSocket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void handleClient(Socket socket){
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)){

            writer.println("Hello! You are connected to the server.");
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Client says: " + line);
                writer.println("Echo: " + line);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
