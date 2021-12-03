package escape.exception;

import escape.board.EscapeCoordinate;

public class OutOfBoundsException extends EscapeException
{
    public OutOfBoundsException(EscapeCoordinate coordinate) {
        super("The coordinate " + coordinate.toString() + " is out of bounds please select a coordinate in bounds.");
    }
}
