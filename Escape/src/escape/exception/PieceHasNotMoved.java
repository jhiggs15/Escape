package escape.exception;

import escape.board.EscapeCoordinate;

public class PieceHasNotMoved extends EscapeException
{

    public PieceHasNotMoved(EscapeCoordinate coordinate)
    {
        super(createString(coordinate));

    }

    public static String createString(EscapeCoordinate coordinate)
    {
        return "Moves of length 0 are not permitted";
    }
}
