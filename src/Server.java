import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private static ArrayList<User> users = FileUserManager.loadUsers();
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        int port = 8888;
        ServerSocket listener = null;
        Socket clientSocket = null;

        try {
            listener = new ServerSocket(port);
            System.out.println("Server is running on port " + port);

            while (true) {
                clientSocket = listener.accept();
                System.out.println("New client connected: " + clientSocket);
                ClientHandler clientThread = new ClientHandler(clientSocket, users,clients);
                clients.add(clientThread);
                pool.execute(clientThread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (listener != null) {
                    listener.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void addUser(User user) {
        users.add(user);
    }

    public static void addClient(ClientHandler client) {
        clients.add(client);
    }

   

    
}
