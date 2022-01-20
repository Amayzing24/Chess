package org.cis120.chess;

import java.util.*;

/**
 * ChessBoard is the model of the game
 */
public class ChessBoard {
    private Piece[][] board;
    private List<Piece> whitePieces;
    private List<Piece> blackPieces;
    private Set<Position> blockPositions;
    private Map<Position, Set<Position>> blockerMap;
    private List<Piece> checkers;
    private int turnSide;
    private King whiteKing;
    private King blackKing;

    /**
     * Creates a new ChessBoard object, putting pieces at a regular chess game's
     * starting positions
     */
    public ChessBoard() {
        board = new Piece[8][8];
        whitePieces = new LinkedList<>();
        blackPieces = new LinkedList<>();
        blockerMap = new HashMap<>();
        blockPositions = new HashSet<>();
        checkers = new LinkedList<>();

        blackKing = new King(new Position(0, 4), -1);
        board[0][4] = blackKing;
        board[0][0] = new Rook(new Position(0, 0), -1);
        board[0][7] = new Rook(new Position(0, 7), -1);
        board[0][1] = new Knight(new Position(0, 1), -1);
        board[0][6] = new Knight(new Position(0, 6), -1);
        board[0][2] = new Bishop(new Position(0, 2), -1);
        board[0][5] = new Bishop(new Position(0, 5), -1);
        board[0][3] = new Queen(new Position(0, 3), -1);
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(new Position(1, i), -1);
            blackPieces.add(board[1][i]);
            blackPieces.add(board[0][i]);
        }

        whiteKing = new King(new Position(7, 4), 1);
        board[7][4] = whiteKing;
        board[7][0] = new Rook(new Position(7, 0), 1);
        board[7][7] = new Rook(new Position(7, 7), 1);
        board[7][1] = new Knight(new Position(7, 1), 1);
        board[7][6] = new Knight(new Position(7, 6), 1);
        board[7][2] = new Bishop(new Position(7, 2), 1);
        board[7][5] = new Bishop(new Position(7, 5), 1);
        board[7][3] = new Queen(new Position(7, 3), 1);
        for (int i = 0; i < 8; i++) {
            board[6][i] = new Pawn(new Position(6, i), 1);
            whitePieces.add(board[6][i]);
            whitePieces.add(board[7][i]);
        }

        updateCheckStatus(1);
        turnSide = 1;
    }

    public Piece getPiece(Position p) {
        if (outOfBounds(p)) {
            throw new IllegalArgumentException();
        }
        return board[p.getRow()][p.getCol()];
    }

    public static boolean outOfBounds(Position p) {
        return p.getRow() < 0 || p.getRow() >= 8 || p.getCol() < 0 || p.getCol() >= 8;
    }

    /**
     * Makes a move and updates the board's state accordingly
     * 
     * @param m the Move object being made
     * @return 1 if there's a check, 2 if there's a checkmate, 3 if there's a
     *         stalemate, 0 otherwise
     */
    public int makeMove(Move m) {
        // First do housekeeping - update Pawns movedTwo status and reset check booleans
        for (Piece p : whitePieces) {
            if (p.getPieceID() == 1) {
                Pawn pawn = (Pawn) p;
                pawn.deactivateMovedTwo();
            }
        }
        for (Piece p : blackPieces) {
            if (p.getPieceID() == 1) {
                Pawn pawn = (Pawn) p;
                pawn.deactivateMovedTwo();
            }
        }
        whiteKing.notInCheck();
        blackKing.notInCheck();

        // Eliminate the piece if that is involved
        if (m.hasElim()) {
            Piece elim = m.getElim();
            whitePieces.remove(elim);
            blackPieces.remove(elim);
            board[elim.getPos().getRow()][elim.getPos().getCol()] = null;
        }
        board[m.getPiece().getPos().getRow()][m.getPiece().getPos().getCol()] = null;

        // Checking if promotion move. If so, removes the old pawn and updates it with
        // the new piece.
        // Also checking if castle move. If so, moves the rook as well.
        if (m.isPromotion()) {
            board[m.getNewPos().getRow()][m.getNewPos().getCol()] = m.getPromotion();
            if (m.getPiece().getSide() == 1) {
                whitePieces.remove(m.getPiece());
                whitePieces.add(m.getPromotion());
            } else {
                blackPieces.remove(m.getPiece());
                blackPieces.add(m.getPromotion());
            }
        } else if (m.isCastle()) {
            if (m.getNewPos().getCol() == 6) {
                Piece rook = board[m.getPiece().getPos().getRow()][7];
                board[m.getPiece().getPos().getRow()][7] = null;
                board[m.getPiece().getPos().getRow()][5] = rook;
                rook.move(new Position(m.getPiece().getPos().getRow(), 5));
            } else {
                Piece rook = board[m.getPiece().getPos().getRow()][0];
                board[m.getPiece().getPos().getRow()][0] = null;
                board[m.getPiece().getPos().getRow()][3] = rook;
                rook.move(new Position(m.getPiece().getPos().getRow(), 3));
            }
            board[m.getNewPos().getRow()][m.getNewPos().getCol()] = m.getPiece();
            m.getPiece().move(m.getNewPos());
        } else {
            board[m.getNewPos().getRow()][m.getNewPos().getCol()] = m.getPiece();
            m.getPiece().move(m.getNewPos());
        }

        updateCheckStatus(turnSide);

        List<Piece> currPieces;
        if (turnSide == 1) {
            currPieces = blackPieces;
        } else {
            currPieces = whitePieces;
        }

        Set<Position> validMoves = new HashSet<>();
        for (Piece p : currPieces) {
            validMoves.addAll(p.getPossibleMoves(this).keySet());
        }

        turnSide = turnSide * -1;

        // Check if there is a checkmate, stalemate, or check
        int code;
        if (sideInCheck(turnSide) && validMoves.isEmpty()) {
            code = 2;
            turnSide = turnSide * -1;
        } else if (sideInCheck(turnSide)) {
            code = 1;
        } else if (validMoves.isEmpty()) {
            code = 3;
        } else {
            code = 0;
        }

        return code;
    }

    /**
     * Gets all positions that can block a current check
     * @return a set of positions that block a current check
     */
    public Set<Position> getBlockCheckPositions() {
        return blockPositions;
    }

    /**
     * Gets all positions that will maintain a block for a Piece p
     * @param p
     * @return a set of positions that will maintain a block for a blocking piece,
     * null if the piece is not a blocker
     */
    public Set<Position> getBlockMaintainPositions(Piece p) {
        return blockerMap.get(p.getPos());
    }

    /**
     * Updates the various collection fields after a move has been made to keep track of
     * checking, blocking, etc.
     * @param side
     */
    private void updateCheckStatus(int side) {
        List<Piece> lst;
        King currKing;
        blockerMap = new HashMap<>();
        blockPositions = new HashSet<>();
        checkers = new LinkedList<>();

        if (side == 1) {
            lst = whitePieces;
            currKing = blackKing;
        } else {
            lst = blackPieces;
            currKing = whiteKing;
        }

        for (Piece p : lst) {
            List<Integer> status = p.checkStatus(this);
            Iterator<Integer> iter = status.iterator();
            int fst = iter.next();
            if (fst == 2) {
                Set<Position> bSet = new HashSet<>();
                Position bPos = new Position(iter.next(), iter.next());
                while (iter.hasNext()) {
                    bSet.add(new Position(iter.next(), iter.next()));
                }
                blockerMap.put(bPos, bSet);
            } else if (fst == 1) {
                checkers.add(p);
                while (iter.hasNext()) {
                    blockPositions.add(new Position(iter.next(), iter.next()));
                }
                blockPositions.add(p.getPos());
            }
        }

        if (checkers.size() > 0) {
            currKing.putInCheck();
        }
        if (checkers.size() > 1) {
            blockPositions = new HashSet<>();
        }
    }

    /**
     * Gets all positions threatened by a side. These are positions that a piece on
     * the side can move to in the event the king moves there.
     * @param side the side being looked at
     * @return a set of all positions threatened
     */
    public Set<Position> getAllSquaresThreatened(int side) {
        Set<Position> moves = new HashSet<>();
        List<Piece> lst;

        if (side == 1) {
            lst = whitePieces;
        } else {
            lst = blackPieces;
        }

        for (Piece p : lst) {
            moves.addAll(p.getSquaresThreatened(this));
        }

        return moves;
    }

    /**
     * Checks if the given side is in check by looking at that side's king
     * @param side the side being looked at
     * @return true if the provided side's king is in check, false otherwise
     */
    public boolean sideInCheck(int side) {
        if (side == 1) {
            return whiteKing.isInCheck();
        } else {
            return blackKing.isInCheck();
        }
    }

    public Position getKingPos(int side) {
        if (side == 1) {
            return whiteKing.getPos();
        } else {
            return blackKing.getPos();
        }
    }

    public int getTurnSide() {
        return turnSide;
    }

    public String getTurnColor() {
        if (turnSide == 1) {
            return "White";
        } else {
            return "Black";
        }
    }
}
