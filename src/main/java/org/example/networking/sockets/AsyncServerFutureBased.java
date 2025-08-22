package org.example.networking.sockets;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsyncServerFutureBased {

    public static void main(String[] args) {
        try (AsynchronousServerSocketChannel server =
                     AsynchronousServerSocketChannel.open()) {

            server.bind(new InetSocketAddress(5000));
            System.out.println("Async server running on port 5000...");

            while (true) {
                // accept() returns a Future
                Future<AsynchronousSocketChannel> future = server.accept();
                AsynchronousSocketChannel client = future.get(); // blocks until a client connects
                System.out.println("Client connected!");

                // Read message
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                Future<Integer> readFuture = client.read(buffer);
                readFuture.get(); // wait for read
                buffer.flip();
                String message = new String(buffer.array(), 0, buffer.limit());
                System.out.println("Client says: " + message);

                // Write back
                buffer.clear();
                buffer.put(("Echo: " + message).getBytes());
                buffer.flip();
                Future<Integer> writeFuture = client.write(buffer);
                writeFuture.get(); // wait for write

                client.close(); // close connection (one-shot example)
            }

        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
