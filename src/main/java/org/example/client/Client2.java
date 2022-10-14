package org.example.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client2 {
    public static void main(String[] args) {
        String host = "netology.homework";
        int port = 8080;

        try(Socket clientSocket = new Socket(host, port);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            try(BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
            ) {
                interaction(reader, in, out, true);
                String message = "";
                while(!message.equals("/exit")) {
                    try {
                        if (reader.ready()) {
                            message = reader.readLine();
                            out.println(message);
                        } else {
                            if (in.ready()) {
                                System.out.println(in.readLine());
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String interaction(
        BufferedReader reader, BufferedReader in, PrintWriter out, Boolean clientAnswer
    ) throws IOException {
        System.out.println(in.readLine());
        String message = "";
        if (clientAnswer) {
            message = reader.readLine();
            out.println(message);
        }
        return message;
    }
}
