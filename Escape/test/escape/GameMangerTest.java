package escape;

import escape.board.EscapeCoordinate;
import escape.exception.EscapeException;
import escape.piece.EscapeGamePiece;
import escape.required.EscapePiece;
import escape.required.GameObserver;
import escape.required.Player;
import escape.util.PieceAttribute;
import escape.util.PieceTypeDescriptor;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class GameMangerTest
{

    public EscapeGameManager<EscapeCoordinate> makeGameManager(String filename) throws Exception {
        EscapeGameBuilder egb = new EscapeGameBuilder(filename);
        EscapeGameManager<EscapeCoordinate> gm = egb.makeGameManager();
        return gm;
    }

    public static EscapeGamePiece makePiece(Player player, EscapePiece.PieceName pieceName, EscapePiece.MovementPattern movementPattern, PieceAttribute[] attributes){
        return new EscapeGamePiece(player,
                new PieceTypeDescriptor(
                        pieceName,
                        movementPattern,
                        attributes));
    }

    @Test
    void move_triesToMoveOutOfBounds() throws Exception
    {
        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/NoNeighbors.egc");

        assertFalse(gm.move(gm.makeCoordinate(2, 2), gm.makeCoordinate(1, 1)));
        GameManager myGm = (GameManager) gm;
        assertEquals(myGm.whoseTurn(), Player.PLAYER1);
        assertEquals(((GameManager) gm).getPlayerScore(Player.PLAYER1), 0);

        assertFalse(gm.move(gm.makeCoordinate(1, 1), gm.makeCoordinate(2, 2)));
        assertEquals(myGm.whoseTurn(), Player.PLAYER1);
        assertEquals(((GameManager) gm).getPlayerScore(Player.PLAYER1), 0);
    }

    @Test
    void move_noPieceAtStart() throws Exception
    {
        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/NoPiecesInfinite.egc");

        assertFalse(gm.move(gm.makeCoordinate(-9, 8), gm.makeCoordinate(1, 1)));
        GameManager myGm = (GameManager) gm;
        assertEquals(myGm.whoseTurn(), Player.PLAYER1);
        assertEquals(((GameManager) gm).getPlayerScore(Player.PLAYER1), 0);

    }

    @Test
    void move_noneOfAPlayersPiecesAtLocation() throws Exception
    {
        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/PlayersHaveSamePiece.egc");

        assertFalse(gm.move(gm.makeCoordinate(10, 12), gm.makeCoordinate(1, 1)));
        GameManager myGm = (GameManager) gm;
        assertEquals(myGm.whoseTurn(), Player.PLAYER1);
        assertEquals(((GameManager) gm).getPlayerScore(Player.PLAYER1), 0);
    }

    @Test
    void move_pieceCannotTravelFarEnough() throws Exception
    {
        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/PlayersHaveSamePiece.egc");

        assertFalse(gm.move(gm.makeCoordinate(10, 12), gm.makeCoordinate(16, 12)));
        GameManager myGm = (GameManager) gm;
        assertEquals(myGm.whoseTurn(), Player.PLAYER1);
        assertEquals(((GameManager) gm).getPlayerScore(Player.PLAYER1), 0);
    }

    @Test
    void move_pieceCanMove() throws Exception
    {
        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/PlayersHaveSamePiece.egc");

        assertTrue(gm.move(gm.makeCoordinate(4, 4), gm.makeCoordinate(6, 9)));

        EscapePiece gamePiece = makePiece(
                Player.PLAYER1,
                EscapePiece.PieceName.DOG,
                EscapePiece.MovementPattern.OMNI,
                new PieceAttribute[]{
                        new PieceAttribute(
                                EscapePiece.PieceAttributeID.DISTANCE, 5)});

        assertNotNull(gm.getPieceAt(gm.makeCoordinate(6, 9)));

        GameManager myGm = (GameManager) gm;

        assertEquals(myGm.getPieceAt(gm.makeCoordinate(6, 9)), gamePiece);

        assertEquals(myGm.whoseTurn(), Player.PLAYER2);
        assertEquals(((GameManager) gm).getPlayerScore(Player.PLAYER1), 0);
    }

    @Test
    void move_noPathCouldBeFound() throws Exception
    {
        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/ForcedPath.egc");
        assertFalse(gm.move(new EscapeCoordinate(-1, -1), new EscapeCoordinate(1, -3)));

        GameManager myGm = (GameManager) gm;

        assertEquals(myGm.whoseTurn(), Player.PLAYER1);
        assertEquals(((GameManager) gm).getPlayerScore(Player.PLAYER1), 0);
    }

    @Test
    void move_shortestPathIsLongerThanPiecesValue() throws Exception
    {
        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/ForcedPath.egc");
        assertFalse(gm.move(new EscapeCoordinate(2, 0), new EscapeCoordinate(2, 4)));

        GameManager myGm = (GameManager) gm;

        assertEquals(myGm.whoseTurn(), Player.PLAYER1);
        assertEquals(((GameManager) gm).getPlayerScore(Player.PLAYER1), 0);
    }

    @Test
    void move_multipleConsecutiveMoves() throws Exception
    {
        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/test1.egc");
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(4, 4), new EscapeCoordinate(5, 5)));
        assertEquals(myGm.whoseTurn(), Player.PLAYER2);
        assertTrue(gm.move(new EscapeCoordinate(10, 12), new EscapeCoordinate(5, 8)));
        assertEquals(myGm.whoseTurn(), Player.PLAYER1);
        assertTrue(gm.move(new EscapeCoordinate(5, 5), new EscapeCoordinate(6, 6)));
        assertEquals(myGm.whoseTurn(), Player.PLAYER2);

        EscapeGamePiece snail = makePiece(
                Player.PLAYER1,
                EscapePiece.PieceName.SNAIL,
                EscapePiece.MovementPattern.OMNI,
                new PieceAttribute[]{
                        new PieceAttribute(
                                EscapePiece.PieceAttributeID.DISTANCE, 1)});

        EscapeGamePiece horse = makePiece(
                Player.PLAYER2,
                EscapePiece.PieceName.HORSE,
                EscapePiece.MovementPattern.OMNI,
                new PieceAttribute[]{
                        new PieceAttribute(
                                EscapePiece.PieceAttributeID.DISTANCE, 7),
                        new PieceAttribute(EscapePiece.PieceAttributeID.JUMP)}
        );

        assertNull(gm.getPieceAt( new EscapeCoordinate(5, 5)));
        assertNull(gm.getPieceAt( new EscapeCoordinate(4, 4)));
        assertNull(gm.getPieceAt( new EscapeCoordinate(10, 12)));

        assertEquals(myGm.getPieceAt(new EscapeCoordinate(5, 8)), horse);
        assertEquals(myGm.getPieceAt(new EscapeCoordinate(6, 6)), snail);
    }

    @Test
    void move_pieceReachesTheEndAndScores() throws Exception
    {
        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/test1.egc");
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(4, 4), new EscapeCoordinate(5, 5)));
        assertEquals(myGm.whoseTurn(), Player.PLAYER2);
        assertTrue(gm.move(new EscapeCoordinate(10, 12), new EscapeCoordinate(5, 12)));

        EscapeGamePiece horse = makePiece(
                Player.PLAYER2,
                EscapePiece.PieceName.HORSE,
                EscapePiece.MovementPattern.OMNI,
                new PieceAttribute[]{
                        new PieceAttribute(
                                EscapePiece.PieceAttributeID.DISTANCE, 7),
                        new PieceAttribute(EscapePiece.PieceAttributeID.JUMP)}
        );

        assertNull(gm.getPieceAt( new EscapeCoordinate(5, 12)));

        assertEquals(myGm.whoseTurn(), Player.PLAYER1);
        assertEquals(myGm.getPlayerScore(Player.PLAYER1), 0);
        assertEquals(myGm.getPlayerScore(Player.PLAYER2), horse.getValue());
    }

    @Test
    void move_turnLimitReachedTie() throws Exception
    {
        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/TurnLimit.egc");
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(1, 1), new EscapeCoordinate(2, 2)));
        assertTrue(gm.move(new EscapeCoordinate(1, 0), new EscapeCoordinate(2, 1)));

        assertTrue(gm.move(new EscapeCoordinate(2, 2), new EscapeCoordinate(3, 3)));
        assertTrue(gm.move(new EscapeCoordinate(2, 1), new EscapeCoordinate(3, 2)));

        assertTrue(gm.move(new EscapeCoordinate(3, 3), new EscapeCoordinate(4, 4)));

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        assertTrue(gm.move(new EscapeCoordinate(3, 2), new EscapeCoordinate(4, 3)));

        assertEquals("Game Over", outContent.toString());
        System.setOut(originalOut);
    }

    @Test
    void move_PlayerMovesAfterGameIsOver() throws Exception
    {
        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/TurnLimit.egc");
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(1, 1), new EscapeCoordinate(2, 2)));
        assertTrue(gm.move(new EscapeCoordinate(1, 0), new EscapeCoordinate(2, 1)));

        assertTrue(gm.move(new EscapeCoordinate(2, 2), new EscapeCoordinate(3, 3)));
        assertTrue(gm.move(new EscapeCoordinate(2, 1), new EscapeCoordinate(3, 2)));

        assertTrue(gm.move(new EscapeCoordinate(3, 3), new EscapeCoordinate(4, 4)));

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        assertTrue(gm.move(new EscapeCoordinate(3, 2), new EscapeCoordinate(4, 3)));

        assertEquals("Game Over", outContent.toString());
        System.setOut(originalOut);

        assertFalse(gm.move(new EscapeCoordinate(3, 2), new EscapeCoordinate(4, 3)));

    }

    @Test
    void move_turnLimitReachedPlayer1Wins() throws Exception
    {
        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/TurnLimit.egc");
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(1, 1), new EscapeCoordinate(2, 2)));
        assertTrue(gm.move(new EscapeCoordinate(1, 0), new EscapeCoordinate(2, 1)));

        assertTrue(gm.move(new EscapeCoordinate(2, 2), new EscapeCoordinate(5, 5)));
        assertTrue(gm.move(new EscapeCoordinate(2, 1), new EscapeCoordinate(3, 2)));

        assertTrue(gm.move(new EscapeCoordinate(1, -1), new EscapeCoordinate(2, 0)));

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        assertTrue(gm.move(new EscapeCoordinate(3, 2), new EscapeCoordinate(4, 3)));

        assertEquals("PLAYER 1 wins", outContent.toString());
        System.setOut(originalOut);
    }

    @Test
    void move_turnLimitReachedPlayer2Wins() throws Exception
    {
        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/TurnLimit.egc");
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(1, 1), new EscapeCoordinate(2, 2)));
        assertTrue(gm.move(new EscapeCoordinate(1, 0), new EscapeCoordinate(2, 1)));

        assertTrue(gm.move(new EscapeCoordinate(2, 2), new EscapeCoordinate(3, 3)));
        assertTrue(gm.move(new EscapeCoordinate(2, 1), new EscapeCoordinate(3, 2)));

        assertTrue(gm.move(new EscapeCoordinate(1, -1), new EscapeCoordinate(2, 0)));

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        assertTrue(gm.move(new EscapeCoordinate(3, 2), new EscapeCoordinate(5, 5)));

        assertEquals("PLAYER 2 wins", outContent.toString());
        System.setOut(originalOut);
    }

    @Test
    void move_scoreLimitReachedPlayer1Wins() throws Exception
    {
        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/ScoreLimit.egc");
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(1, 1), new EscapeCoordinate(2, 2)));
        assertTrue(gm.move(new EscapeCoordinate(1, 0), new EscapeCoordinate(2, 1)));

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        assertTrue(gm.move(new EscapeCoordinate(2, 2), new EscapeCoordinate(5, 5)));

        assertEquals("PLAYER 1 wins", outContent.toString());
        System.setOut(originalOut);

    }

    @Test
    void move_scoreLimitReachedPlayer2Wins() throws Exception
    {
        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/ScoreLimit.egc");
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(1, 1), new EscapeCoordinate(2, 2)));
        assertTrue(gm.move(new EscapeCoordinate(1, 0), new EscapeCoordinate(2, 1)));

        assertTrue(gm.move(new EscapeCoordinate(2, 2), new EscapeCoordinate(3, 3)));
        assertTrue(gm.move(new EscapeCoordinate(2, 1), new EscapeCoordinate(3, 2)));

        assertTrue(gm.move(new EscapeCoordinate(1, -1), new EscapeCoordinate(2, 0)));

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        assertTrue(gm.move(new EscapeCoordinate(3, 2), new EscapeCoordinate(5, 5)));

        assertEquals("PLAYER 2 wins", outContent.toString());
        System.setOut(originalOut);
    }

    @Test
    void move_scoreLimitAndTurnLimitScoreLimitReached() throws Exception
    {
        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/ScoreLimitAndTurnLimit.egc");
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(1, 1), new EscapeCoordinate(5, 5)));
        assertTrue(gm.move(new EscapeCoordinate(1, 0), new EscapeCoordinate(2, 1)));

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        assertTrue(gm.move(new EscapeCoordinate(1, -1), new EscapeCoordinate(5, 5)));

        assertEquals("PLAYER 1 wins", outContent.toString());
        System.setOut(originalOut);

    }


    @Test
    void move_scoreLimitAndTurnLimitReachedTurnLimitReached() throws Exception
    {
        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/ScoreLimitAndTurnLimit.egc");
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(1, 1), new EscapeCoordinate(2, 2)));
        assertTrue(gm.move(new EscapeCoordinate(1, 0), new EscapeCoordinate(2, 1)));

        assertTrue(gm.move(new EscapeCoordinate(2, 2), new EscapeCoordinate(3, 3)));
        assertTrue(gm.move(new EscapeCoordinate(2, 1), new EscapeCoordinate(3, 2)));

        assertTrue(gm.move(new EscapeCoordinate(1, -1), new EscapeCoordinate(2, 0)));

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        assertTrue(gm.move(new EscapeCoordinate(3, 2), new EscapeCoordinate(5, 5)));

        assertEquals("PLAYER 2 wins", outContent.toString());
        System.setOut(originalOut);

    }

    @Test
    void move_scoreLimitAndTurnLimitReachedBothReached() throws Exception
    {
        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/ScoreLimitAndTurnLimit.egc");
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(1, 1), new EscapeCoordinate(2, 2)));
        assertTrue(gm.move(new EscapeCoordinate(1, 0), new EscapeCoordinate(5, 5)));

        assertTrue(gm.move(new EscapeCoordinate(2, 2), new EscapeCoordinate(3, 3)));
        assertTrue(gm.move(new EscapeCoordinate(1, -2), new EscapeCoordinate(3, 2)));

        assertTrue(gm.move(new EscapeCoordinate(1, -1), new EscapeCoordinate(2, 0)));

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        assertTrue(gm.move(new EscapeCoordinate(3, 2), new EscapeCoordinate(5, 5)));

        assertEquals("PLAYER 2 wins", outContent.toString());
        System.setOut(originalOut);

    }


    // ---- observer tests

    @Test
    void observerTest() throws Exception
    {
        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/ScoreLimitAndTurnLimit.egc");
        GameObserver gmob = new EscapeGameObsever();
        assertThrows(EscapeException.class,() -> gmob.notify("hello world"));
        assertThrows(EscapeException.class,() -> gmob.notify("hello world", new EscapeException("hello", new Throwable())));
        assertThrows(EscapeException.class, () -> gm.addObserver(new EscapeGameObsever()));
        assertThrows(EscapeException.class, () -> gm.removeObserver(new EscapeGameObsever()));
    }






}
