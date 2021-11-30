package escape.piece;

import escape.board.Board;
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

    public EscapeGamePiece makeCopy(Player owner)
    {
        return new EscapeGamePiece(owner, descriptor, moveManager);
    }

    public Player getOwner() {
        return owner;
    }

    public PieceTypeDescriptor getDescriptor() {
        return descriptor;
    }

    public MoveManager getMoveManager() {
        return moveManager;
    }

    @Override
    public boolean equals(Object otherPiece) {
        if(otherPiece.getClass() != this.getClass()) return false;
        EscapeGamePiece otherGamePiece = (EscapeGamePiece) otherPiece;

        return this.owner == otherGamePiece.getOwner() &&
                this.descriptor.equals(otherGamePiece.descriptor) &&
                this.moveManager.equals(otherGamePiece.moveManager);
    }
}
