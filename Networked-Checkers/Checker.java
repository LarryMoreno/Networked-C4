import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Checker {
    public static String type_BLANK = "BLANK";
    public static String type_RED = "RED";
    public static String type_BLACK = "BLACK";

    public static int width = 100;
    public static int height = 100;
    public static boolean isServer = true;

    public static class Board {
        private JFrame frame = new JFrame();
        private JPanel backBoard = new JPanel();
        private BoardSquare[][] boardSquares = new BoardSquare[8][8];
        private BoardSquare selectedSquare = null;

        Board(boolean player) {
            int numRows = 8;
            int numCols = 8;

            frame.setSize(905, 905);
            backBoard.setSize(900, 900);
            frame.setTitle("Checkers");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            backBoard.setVisible(true);
            backBoard.setLayout(new GridLayout(8, 8));

            for (int r = 0; r < numRows; r++) {
                for (int c = 0; c < numCols; c++) {
                    String type = type_BLANK;
                    if (c % 2 == 0) {
                        if (r == 0 || r == 2) {
                            type = type_RED;
                        } else if (r == 6) {
                            type = type_BLACK;
                        }
                    } else {
                        if (r == 1) {
                            type = type_RED;
                        } else if (r == 5 || r == 7) {
                            type = type_BLACK;
                        }
                    }
                    BoardSquare square = new BoardSquare(r, c, type);
                    backBoard.add(square);
                    boardSquares[r][c] = square;
                }
            }

            frame.add(backBoard);
            if(!isServer)
            {
                // Add mouse listener to handle piece movement
                backBoard.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    int x = e.getX() / width;
                    int y = e.getY() / height;
                    BoardSquare clickedSquare = boardSquares[y][x];
                    System.out.println(e);
                    if (selectedSquare == null) {
                        // No piece selected yet, check if the clicked square has a piece
                        if (clickedSquare.hasPiece() && clickedSquare.getPiece().getColor() == Color.BLACK) {
                            selectedSquare = clickedSquare;
                            selectedSquare.setBorder(new LineBorder(Color.YELLOW, 3));
                        }
                    } else {
                        // Move the selected piece to the clicked square if it's a valid move
                        if (isValidMove(selectedSquare, clickedSquare)) {
                            movePiece(selectedSquare, clickedSquare);
                            selectedSquare.setBorder(null);
                            selectedSquare = null;
                        } else {
                            // Invalid move, reset selection
                            selectedSquare.setBorder(null);
                            selectedSquare = null;
                        }
                    }
                }
                });
            }
            else
            {
                int x = 0;
                int y = 0;
                BoardSquare square = boardSquares[x][y];
                BoardSquare selectedSquare = boardSquares[x][y];
                
                while(square.getPiece().getColor() != Color.RED)
                {
                    x = (int)Math.floor(Math.random() * 600) + 1;
                    y = (int)Math.floor(Math.random() * 600) + 1;
                    
                    square = boardSquares[x][y];
                }
                
                while(selectedSquare.hasPiece() == true)
                {
                    x = (int)Math.floor(Math.random() * 600) + 1;
                    y = (int)Math.floor(Math.random() * 600) + 1;
                    
                    selectedSquare = boardSquares[x][y];
                }
                
                // Move the selected piece to the clicked square if it's a valid move
                if (isValidMove(selectedSquare, square)) {
                    movePiece(selectedSquare, square);
                    selectedSquare.setBorder(null);
                    selectedSquare = null;
                } else {
                    // Invalid move, reset selection
                    selectedSquare.setBorder(null);
                    selectedSquare = null;
                }

            }
        }

        private boolean isValidMove(BoardSquare from, BoardSquare to) {
            // Implement basic move validation logic here
            // For now, let's allow diagonal movement by one square
            int dx = Math.abs(from.getXPos() - to.getXPos());
            int dy = Math.abs(from.getYPos() - to.getYPos());
            return dx == 1 && dy == 1 && !to.hasPiece();
        }

        private void movePiece(BoardSquare from, BoardSquare to) {
            // Move the piece from the "from" square to the "to" square
            to.setPiece(from.getPiece());
            from.removePiece();
            from.repaint();
            to.repaint();
        }
    }

    private static class BoardSquare extends JPanel {
        private int x; //x position of the rectangle measured from top left corner
        private int y; //y position of the rectangle measured from top left corner
        private Piece piece;

        public BoardSquare(int p, int q, String type) {
            this.setPreferredSize(new Dimension(width, height));
            x = p;
            y = q;
            if (!type.equals(type_BLANK)) {
                piece = new Piece(type);
            }
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            Rectangle box = new Rectangle(0, 0, width, height);
            g2.draw(box);
            g2.setPaint(Color.WHITE);
            g2.fill(box);
            if (piece != null) {
                g2.setColor(piece.getColor());
                g2.fillOval(10, 10, width - 20, height - 20);
            }
        }

        public int getXPos() {
            return x;
        }

        public int getYPos() {
            return y;
        }

        public boolean hasPiece() {
            return piece != null;
        }

        public Piece getPiece() {
            return piece;
        }

        public void setPiece(Piece piece) {
            this.piece = piece;
        }

        public void removePiece() {
            this.piece = null;
        }
        
    }

    private static class Piece {
        private Color color;

        public Piece(String type) {
            if (type.equals(type_BLACK)) {
                color = Color.BLACK;
            } else if (type.equals(type_RED)) {
                color = Color.RED;
            }
        }

        public Color getColor() {
            return color;
        }
    }

    public static void main(String[] args) {
        Board game = new Board(false);
    }
}