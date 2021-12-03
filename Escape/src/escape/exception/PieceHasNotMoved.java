package escape.exception;

import escape.board.EscapeCoordinate;

public class PieceHasNotMoved extends EscapeException
{

    public PieceHasNotMoved(EscapeCoordinate coordinate)
    {
        super("Moves of length 0 are not permitted");

    }
}
