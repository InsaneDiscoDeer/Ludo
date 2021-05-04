package no.ntnu.imt3281.ludo.client;

import java.io.*;
import java.net.Socket;

public class Connection {
    private final Socket s;
    public final BufferedReader input;
    private final BufferedWriter output;

    /**
     * Creates a new connection to the server
     * and keep hold on to the socket that communicates with the server
     * @throws IOException
     */
    public Connection() throws IOException {
        s = new Socket(Config.SERVERNAME, Config.SERVERPORT);
        input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        output = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
    }

    /**
     * Send message to server
     * @param text Message to be send to server
     * @throws IOException
     */
    public void send(String text) throws IOException {
        output.write(text);
        output.newLine();
        output.flush();
    }


    /**
     * Sends a message to the server notifying it about this client
     * leaving and then closes the connection.
     */
    public void close() {
        try {
            send("Bye");
            output.close();
            input.close();
            s.close();
        } catch(IOException e) {

        }
    }
}
