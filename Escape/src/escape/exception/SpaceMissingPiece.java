package escape.exception;

import escape.board.EscapeCoordinate;
import escape.required.Player;

public class SpaceMissingPiece extends EscapeException
{
    public SpaceMissingPiece(Player player, EscapeCoordinate coordinate)
    {
        super("There is no piece that " + player + " owns at coordinate " + coordinate.toString() );
    }

}
