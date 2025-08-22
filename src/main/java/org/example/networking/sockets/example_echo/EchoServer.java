package org.example.networking.sockets.example_echo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Simple Echo Server (blocking)
 * -----------------------------------
 * How to run:
 * 1. Run this server first.
 * 2. Then run EchoClient (or multiple clients) to connect.
 *
 * Switching servers:
 * - If you want to test your other servers (AsyncServerCallbackStyle or AsyncServerFutureBased),
 *   just run the different files
 *   then run the client as usual.
 */

public class EchoServer {
    public static void main(String[] args) {
        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Echo server started on port " + port);

            while (true) {
                Socket client = serverSocket.accept(); // wait for client
                System.out.println("Client connected: " + client.getInetAddress());

                // Handle client in a separate thread (so multiple clients can connect)
                new Thread(() -> handleClient(client)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket client) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

            String msg;
            while ((msg = in.readLine()) != null) {
                System.out.println("Received: " + msg);
                out.println("Echo: " + msg); // send it back
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
