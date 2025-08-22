package org.example.networking.sockets.example_echo;

import java.io.*;
import java.net.Socket;

/**
 * Simple Echo Client
 * -----------------------------------
 * How to run:
 * 1. Make sure a server is running (EchoServer, AsyncServerCallbackStyle, AsyncServerFutureBased, etc.)
 *    on the correct port (default: 5000).
 * 2. Run this client. Type messages and press enter.
 * 3. The server will echo back each message.
 *
 * Notes:
 * - You can run multiple clients simultaneously to test multi-client behavior.
 * - To switch servers, just chosen server file that you want to run (same port)
 *   and then start the client.
 */

public class EchoClient {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 5000;

        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server. Type messages to send:");

            String userInput;
            while ((userInput = console.readLine()) != null) {
                out.println(userInput); // send to server
                String response = in.readLine(); // read response
                System.out.println("Server replied: " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
