package org.cis120.chess;

/**
 * CIS 120 HW09 - Chess
 * (c) University of Pennsylvania
 * Created by Amay Tripathi in Fall 2021
 */

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * This class instantiates a ChessBoard object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 */
@SuppressWarnings("serial")
public class GameWindow extends JPanel {

    private ChessBoard board; // model for the game
    private Position posSelected;
    private JLabel msgBoard;
    private Map<Position, Move> possMoves;
    boolean gameOver;
    BufferedWriter bw;
    BufferedReader br;

    private final Color color1 = new Color(120, 44, 171);
    private final Color color2 = new Color(176, 139, 201);
    // 16, 135, 70
    private final Color highlightColor = new Color(147, 237, 158);
    private final Font mainFont = new Font("Serif", Font.PLAIN, 16);
    private final String dir = "images/";
    private final Image[] whiteImages = new Image[] {
        ImageIO.read(new File(dir + "Chess_klt60.png")),
        ImageIO.read(new File(dir + "Chess_plt60.png")),
        ImageIO.read(new File(dir + "Chess_rlt60.png")),
        ImageIO.read(new File(dir + "Chess_blt60.png")),
        ImageIO.read(new File(dir + "Chess_nlt60.png")),
        ImageIO.read(new File(dir + "Chess_qlt60.png")) };
    private final Image[] blackImages = new Image[] {
        ImageIO.read(new File(dir + "Chess_kdt60.png")),
        ImageIO.read(new File(dir + "Chess_pdt60.png")),
        ImageIO.read(new File(dir + "Chess_rdt60.png")),
        ImageIO.read(new File(dir + "Chess_bdt60.png")),
        ImageIO.read(new File(dir + "Chess_ndt60.png")),
        ImageIO.read(new File(dir + "Chess_qdt60.png")) };

    // Game constants
    public static final int BOARD_WIDTH = 800;
    public static final int BOARD_HEIGHT = 800;

    /**
     * Initializes the game board.
     */
    public GameWindow(JLabel msgBoard) throws IOException {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        board = new ChessBoard(); // initializes model for the game
        posSelected = null;
        possMoves = new HashMap<>();
        this.msgBoard = msgBoard;
        this.msgBoard.setText("Game started. White turn.");
        gameOver = false;

        // Create a file reader to read in previous game state
        try {
            br = new BufferedReader(new FileReader("files/gamestate.txt"));
        } catch (FileNotFoundException e) {
            br = null;
        }

        // Read in the previous game state, resetting the board in case the file is not
        // valid
        while (br != null) {
            String line;
            try {
                line = br.readLine();
                if (line == null) {
                    break;
                }
                Scanner scan = new Scanner(line);
                try {
                    Piece p = board.getPiece(new Position(scan.nextInt(), scan.nextInt()));
                    Position newPos = new Position(scan.nextInt(), scan.nextInt());
                    Position elimPos = new Position(scan.nextInt(), scan.nextInt());
                    Move m;
                    if (elimPos.getRow() == -1 || elimPos.getCol() == -1) {
                        m = new Move(p, newPos, board);
                    } else {
                        m = new Move(p, newPos, board, board.getPiece(elimPos));
                    }
                    int next = scan.nextInt();
                    if (next == 1) {
                        m.makeCastle();
                    }
                    int resp = board.makeMove(m);
                    String message = m.toString() + ". ";
                    if (resp == 1) {
                        message += "Check. " + board.getTurnColor() + "'s turn.";
                    } else if (resp == 2) {
                        message += "Checkmate. " + board.getTurnColor() + " wins!";
                        gameOver = true;
                    } else if (resp == 3) {
                        message += "Stalemate. Who doesn't like a draw?";
                    } else {
                        message += board.getTurnColor() + "'s turn.";
                    }
                    updateMessage(message);
                } catch (NoSuchElementException e) {
                    reset();
                    break;
                }
            } catch (IOException e) {
                reset();
                break;
            }
        }

        try {
            bw = new BufferedWriter(new FileWriter("files/gamestate.txt", true));
        } catch (FileNotFoundException e) {
            bw = null;
        }

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                if (p.y > 800 || p.x > 800) {
                    return;
                }
                Position pos = new Position(p.y / 100, p.x / 100);
                Piece piece = board.getPiece(pos);

                if (posSelected == null && !gameOver) {
                    if (piece != null && piece.getSide() == board.getTurnSide()) {
                        posSelected = pos;
                        possMoves = board.getPiece(posSelected).getPossibleMoves(board);
                    }
                } else if (!gameOver) {
                    if (possMoves.containsKey(pos)) {
                        Move m = possMoves.get(pos);
                        String message = m.toString() + ". ";
                        try {
                            if (bw != null) {
                                bw.write(m.toFileString());
                                bw.newLine();
                                bw.flush();
                            }
                        } catch (IOException ex) {
                            bw = null;
                        }

                        int resp = board.makeMove(possMoves.get(pos));
                        if (resp == 1) {
                            message += "Check. " + board.getTurnColor() + "'s turn.";
                        } else if (resp == 2) {
                            message += "Checkmate. " + board.getTurnColor() + " wins!";
                            gameOver = true;
                        } else if (resp == 3) {
                            message += "Stalemate. Who doesn't like a draw?";
                        } else {
                            message += board.getTurnColor() + "'s turn.";
                        }
                        updateMessage(message);
                    }
                    posSelected = null;
                    possMoves = new HashMap<>();
                }
                repaint(); // repaints the game board
            }
        });
    }

    /**
     * Reset the game state
     * 
     * @throws IOException
     */
    public void reset() throws IOException {
        board = new ChessBoard(); // initializes model for the game
        posSelected = null;
        possMoves = new HashMap<>();
        msgBoard.setText("Game started. White turn.");
        gameOver = false;
        try {
            bw = new BufferedWriter(new FileWriter("files/gamestate.txt", false));
        } catch (FileNotFoundException e) {
            bw = null;
        }

        repaint();
    }

    public void updateMessage(String msg) {
        msgBoard.setText(msg);
    }

    /**
     * Draws the game board.
     *
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw squares and label left-most and bottom-most tiles
        for (int i = 0; i <= 7; i++) {
            boolean flip;
            if (i % 2 == 0) {
                flip = true;
            } else {
                flip = false;
            }
            for (int j = 0; j <= 7; j++) {
                Position pos = new Position(j, i);
                if (possMoves.get(pos) != null || pos.equals(posSelected)) {
                    g.setColor(highlightColor);
                } else if (flip) {
                    g.setColor(color2);
                } else {
                    g.setColor(color1);
                }
                g.fillRect(i * 100, j * 100, 100, 100);
                flip = !flip;

                g.setFont(mainFont);
                if (i == 0) {
                    g.setColor(Color.white);
                    g.drawString(Integer.toString(8 - j), i * 100 + 10, j * 100 + 20);
                }
                if (j == 7) {
                    g.setColor(Color.white);
                    g.drawString(Position.CHESS_LETTERS[i], i * 100 + 80, j * 100 + 90);
                }
            }
        }

        // Draw lines
        for (int i = 0; i <= 8; i++) {
            g.setColor(Color.black);
            g.drawLine(i * 100, 0, i * 100, 800);
            g.drawLine(0, i * 100, 800, i * 100);
        }

        // Draw pieces
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = board.getPiece(new Position(i, j));
                if (p != null) {
                    if (p.getSide() == 1) {
                        g.drawImage(whiteImages[p.getPieceID()], j * 100 + 20, i * 100 + 20, this);
                    } else {
                        g.drawImage(blackImages[p.getPieceID()], j * 100 + 20, i * 100 + 20, this);
                    }
                }
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
