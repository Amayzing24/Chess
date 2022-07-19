package org.cis120.chess;

import java.util.Map;
import java.util.Scanner;

/**
 * A testing class that plays the game on the print terminal
 */
public class ChessPrint {
    private final static String[] ROW_MAP = new String[] { "A", "B", "C", "D", "E", "F", "G", "H" };
    private final static String[] PIECE_MAP = new String[] { "K", "P", "R", "B", "H", "Q" };

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        ChessBoard board = new ChessBoard();

        int resp;
        while (true) {
            printBoard(board);
            System.out.println("Enter position of piece to move");
            String line = scan.nextLine();
            Scanner scan2 = new Scanner(line);
            Position posReq = new Position(scan2.nextInt(), scan2.nextInt());
            System.out.println(posReq);
            Piece p = board.getPiece(posReq);
            if (p != null && p.getSide() == board.getTurnSide()) {
                Map<Position, Move> moves = p.getPossibleMoves(board);
                System.out.println("Possible moves: " + moves);
                System.out.println("Enter position of where to move");
                line = scan.nextLine();
                scan2 = new Scanner(line);
                posReq = new Position(scan2.nextInt(), scan2.nextInt());
                Move m = moves.get(posReq);
                if (m != null) {
                    System.out.println(m.toString());
                    resp = board.makeMove(m);
                    if (resp == 1) {
                        System.out.println("Check");
                    } else if (resp == 2) {
                        System.out.println("Checkmate");
                        break;
                    } else if (resp == 3) {
                        System.out.println("Stalemate");
                        break;
                    }
                }
            }
        }
        printBoard(board);
    }

    public static void printBoard(ChessBoard board) {
        System.out.println("  0  1  2  3  4  5  6  7");
        for (int i = 0; i < 8; i++) {
            String line = i + " ";
            for (int j = 0; j < 8; j++) {
                Piece p = board.getPiece(new Position(i, j));
                if (p == null) {
                    line += "**" + " ";
                } else {
                    String color;
                    if (p.getSide() == 1) {
                        color = "W";
                    } else {
                        color = "B";
                    }
                    line += color + PIECE_MAP[p.getPieceID()] + " ";
                }
            }
            System.out.println(line);
        }
    }
}
