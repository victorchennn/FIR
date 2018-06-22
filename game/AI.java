package game;

import static game.PieceColor.*;

/**
 * An AI player, can automatically compute the best piece in
 * the current board.
 * @author Victor
 */
public class AI extends Player {

    /** A new AI for GAME that will play MYCOLOR. */
    AI(Game game, PieceColor myColor) {
        super(game, myColor);
    }

    @Override
    Piece next() {
        Board b = new Board(board());
        if (myColor() == WHITE) {
            findPiece(b, MAX_DEPTH, -INFINITY, INFINITY, true);
        } else {
            findPiece(b, MAX_DEPTH, -INFINITY, INFINITY, false);
        }
        return _lastStep;
    }


    private int findPiece(Board board, int depth, int alpha, int beta, boolean MaxmizingPlayer) {
        if (board.gameOver()) {
            return board.whoseMove() == WHITE ? WINNING_VALUE : -WINNING_VALUE;
        }
        if (depth == 0) {
            return score(board);
        }
        if (MaxmizingPlayer) {
            int v = -INFINITY;
            int response;
            for (Piece p : board.getPotentialPieces(true)) {
                Board temp = new Board(board);
                temp.play(p);
                response = findPiece(temp, depth - 1, alpha, beta, false);
                if (response > v) {
                    v = response;
                    _lastStep = p;
                }
                alpha = Math.max(alpha, v);
                if (beta <= alpha) {
                    break;
                }
            }
            return v;
        } else {
            int v = INFINITY;
            int response;
            for (Piece p : board.getPotentialPieces(true)) {
                Board temp = new Board(board);
                temp.play(p);
                response = findPiece(temp, depth - 1, alpha, beta, true);
                if (response < v) {
                    v = response;
                    _lastStep = p;
                }
                beta = Math.min(beta, v);
                if (beta <= alpha) {
                    break;
                }
            }
            return v;
        }
    }

    /**
     * Used to count heuristic score on the board to determine which step
     * is best.
     */
    private int score(Board board) {
        int me = 0, op = 0;
        int[] my_score = board.chainOfPieces(board.whoseMove());
        int[] op_score = board.chainOfPieces(board.whoseMove().opposite());
        for (int i = 1; i <= 5; i++) {
            me += my_score[i - 1] * i;
            op += op_score[i - 1] * i;
        }
        return me - op;
    }

    /** Maximum minimax search depth before going to static evaluation. */
    private static final int MAX_DEPTH = 1;

    /** A magnitude greater than a normal value. */
    private static final int INFINITY = Integer.MAX_VALUE;

    /** A position magnitude indicating a win (for white if positive, black
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;

    /** The piece found by the last call to findMove method. */
    private Piece _lastStep;
}
