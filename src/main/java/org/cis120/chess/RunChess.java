package org.cis120.chess;

import javax.swing.*;
import javax.swing.border.Border;
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
        status.setFont(new Font(status.getFont().getName(), Font.PLAIN, 14));
        status_panel.add(status);
        final JLabel turnCounter = new JLabel();
        turnCounter.setFont(new Font(status.getFont().getName(), Font.PLAIN, 16));

        // Game board
        final GameWindow board;
        GameWindow board1;
        try {
            board1 = new GameWindow(status, turnCounter);
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
        final Button ai_move_button = new Button("AI Move");
        ai_move_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                ai_move_button.setEnabled(false);
                try {
                    board.aiMove();
                } catch (IllegalStateException exception) {
                    System.out.println("Error while finding AI move: " + exception);
                }
                ai_move_button.setEnabled(true);
            }
        });
        final Button undo_button = new Button("Undo");
        undo_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                board.undoLastMove();
            }
        });

        final JPanel button_panel = new JPanel();

        button_panel.add(turnCounter);
        button_panel.add(reset_button);
        button_panel.add(ai_move_button);
        button_panel.add(undo_button);

        frame.add(button_panel, BorderLayout.EAST);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
