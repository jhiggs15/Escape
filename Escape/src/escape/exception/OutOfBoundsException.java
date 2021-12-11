package escape.exception;

import escape.board.EscapeCoordinate;

public class OutOfBoundsException extends EscapeException
{
    public OutOfBoundsException(EscapeCoordinate coordinate) {
        super(createString(coordinate));
    }

    public static String createString(EscapeCoordinate coordinate)
    {
        return "The coordinate " + coordinate.toString() + " is out of bounds please select a coordinate in bounds.";
    }
}
