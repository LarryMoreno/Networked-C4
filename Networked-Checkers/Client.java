import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        
        if (args.length != 2) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }
        
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        Socket imSocket = null;
        PrintWriter out = null;
        ObjectInputStream in = null;
        //"localhost","4444" 
        
        try{
            imSocket = new Socket(hostName, portNumber);
            out = new PrintWriter(imSocket.getOutputStream(), true);
            in = new ObjectInputStream(imSocket.getInputStream());
            //inMessage = (Message) in.readObject();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
        BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
        
        
        
        out.close();
        in.close();
        stdIn.close();
        imSocket.close();
    }
}
