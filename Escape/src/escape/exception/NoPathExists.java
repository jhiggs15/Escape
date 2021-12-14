package escape.exception;

import escape.board.EscapeCoordinate;
import escape.required.Coordinate;

public class NoPathExists extends EscapeException
{

    public NoPathExists(EscapeCoordinate from, EscapeCoordinate to)
    {
        super(createString(from, to));
    }

    public NoPathExists(EscapeCoordinate from, EscapeCoordinate to, boolean isBlocked)
    {
        super(createString(from, to, isBlocked));
    }

    public NoPathExists(EscapeCoordinate from, EscapeCoordinate to, int pathLength, int pieceValue)
    {
        super(createString(from, to, pathLength, pieceValue));
    }

    public static String createString(EscapeCoordinate from, EscapeCoordinate to, int pathLength, int pieceValue)
    {
        return "No path could be found between " + from.toString() + " and " + to.toString() +
                " because the piece has a value of " + pieceValue + " and the length of the path is " + pathLength;
    }

    public static String createString(EscapeCoordinate from, EscapeCoordinate to)
    {
        return "No path could be found between " + from.toString() + " and " + to.toString() + ".";
    }

    public static String createString(EscapeCoordinate from, EscapeCoordinate to, boolean isBlocked)
    {
        return "No path could be found between " + from.toString() + " and " + to.toString() + " because the destination is blocked by a piece or block space.";
    }
}

