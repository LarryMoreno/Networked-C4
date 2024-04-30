import java.io.*;
import java.net.*;

public class Connect4Server {
    private static final int PORT = 5555;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running. Waiting for players to connect...");

            Socket player1Socket = serverSocket.accept();
            System.out.println("Player 1 connected.");
            Socket player2Socket = serverSocket.accept();
            System.out.println("Player 2 connected.");

            // Start a new game thread
            C4 game = new C4(player1Socket, player2Socket);
            game.start();

            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
