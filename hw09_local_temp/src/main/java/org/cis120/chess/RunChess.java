package org.cis120.chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Runs the chess game
 */
public class RunChess implements Runnable {
    public void run() {
        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Chess");
        frame.setLocation(300, 300);

        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.WEST);
        final JLabel status = new JLabel();
        status.setFont(new Font(status.getFont().getName(), Font.PLAIN, 16));
        status_panel.add(status);

        // Game board
        final GameWindow board;
        GameWindow board1;
        try {
            board1 = new GameWindow(status);
        } catch (IOException e) {
            board1 = null;
        }
        board = board1;
        frame.add(board, BorderLayout.NORTH);

        final Button reset_button = new Button("Reset");
        reset_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                try {
                    board.reset();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        final Button instructions_button = new Button("Instructions");
        instructions_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                final JFrame rules_frame = new JFrame("Game Rules");
                rules_frame.setSize(200, 200);
                String ruleString = "<html> Welcome to chess! This is a turn based game " +
                        "with two players. Each "
                        +
                        "player has to move exactly one piece per turn. Each piece has " +
                        "differing movement capabilities."
                        +
                        " Pieces can eliminate pieces from the other side by going onto " +
                        "their position. A check "
                        +
                        "happens "
                        +
                        "when a piece moves into a position such that it can eliminate the " +
                        "king. The objective of the "
                        +
                        "game is to get the opponent into checkmate, a position such that " +
                        "they cannot move any "
                        +
                        "pieces " +
                        "to "
                        +
                        "avoid losing their king. If it is your turn, to select a piece, click " +
                        "one of the pieces on "
                        +
                        "your " +
                        "side. Green squares will indicate where that piece can move. To make " +
                        "a move, click on one "
                        +
                        "of " +
                        "the green squares. To switch to a different piece, click on any point" +
                        " that is not a green "
                        +
                        "square. You can also exit the game and resume the same game after " +
                        "reopening. There is also a reset button. " +
                        "Enjoy! <html>";
                ruleString = String.format("<html><div WIDTH=%d>%s</div></html>", 500, ruleString);
                final JLabel rules = new JLabel(ruleString);
                rules_frame.add(rules, BorderLayout.CENTER);
                rules_frame.pack();
                rules_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                rules_frame.setVisible(true);
            }
        });

        final JPanel button_panel = new JPanel();

        button_panel.add(reset_button);
        button_panel.add(instructions_button);

        frame.add(button_panel, BorderLayout.EAST);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
