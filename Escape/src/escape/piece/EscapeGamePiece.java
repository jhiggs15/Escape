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

    public EscapeGamePiece(PieceTypeDescriptor descriptor, Board board) {
        this.owner = null;
        this.descriptor = descriptor;
    }

    public EscapeGamePiece(Player owner, PieceTypeDescriptor descriptor)
    {
        this.owner = owner;
        this.descriptor = descriptor;
    }

    public boolean hasAttribute(PieceAttributeID attributeID)
    {
        return descriptor.getAttribute(attributeID) != null;
    }

    public MovementPattern getMovementPattern()
    {
        return descriptor.getMovementPattern();
    }

    public int getMovementValue()
    {
        return descriptor.getMovementValue();
    }

    public int getValue()
    {
        return descriptor.getValue();
    }

    public EscapeGamePiece makeCopy(Player owner)
    {
        return new EscapeGamePiece(owner, descriptor);
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
                this.descriptor.equals(otherGamePiece.descriptor);
    }
}
