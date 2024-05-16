import java.io.*;
import java.net.*;

public class Connect4Server {
    private static final int PORT = 5555;
    private static final String LEADERBOARD_FILE = "leaderboard.ser";
    private Leaderboard leaderboard;

    public static void main(String[] args) {
        new Connect4Server().start();
    }

    public Connect4Server() {
        leaderboard = new Leaderboard();
        leaderboard.loadLeaderboard(LEADERBOARD_FILE);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running. Waiting for players to connect...");

            while (true) {
                Socket player1Socket = serverSocket.accept();
                System.out.println("Player 1 connected.");
                BufferedReader player1Input = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
                String player1Name = player1Input.readLine();

                Socket player2Socket = serverSocket.accept();
                System.out.println("Player 2 connected.");
                BufferedReader player2Input = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
                String player2Name = player2Input.readLine();

                // Start a new game thread
                C4 game = new C4(player1Socket, player2Socket, player1Name, player2Name, leaderboard);
                game.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            leaderboard.saveLeaderboard(LEADERBOARD_FILE);
        }
    }
}
