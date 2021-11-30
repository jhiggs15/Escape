package escape.piece;

import escape.board.Board;
import escape.required.EscapePiece;

public class MoveManager
{
    private Board board;

    public MoveManager(EscapePiece.MovementPattern movementPattern, Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object otherMoveManager) {
        if(otherMoveManager.getClass() != this.getClass()) return false;
        MoveManager castMoveManger = (MoveManager) otherMoveManager;
        return this.board.getBoardType().equals(castMoveManger.board.getBoardType());
    }
}
