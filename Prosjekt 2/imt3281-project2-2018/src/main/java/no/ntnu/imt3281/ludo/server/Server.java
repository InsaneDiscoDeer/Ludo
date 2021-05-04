package no.ntnu.imt3281.ludo.server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * This is the main class for the server.
 * **Note, change this to extend other classes if desired.**
 *
 * @author
 *
 */

/*
    TODO: Listen for request and send over necessary data
    TODO: Log chat
    TODO: Store every ongoing games and chat room information
 */
public class Server {
    //private final Logger logger = Logger.getLogger(getClass().getName());
    private static boolean shutdown = false;
    private static DBController db;

    private final LinkedBlockingQueue<String> messages = new LinkedBlockingQueue<>(); // List with messages
    private final ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>(); // List of active clients

    /**
     * Creates a cached thread pool
     * - that listens to incoming connections from clients
     * - that listens for new messages to send
     * - incoming messages from clients
     */
     private Server() {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(this::connectionListenerThread);
        executor.execute(this::messageSenderThread);
        executor.execute(this::messageListenerThread);
    }

    public static void main(String[] args) {
        new Server();
        db = new DBController();
        db.establishConnection();
        //db.checkIfUserExist("AAA");
        //db.insertUser(username, password);
    }


    /**
     * Create a thread that listens on port SERVERPORT (specified in the config file)
     * and try to establish a connection with the client.
     * If it succeeds, add the client and store the socket communication.
     * System exits if failed to listen to SERVERPORT or failed to getting client connection
     */
    private void connectionListenerThread() {
        try (ServerSocket listener = new ServerSocket(Config.SERVERPORT)) {
        	System.out.println("Listening on port " + Config.SERVERPORT);
            while (!shutdown) {
                Socket s = null;
                try {
                    s = listener.accept();
                    System.out.println("A new client is connected: " + s);
                } catch (IOException e) {
                    System.out.println("Error while getting client connection: " + Config.SERVERPORT);
                    //logger.log(Level.SEVERE, "Error while getting client connection: " + Config.SERVERPORT, e);
                    System.exit(0);
                }
                try {
                    addClient(s);
                } catch (IOException e) {
                    //logger.log(Level.FINER, "Failed to establish connection with client", e);
                    System.out.println("Failed to establish connection with client");
                }
            }
        } catch (IOException e) {
            //logger.log(Level.SEVERE, "Unable to listen to port" + Config.SERVERPORT, e);
            System.out.println("Unable to listen to port: " + Config.SERVERPORT);
            System.exit(0);
        }
    }

    /**
     * Watches the message queue and send messages to the clients
     */
    private void messageSenderThread() {
        while (!shutdown) {
            try {
                final String message = messages.take();
                clients.forEachValue(100, client -> client.write(message));
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            clients.forEachValue(100, clientHandler -> {
                if (!clientHandler.isActive() && clients.remove(clientHandler.getName()) != null) {
                    clientRemoved(clientHandler);
                }
            });
        }
    }

    /**
     * Listen for messages from the clients and checks
     */
    private void messageListenerThread() {
        while (!shutdown) {
            clients.forEachValue(100, client -> {
                String msg = client.read();
                if (msg != null && msg.equals("Bye")) {
                    msgFromClient(client, msg);
                    if (clients.remove(client.getName()) != null) {
                        clientRemoved(client);
                    }
                } else if (msg != null) {
                	if(msg.startsWith("user:"))
                	{
                		db.insertUser(msg.substring(5,msg.length()), "test2");
                	}
                	else if(msg.startsWith("check:"))
                	{
                		if(db.checkIfUserExist(msg.substring(6,msg.length())))
                			client.write("yeees");
                	}
                    msgFromClient(client, msg);
                    messages.add("MSG:" + client.getName() + ">" + msg);
                }
            });
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    /**
     *
     * @param s
     * @throws IOException
     */
    private void addClient(Socket s) throws IOException {
        final ClientHandler client = new ClientHandler(s);
        clients.forEachKey(100, name -> client.write("JOIN:" + name));
        clients.put(client.getName(), client);
        messages.add("JOIN:" + client.getName());
    }

    /**
     * Will be used for logging chat messages
     * @param client
     * @param msg
     */
    private void msgFromClient(ClientHandler client, String msg) {
        System.out.println(client.getName() + " says: " + msg);
    }

    /**
     *
     * @param client
     */
    private void clientRemoved(ClientHandler client) {
        messages.add("DISCONNECTED:" + client.getName());	// Message will be sent to all other clients

    }
}
