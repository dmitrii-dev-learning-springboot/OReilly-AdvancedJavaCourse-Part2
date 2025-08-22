package org.example.networking.sockets;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AsyncServerCallbackStyle {

    public static void main(String[] args) {
        try {
            AsynchronousServerSocketChannel server =
                    AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(5000));

            System.out.println("Async callback server running on port 5000...");

            // Start accepting connections
            server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
                @Override
                public void completed(AsynchronousSocketChannel client, Void attachment) {
                    System.out.println("Client connected!");

                    // accept the next client (important: accept is one-shot, must re-register)
                    server.accept(null, this);

                    // read from client
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    client.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer buf) {
                            buf.flip();
                            String message = new String(buf.array(), 0, buf.limit());
                            System.out.println("Client says: " + message);

                            // echo back
                            ByteBuffer out = ByteBuffer.wrap(("Echo: " + message).getBytes());
                            client.write(out, out, new CompletionHandler<Integer, ByteBuffer>() {
                                @Override
                                public void completed(Integer result, ByteBuffer attachment) {
                                    System.out.println("Sent echo back to client.");
                                    try {
                                        client.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void failed(Throwable exc, ByteBuffer attachment) {
                                    System.err.println("Failed to write to client: " + exc);
                                }
                            });
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer buf) {
                            System.err.println("Failed to read from client: " + exc);
                        }
                    });
                }

                @Override
                public void failed(Throwable exc, Void attachment) {
                    System.err.println("Failed to accept client: " + exc);
                }
            });

            // Keep server alive
            Thread.currentThread().join();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
