package escape.exception;

import escape.EscapeGameBuilder;
import escape.board.Board;
import escape.piece.EscapeGamePiece;
import escape.required.EscapePiece;
import escape.required.Player;
import escape.util.PieceAttribute;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExceptionTest
{
    @Test
    void getPieceThatExists() throws Exception {
        assertThrows(EscapeException.class, () -> fun());
    }

    public void fun()
    {
        throw new EscapeException("Peace of Mind", new Throwable());
    }
}
