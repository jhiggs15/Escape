package escape.board;

import escape.EscapeGameBuilder;
import escape.piece.EscapeGamePiece;
import escape.piece.MoveManager;
import escape.required.EscapePiece;
import escape.required.Player;
import escape.util.PieceAttribute;
import escape.util.PieceTypeDescriptor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.*;

public class BoardTest
{

    @Test
    void test1() throws Exception {
        EscapeGameBuilder egb = new EscapeGameBuilder("Escape/config/egc/test1.egc");
        Board board = new Board(egb.getGameInitializer());
        ArrayList<EscapeGamePiece> pieces = new ArrayList<>();
        pieces.add(makePiece(
                Player.PLAYER1,
                EscapePiece.PieceName.SNAIL,
                EscapePiece.MovementPattern.OMNI,
                new PieceAttribute[]{
                        new PieceAttribute(
                                EscapePiece.PieceAttributeID.DISTANCE, 1)},
                board));

        pieces.add(makePiece(
                Player.PLAYER2,
                EscapePiece.PieceName.HORSE,
                EscapePiece.MovementPattern.OMNI,
                new PieceAttribute[]{
                        new PieceAttribute(
                                EscapePiece.PieceAttributeID.DISTANCE, 7),
                        new PieceAttribute(
                                EscapePiece.PieceAttributeID.JUMP)},
                board));


        assertFalse(board.getPiecesOnTheBoard().isEmpty());
        boolean result = true;
        for ( EscapeGamePiece piece : board.getPiecesOnTheBoard() )
        {
            assertTrue(pieces.contains(piece));
        }


    }

    @Test
    void boardWithNoPieces() throws Exception {
        EscapeGameBuilder egb = new EscapeGameBuilder("Escape/config/egc/NoPieces.egc");
        Board board = new Board(egb.getGameInitializer());

        assertTrue(board.getPiecesOnTheBoard().isEmpty());



    }


    public static EscapeGamePiece makePiece(Player player, EscapePiece.PieceName pieceName, EscapePiece.MovementPattern movementPattern, PieceAttribute[] attributes, Board board){
        return new EscapeGamePiece(player,
                new PieceTypeDescriptor(
                        pieceName,
                        movementPattern,
                        attributes),
                new MoveManager(movementPattern, board));
    }
}
