import java.io.*;
import java.net.*;

public class Connect4Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 5555;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);

                if (response.equals("GAME_OVER")) {
                    break;
                }
                
                if (response.startsWith("|")) {
                    // Skip printing intermediate board states
                    continue;
                }

                String userInput = consoleInput.readLine();
                out.println(userInput);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
