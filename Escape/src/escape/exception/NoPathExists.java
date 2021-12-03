package escape.exception;

import escape.board.EscapeCoordinate;
import escape.required.Coordinate;

public class NoPathExists extends EscapeException
{

    public NoPathExists(EscapeCoordinate from, EscapeCoordinate to)
    {
        super("No path could be found between " + from.toString() + " and " + to.toString() + ".");
    }

    public NoPathExists(EscapeCoordinate from, EscapeCoordinate to, int pathLength, int pieceValue)
    {
        super("No path could be found between " + from.toString() + " and " + to.toString() +
                " because the piece has a value of " + pieceValue + " and the length of the path is " + pathLength);
    }
}
