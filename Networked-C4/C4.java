import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class C4 extends Thread {
    private Socket player1Socket;
    private Socket player2Socket;
    private PrintWriter player1Out;
    private PrintWriter player2Out;
    private Scanner player1In;
    private Scanner player2In;
    private char[][] board;
    private char currentPlayer;
    private boolean gameRunning;
    private Leaderboard leaderboard;
    private String player1Name;
    private String player2Name;
    public C4(Socket player1Socket, Socket player2Socket, String player1Name, String player2Name, Leaderboard leaderboard) {
    this.player1Socket = player1Socket;
    this.player2Socket = player2Socket;
    this.leaderboard = leaderboard;
    this.player1Name = player1Name;
    this.player2Name = player2Name;
    board = new char[6][7]; // Initialize the game board with 6 rows and 7 columns
    currentPlayer = 'R'; // Red player starts
    gameRunning = true;

    // Initialize the game board with empty spaces
    for (int row = 0; row < 6; row++) {
        for (int col = 0; col < 7; col++) {
            board[row][col] = ' '; // Set each cell to an empty space
        }
    }

    try {
        player1In = new Scanner(player1Socket.getInputStream());
        player1Out = new PrintWriter(player1Socket.getOutputStream(), true);
        player2In = new Scanner(player2Socket.getInputStream());
        player2Out = new PrintWriter(player2Socket.getOutputStream(), true);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
private void handleMove(Socket socket, PrintWriter out) {
    try {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        int column = Integer.parseInt(in.readLine());

        if (isValidMove(column)) {
            dropPiece(column);
            out.println("VALID_MOVE");
            sendBoardState(); // Send the updated board state after a valid move
        } else {
            out.println("INVALID_MOVE");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public void run() {
    try {
        player1Out.println("START R");
        player2Out.println("START B");

        // Send the initial board state to both players
        sendBoardState();

        while (gameRunning) {
            // Notify the current player that it's their turn
            if (currentPlayer == 'R') {
                player1Out.println("YOUR_TURN");
                player2Out.println("OPPONENTS_TURN");
                handleMove(player1Socket, player1Out);
            } else {
                player1Out.println("OPPONENTS_TURN");
                player2Out.println("YOUR_TURN");
                handleMove(player2Socket, player2Out);
            }

            // Check for game over conditions
            if (checkForWinner() || checkForDraw()) {
                gameRunning = false;
                sendBoardState();
                break;
            }

            // Switch players
            currentPlayer = (currentPlayer == 'R') ? 'B' : 'R';
        }
    } finally {
     player1Out.println("GAME_OVER");
            player2Out.println("GAME_OVER");

            if (checkForWinner()) {
                if (currentPlayer == 'R') {
                    leaderboard.recordWin(player1Name);
                    leaderboard.recordLoss(player2Name);
                } else {
                    leaderboard.recordWin(player2Name);
                    leaderboard.recordLoss(player1Name);
                }
            }

            // Send the leaderboard to both players
            String leaderboardData = leaderboard.getLeaderboard();
            player1Out.println(leaderboardData);
            player2Out.println(leaderboardData);

            player1Out.close();
            player2Out.close();

        
        try {
            player1Socket.close();
            player2Socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


    private void dropPiece(int column) {
        for (int row = 5; row >= 0; row--) {
            if (board[row][column] == ' ') {
                board[row][column] = currentPlayer;
                break;
            }
        }
    }

    private boolean checkForWinner() {
        // Check rows
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col <= 3; col++) {
                if (board[row][col] == currentPlayer &&
                    board[row][col + 1] == currentPlayer &&
                    board[row][col + 2] == currentPlayer &&
                    board[row][col + 3] == currentPlayer) {
                    return true;
                }
            }
        }

        // Check columns
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row <= 2; row++) {
                if (board[row][col] == currentPlayer &&
                    board[row + 1][col] == currentPlayer &&
                    board[row + 2][col] == currentPlayer &&
                    board[row + 3][col] == currentPlayer) {
                    return true;
                }
            }
        }

        // Check diagonals
        for (int row = 0; row <= 2; row++) {
            for (int col = 0; col <= 3; col++) {
                if (board[row][col] == currentPlayer &&
                    board[row + 1][col + 1] == currentPlayer &&
                    board[row + 2][col + 2] == currentPlayer &&
                    board[row + 3][col + 3] == currentPlayer) {
                    return true;
                }
            }
        }
        for (int row = 0; row <= 2; row++) {
            for (int col = 3; col < 7; col++) {
                if (board[row][col] == currentPlayer &&
                    board[row + 1][col - 1] == currentPlayer &&
                    board[row + 2][col - 2] == currentPlayer &&
                    board[row + 3][col - 3] == currentPlayer) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkForDraw() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                if (board[row][col] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
    
private boolean isValidMove(int column) {
    // Check if the column is within bounds
    if (column < 0 || column >= 7) {
        return false;
    }
    
    // Check if the top row of the column is empty
    return board[0][column] == ' ';
}

private void sendBoardState() {
    // Concatenate the board into a single string
    StringBuilder boardString = new StringBuilder();
    boardString.append("| 0   1   2   3   4   5   6  \n");
    boardString.append("|____________________________\n");
    for (int row = 0; row < 6; row++) {
        boardString.append("|");
        for (int col = 0; col < 7; col++) {
            boardString.append(" ");
            if (board[row][col] == '\0') {
                boardString.append(" - "); // Use '-' for empty cells
            } else {
                boardString.append(board[row][col]);
            }
            boardString.append(" |");
        }
        boardString.append("\n|___|___|___|___|___|___|___|\n");
    }

    // Send the board state to both players
    player1Out.println(boardString.toString());
    player2Out.println(boardString.toString());
}
}

