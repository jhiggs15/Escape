package escape.piece;

import escape.board.Board;
import escape.board.EscapeCoordinate;
import escape.exception.EscapeException;
import escape.movement.MoveManager;
import escape.required.EscapePiece;
import escape.required.Player;
import escape.util.PieceTypeDescriptor;

public class EscapeGamePiece implements EscapePiece
{
    private Player owner;
    private PieceTypeDescriptor descriptor;
    private MoveManager moveManager;

    public EscapeGamePiece(PieceTypeDescriptor descriptor, Board board) {
        this.owner = null;
        this.descriptor = descriptor;
        this.moveManager = new MoveManager(descriptor.getMovementPattern(), board);
    }

    public EscapeGamePiece(Player owner, PieceTypeDescriptor descriptor, MoveManager moveManager)
    {
        this.owner = owner;
        this.descriptor = descriptor;
        this.moveManager = moveManager;
    }

    public boolean canMove(EscapeCoordinate from, EscapeCoordinate to)
    {
        return moveManager.canMove(from, to, descriptor.getMovementValue());
    }

    public EscapeGamePiece makeCopy(Player owner)
    {
        return new EscapeGamePiece(owner, descriptor, moveManager);
    }

    public Player getPlayer()
    {
        return owner;
    }

    @Override
    public boolean equals(Object otherPiece) {
        if(otherPiece.getClass() != this.getClass()) return false;
        EscapeGamePiece otherGamePiece = (EscapeGamePiece) otherPiece;

        return this.owner == otherGamePiece.getPlayer() &&
                this.descriptor.equals(otherGamePiece.descriptor) &&
                this.moveManager.equals(otherGamePiece.moveManager);
    }
}
