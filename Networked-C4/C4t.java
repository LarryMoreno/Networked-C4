import java.util.Scanner;

public class C4t {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        char[][] grid = new char[6][7];
        
        // Initialize the game grid
        initializeGrid(grid);
        
        // Create players
        Player player1 = new Player("Red", 'R');
        Player player2 = new Player("Blue", 'B');
        
        // Start the game loop
        int turn = 1;
        Player currentPlayer = player1; // Start with player 1
        boolean winner = false;
        
        while (!winner && turn <= 42) {
            display(grid);
            System.out.println("Player " + currentPlayer.getName() + ", choose a column: ");
            int column = in.nextInt();
            
            // Validate and make the move
            if (isValidMove(column, grid)) {
                makeMove(column, currentPlayer, grid);
                winner = isWinner(currentPlayer.getColor(), grid);
                if (winner) {
                    System.out.println(currentPlayer.getName() + " won!");
                } else {
                    // Switch players
                    currentPlayer = (currentPlayer == player1) ? player2 : player1;
                    turn++;
                }
            } else {
                System.out.println("Invalid move. Please choose another column.");
            }
        }
        
        // Display final game state
        display(grid);
        if (!winner) {
            System.out.println("Tie game");
        }
    }
    public static void display(char[][] grid) {
    System.out.println("  0   1   2   3   4   5   6 ");
    System.out.println(" ___________________________");
    for (int row = 0; row < grid.length; row++) {
        System.out.print("|");
        for (int col = 0; col < grid[0].length; col++) {
            System.out.print(" ");
            if (grid[row][col] == 'R') {
                System.out.print("\u25cf "); // Red checker
            } else if (grid[row][col] == 'B') {
                System.out.print("\u25cb "); // Blue checker
            } else {
                System.out.print("\u2001 "); // Empty slot
            }
            System.out.print("|");
        }
        System.out.println();
        System.out.println("|___|___|___|___|___|___|___|");
    }
    System.out.println();
    }
    public static void initializeGrid(char[][] grid) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                grid[row][col] = ' ';
            }
        }
    }
    
    // Validate if the move is valid
    public static boolean isValidMove(int column, char[][] grid) {
        if (column < 0 || column >= grid[0].length) {
            return false; // Invalid column
        }
        return grid[0][column] == ' '; // Check if the column is not full
    }
    
    // Make a move for the current player
    public static void makeMove(int column, Player player, char[][] grid) {
        for (int row = grid.length - 1; row >= 0; row--) {
            if (grid[row][column] == ' ') {
                grid[row][column] = player.getColor();
                break;
            }
        }
    }
    public static boolean isWinner(char player, char[][] grid) {
    // Check for horizontal wins
    for (int row = 0; row < grid.length; row++) {
        for (int col = 0; col <= grid[0].length - 4; col++) {
            if (grid[row][col] == player &&
                grid[row][col + 1] == player &&
                grid[row][col + 2] == player &&
                grid[row][col + 3] == player) {
                return true;
            }
        }
    }

    // Check for vertical wins
    for (int col = 0; col < grid[0].length; col++) {
        for (int row = 0; row <= grid.length - 4; row++) {
            if (grid[row][col] == player &&
                grid[row + 1][col] == player &&
                grid[row + 2][col] == player &&
                grid[row + 3][col] == player) {
                return true;
            }
        }
    }

    // Check for diagonal wins (upward)
    for (int row = 3; row < grid.length; row++) {
        for (int col = 0; col <= grid[0].length - 4; col++) {
            if (grid[row][col] == player &&
                grid[row - 1][col + 1] == player &&
                grid[row - 2][col + 2] == player &&
                grid[row - 3][col + 3] == player) {
                return true;
            }
        }
    }

    // Check for diagonal wins (downward)
    for (int row = 0; row <= grid.length - 4; row++) {
        for (int col = 0; col <= grid[0].length - 4; col++) {
            if (grid[row][col] == player &&
                grid[row + 1][col + 1] == player &&
                grid[row + 2][col + 2] == player &&
                grid[row + 3][col + 3] == player) {
                return true;
            }
        }
    }

    return false;
}


}