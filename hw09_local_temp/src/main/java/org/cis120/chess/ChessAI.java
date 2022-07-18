package org.cis120.chess;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ChessAI {
    private ChessBoard board;

    private final static int DEPTH = 4;

    private int states;

    public ChessAI(ChessBoard board) {
        this.board = board;
        states = 0;
    }

    public double evaluatePosition(int turn) {
        states++;
        if (states % 20000 == 0) {
            System.out.println("States: " + states);
        }
        double total = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = board.getPiece(new Position(i, j));
                if (p != null) {
                    total += turn * p.getPoints();
                }
            }
        }

        return total;
    }

    public Move getBestMove() {
        states = 0;
        Move best = minimax(1, board.getTurnSide(), Integer.MIN_VALUE, Integer.MAX_VALUE);

        if (best == null) {
            board.updateCheckStatus(board.getTurnSide() * -1);
            throw new IllegalStateException();
        }

        System.out.println("States evaluated: " + states);
        System.out.println("Best move value: " + best.getPoints());

        return best;
    }

    private Move minimax(int level, int turn, double alpha, double beta) {
        List<Piece> pieces = new LinkedList<>();
        Move bestMove = null;
        double bestPoints;

        if (level % 2 == 1) {
            bestPoints = Integer.MIN_VALUE;
        } else {
            bestPoints = Integer.MAX_VALUE;
        }

        for (Piece p : board.getCurrSidePieces()) {
            pieces.add(p);
        }

        for (Piece p : pieces) {
            Map<Position, Move> moves = p.getPossibleMoves(board);

            for (Position pos : moves.keySet()) {
                Move m = moves.get(pos);
                int code = board.makeMove(m);

                if (code == 2) {
                    board.setSide(board.getTurnSide() * -1);
                    if (level % 2 == 1) {
                        m.setPoints(Integer.MAX_VALUE);
                    } else {
                        m.setPoints(Integer.MIN_VALUE);
                    }
                    board.undoLastMove();
                    return m;
                }

                if (level < DEPTH) {
                    Move future = minimax(level + 1, turn, alpha, beta);
                    if (future == null) {
                        board.undoLastMove();
                        continue;
                    }
                    if ((level % 2 == 1 && future.getPoints() > bestPoints) ||
                            (level % 2 == 0 && future.getPoints() < bestPoints)) {
                        bestMove = m;
                        bestPoints = future.getPoints();
                        m.setPoints(bestPoints);
                    }
                    if (level % 2 == 1) {
                        alpha = Math.max(alpha, bestPoints);
                    } else {
                        beta = Math.min(beta, bestPoints);
                    }
                } else {
                    m.setPoints(evaluatePosition(turn));
                    if ((level % 2 == 1 && m.getPoints() > bestPoints) ||
                            (level % 2 == 0 && m.getPoints() < bestPoints)) {
                        bestMove = m;
                        bestPoints = m.getPoints();
                    }
                }

                board.undoLastMove();

                if (beta <= alpha && bestMove != null) {
                    return bestMove;
                }
            }
        }

        return bestMove;
    }
}
