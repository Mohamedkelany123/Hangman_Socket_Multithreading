import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerConnection implements Runnable{

    private Socket server;
    private BufferedReader in;
    private String currentResponse;

    //HANDLES INPUT FROM THE SERVER
    public ServerConnection(Socket s) throws IOException{
        server = s;
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
    }
    

    public void setCurrentMsg(String response){
        this.currentResponse = response;
    }

    public String getCurrentResponse(){
        return currentResponse;
    }
    @Override
    public void run() {
        try {
                while (true) {
                String serverResponse = in.readLine();
                setCurrentMsg(serverResponse);
                System.out.println(serverResponse);
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
