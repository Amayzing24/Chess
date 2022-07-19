package org.cis120.chess;

import java.util.NoSuchElementException;

public class Move {
    private Piece piece;
    private Position pos;
    private Position origPos;
    private ChessBoard board;
    private Piece elim;
    private Piece promo;
    private boolean isCastle;
    private double points;

    public Move(Piece piece, Position pos, ChessBoard board) {
        this.piece = piece;
        this.pos = pos;
        this.board = board;
        elim = null;
        promo = null;
        updatePromotion(5);
        isCastle = false;
        origPos = piece.getPos();
        points = 0;
    }

    public Move(Piece piece, Position pos, ChessBoard board, Piece elim) {
        this.piece = piece;
        this.pos = pos;
        this.board = board;
        this.elim = elim;
        promo = null;
        updatePromotion(5);
        isCastle = false;
        origPos = piece.getPos();
    }

    public Piece getPiece() {
        return piece;
    }

    public Position getNewPos() {
        return pos;
    }

    public Position getOrigPos() {
        return origPos;
    }

    public ChessBoard getBoard() {
        return board;
    }

    public Piece getElim() {
        return elim;
    }

    public void changePiece(Piece p) {
        piece = p;
    }

    public boolean hasElim() {
        return elim != null;
    }

    public boolean isPromotion() {
        if (piece.getPieceID() != 1) {
            return false;
        }
        if (pos.getRow() == 7 || pos.getRow() == 0) {
            return true;
        }
        return false;
    }

    public void updatePromotion(int id) {
        switch (id) {
            case 1:
                promo = new Pawn(pos, piece.getSide());
                break;
            case 2:
                promo = new Rook(pos, piece.getSide());
                break;
            case 3:
                promo = new Bishop(pos, piece.getSide());
                break;
            case 4:
                promo = new Knight(pos, piece.getSide());
                break;
            case 5:
                promo = new Queen(pos, piece.getSide());
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public Piece getPromotion() {
        if (promo == null) {
            throw new NoSuchElementException();
        }
        return promo;
    }

    public void makeCastle() {
        isCastle = true;
    }

    public boolean isCastle() {
        return isCastle;
    }

    @Override
    public String toString() {
        String out = piece.toString() + " to " + pos.toChessString();
        if (elim != null) {
            out += " eliminating " + elim.toString().toLowerCase();
        }
        return out;
    }

    public String toFileString() {
        String out = piece.getPos().getRow() + " " + piece.getPos().getCol() + " " + pos.getRow()
                + " " + pos.getCol()
                + " ";
        if (elim != null) {
            out += elim.getPos().getRow() + " " + elim.getPos().getCol();
        } else {
            out += -1 + " " + -1;
        }
        if (isCastle()) {
            out += " " + 1;
        } else {
            out += " " + -1;
        }

        return out;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public double getPoints() {
        return points;
    }
}
