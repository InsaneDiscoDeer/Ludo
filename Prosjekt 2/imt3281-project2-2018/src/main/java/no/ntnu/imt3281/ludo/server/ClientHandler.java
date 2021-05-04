package no.ntnu.imt3281.ludo.server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler {
    private final Socket s;
    private BufferedReader input;
    private BufferedWriter output;
    private boolean active = true;
    private String name;


     ClientHandler(Socket s) throws IOException {
        this.s = s;
        input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        output = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        name = input.readLine();
        System.out.println("Your username: " + name);
    }


    /**
     * Reads the input from client
     * @return input from client or null if no data was available
     */
     String read() {
        try {
            if(input.ready()) {
                return input.readLine();
            } else {
                return null;
            }
        } catch (IOException e) {
            System.out.println("Could not read!");
            active = false;
            return null;
        }
    }


     void write(String msg) {
        try {
            output.write(msg);
            output.newLine();
            output.flush();
        } catch (IOException e) {
            active = false;
        }
    }

    Socket getSocket() {
        return s;
    }

    public void close() {
        try {
            active = false;
            input.close();
            output.close();
            s.close();
        } catch (IOException e) {

        }
    }

    public boolean isActive() {
        return active;
    }

    public String getName() {
        return name;
    }
}
