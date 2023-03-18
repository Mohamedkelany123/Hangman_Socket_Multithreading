import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnection implements Runnable{

    private Socket server;
    private BufferedReader in;
    //private PrintWriter out;

    //HANDLES INPUT FROM THE SERVER
    public ServerConnection(Socket s) throws IOException{
        server = s;
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
    }
    
    @Override
    public void run() {
        try {
                while (true) {
                String serverResponse = in.readLine();
                System.out.println("Server response: " + serverResponse);
                if (serverResponse == null) break;    
            }
        } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        } finally{
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
           
       

    }
    
    
}
