import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

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

    public C4(Socket player1Socket, Socket player2Socket) {
    this.player1Socket = player1Socket;
    this.player2Socket = player2Socket;
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
private void handleMove(Scanner in, PrintWriter out) {
    int column = in.nextInt();

    if (isValidMove(column)) {
        dropPiece(column);
        out.println("VALID_MOVE");
        sendBoardState(); // Send the updated board state after a valid move
    } else {
        out.println("INVALID_MOVE");
    }
}


@Override
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
            } else {
                player1Out.println("OPPONENTS_TURN");
                player2Out.println("YOUR_TURN");
            }

            // Receive and handle moves from the current player
            if (currentPlayer == 'R') {
                handleMove(player1In, player1Out);
            } else {
                handleMove(player2In, player2Out);
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
            if (board[row][column] == '\0') {
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
                if (board[row][col] == '\0') {
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
    return board[0][column] == '\0';
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
