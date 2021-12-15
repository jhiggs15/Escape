package escape;

import escape.board.EscapeCoordinate;
import escape.exception.*;
import escape.gamemanagement.EscapeGameManager;
import escape.gamemanagement.EscapeGameObsever;
import escape.gamemanagement.GameManager;
import escape.board.EscapeGamePiece;
import escape.required.EscapePiece;
import escape.required.GameObserver;
import escape.required.Player;
import escape.util.PieceAttribute;
import escape.util.PieceTypeDescriptor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameMangerTest
{

    public EscapeGameManager<EscapeCoordinate> makeGameManager(String filename, GameObserver gameObserver ) throws Exception {
        EscapeGameBuilder egb = new EscapeGameBuilder(filename);
        EscapeGameManager<EscapeCoordinate> gm = egb.makeGameManager();
        gm.addObserver(gameObserver);
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
        EscapeGameObsever observer = new EscapeGameObsever();
        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/NoNeighbors.egc", observer);

        assertFalse(gm.move(gm.makeCoordinate(2, 2), gm.makeCoordinate(1, 1)));
        GameManager myGm = (GameManager) gm;

        assertEquals(observer.getMessage(), OutOfBounds.class.toString());
        assertEquals(observer.getError(), OutOfBounds.createString(gm.makeCoordinate(2, 2)));

        assertEquals(myGm.whoseTurn(), Player.PLAYER1);
        assertEquals(((GameManager) gm).getPlayerScore(Player.PLAYER1), 0);

        assertFalse(gm.move(gm.makeCoordinate(1, 1), gm.makeCoordinate(2, 2)));
        assertEquals(myGm.whoseTurn(), Player.PLAYER1);
        assertEquals(((GameManager) gm).getPlayerScore(Player.PLAYER1), 0);
    }

    @Test
    void move_noPieceAtStart() throws Exception
    {
        EscapeGameObsever observer = new EscapeGameObsever();
        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/NoPiecesInfinite.egc", observer);

        assertFalse(gm.move(gm.makeCoordinate(-9, 8), gm.makeCoordinate(1, 1)));

        assertEquals(observer.getMessage(), SpaceMissingPiece.class.toString());
        assertEquals(observer.getError(), SpaceMissingPiece.createString(Player.PLAYER1, gm.makeCoordinate(-9, 8)));

        GameManager myGm = (GameManager) gm;
        assertEquals(myGm.whoseTurn(), Player.PLAYER1);
        assertEquals(((GameManager) gm).getPlayerScore(Player.PLAYER1), 0);

    }

    @Test
    void move_noneOfAPlayersPiecesAtLocation() throws Exception
    {
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/PlayersHaveSamePiece.egc", observer);

        assertFalse(gm.move(gm.makeCoordinate(10, 12), gm.makeCoordinate(1, 1)));
        assertEquals(observer.getMessage(), SpaceMissingPiece.class.toString());
        assertEquals(observer.getError(), SpaceMissingPiece.createString(Player.PLAYER1, gm.makeCoordinate(10, 12)));

        GameManager myGm = (GameManager) gm;
        assertEquals(myGm.whoseTurn(), Player.PLAYER1);
        assertEquals(((GameManager) gm).getPlayerScore(Player.PLAYER1), 0);
    }

    @Test
    void move_pieceCannotTravelFarEnough() throws Exception
    {
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/PlayersHaveSamePiece.egc", observer);

        assertFalse(gm.move(gm.makeCoordinate(4, 4), gm.makeCoordinate(10, 4)));
        
        assertEquals(observer.getMessage(), MoveTooFar.class.toString());
        assertEquals(observer.getError(), MoveTooFar.createString(gm.makeCoordinate(4, 4), gm.makeCoordinate(10, 4)));

        GameManager myGm = (GameManager) gm;
        assertEquals(myGm.whoseTurn(), Player.PLAYER1);
        assertEquals(((GameManager) gm).getPlayerScore(Player.PLAYER1), 0);
    }

    @Test
    void move_pieceCanMove() throws Exception
    {
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/PlayersHaveSamePiece.egc", observer);

        assertTrue(gm.move(gm.makeCoordinate(4, 4), gm.makeCoordinate(6, 9)));

        assertEquals(observer.getMessage(), Player.PLAYER1 + " move was successful");
        assertNull(observer.getError());

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
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/ForcedPath.egc", observer);
        assertFalse(gm.move(new EscapeCoordinate(-1, -1), new EscapeCoordinate(1, -3)));

        assertEquals(observer.getMessage(), NoPathExists.class.toString());
        assertEquals(observer.getError(), NoPathExists.createString(gm.makeCoordinate(-1, -1), gm.makeCoordinate(1, -3)));

        GameManager myGm = (GameManager) gm;

        assertEquals(myGm.whoseTurn(), Player.PLAYER1);
        assertEquals(((GameManager) gm).getPlayerScore(Player.PLAYER1), 0);
    }

    @Test
    void move_shortestPathIsLongerThanPiecesValue() throws Exception
    {
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/ForcedPath.egc", observer);
        assertFalse(gm.move(new EscapeCoordinate(2, 0), new EscapeCoordinate(2, 4)));

        assertEquals(observer.getMessage(), MoveTooFar.class.toString());
        assertEquals(observer.getError(), MoveTooFar.createString(gm.makeCoordinate(2, 0), gm.makeCoordinate(2, 4)));

        GameManager myGm = (GameManager) gm;

        assertEquals(myGm.whoseTurn(), Player.PLAYER1);
        assertEquals(((GameManager) gm).getPlayerScore(Player.PLAYER1), 0);
    }

    @Test
    void move_multipleConsecutiveMoves() throws Exception
    {
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/test1.egc", observer);
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(4, 4), new EscapeCoordinate(5, 5)));
        assertEquals(myGm.whoseTurn(), Player.PLAYER2);

        assertEquals(observer.getMessage(), Player.PLAYER1 + " move was successful");
        assertNull(observer.getError());

        assertTrue(gm.move(new EscapeCoordinate(10, 12), new EscapeCoordinate(5, 8)));
        assertEquals(myGm.whoseTurn(), Player.PLAYER1);

        assertEquals(observer.getMessage(), Player.PLAYER2 + " move was successful");
        assertNull(observer.getError());

        assertTrue(gm.move(new EscapeCoordinate(5, 5), new EscapeCoordinate(6, 6)));
        assertEquals(myGm.whoseTurn(), Player.PLAYER2);

        assertEquals(observer.getMessage(), Player.PLAYER1 + " move was successful");
        assertNull(observer.getError());

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
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/test1.egc", observer);
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(4, 4), new EscapeCoordinate(5, 5)));
        assertEquals(myGm.whoseTurn(), Player.PLAYER2);

        assertEquals(observer.getMessage(), Player.PLAYER1 + " move was successful");
        assertNull(observer.getError());

        assertTrue(gm.move(new EscapeCoordinate(10, 12), new EscapeCoordinate(5, 12)));


        assertEquals(observer.getMessage(), Player.PLAYER2 + " move was successful");
        assertNull(observer.getError());

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
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/TurnLimit.egc", observer);
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(1, 1), new EscapeCoordinate(2, 2)));
        assertTrue(gm.move(new EscapeCoordinate(1, 0), new EscapeCoordinate(2, 1)));

        assertTrue(gm.move(new EscapeCoordinate(2, 2), new EscapeCoordinate(3, 3)));
        assertTrue(gm.move(new EscapeCoordinate(2, 1), new EscapeCoordinate(3, 2)));

        assertTrue(gm.move(new EscapeCoordinate(3, 3), new EscapeCoordinate(4, 4)));

        assertTrue(gm.move(new EscapeCoordinate(3, 2), new EscapeCoordinate(4, 3)));

        assertEquals(observer.getMessage(), "Game over in a draw");
        assertNull(observer.getError());

    }

    @Test
    void move_PlayerMovesAfterGameIsOver() throws Exception
    {
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/TurnLimit.egc", observer);
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(1, 1), new EscapeCoordinate(2, 2)));
        assertTrue(gm.move(new EscapeCoordinate(1, 0), new EscapeCoordinate(2, 1)));

        assertTrue(gm.move(new EscapeCoordinate(2, 2), new EscapeCoordinate(3, 3)));
        assertTrue(gm.move(new EscapeCoordinate(2, 1), new EscapeCoordinate(3, 2)));

        assertTrue(gm.move(new EscapeCoordinate(3, 3), new EscapeCoordinate(4, 4)));

        assertTrue(gm.move(new EscapeCoordinate(3, 2), new EscapeCoordinate(4, 3)));

        assertEquals(observer.getMessage(), "Game over in a draw");
        assertNull(observer.getError());

        assertFalse(gm.move(new EscapeCoordinate(3, 2), new EscapeCoordinate(4, 3)));

    }

    @Test
    void move_turnLimitReachedPlayer1Wins() throws Exception
    {
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/TurnLimit.egc", observer);
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(1, 1), new EscapeCoordinate(2, 2)));
        assertTrue(gm.move(new EscapeCoordinate(1, 0), new EscapeCoordinate(2, 1)));

        assertTrue(gm.move(new EscapeCoordinate(2, 2), new EscapeCoordinate(5, 5)));
        assertTrue(gm.move(new EscapeCoordinate(2, 1), new EscapeCoordinate(3, 2)));

        assertTrue(gm.move(new EscapeCoordinate(1, -1), new EscapeCoordinate(2, 0)));

        assertTrue(gm.move(new EscapeCoordinate(3, 2), new EscapeCoordinate(4, 3)));

        assertEquals(observer.getMessage(), "Player 1 wins");
        assertNull(observer.getError());

    }

    @Test
    void move_turnLimitReachedPlayer2Wins() throws Exception
    {
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/TurnLimit.egc", observer);
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(1, 1), new EscapeCoordinate(2, 2)));
        assertTrue(gm.move(new EscapeCoordinate(1, 0), new EscapeCoordinate(2, 1)));

        assertTrue(gm.move(new EscapeCoordinate(2, 2), new EscapeCoordinate(3, 3)));
        assertTrue(gm.move(new EscapeCoordinate(2, 1), new EscapeCoordinate(3, 2)));

        assertTrue(gm.move(new EscapeCoordinate(1, -1), new EscapeCoordinate(2, 0)));

        assertTrue(gm.move(new EscapeCoordinate(3, 2), new EscapeCoordinate(5, 5)));

        assertEquals(observer.getMessage(), "Player 2 wins");
        assertNull(observer.getError());

    }

    @Test
    void move_scoreLimitReachedPlayer1Wins() throws Exception
    {
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/ScoreLimit.egc", observer);
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(1, 1), new EscapeCoordinate(2, 2)));
        assertTrue(gm.move(new EscapeCoordinate(1, 0), new EscapeCoordinate(2, 1)));

        assertTrue(gm.move(new EscapeCoordinate(2, 2), new EscapeCoordinate(5, 5)));

        assertEquals(observer.getMessage(), "Player 1 wins");
        assertNull(observer.getError());


    }

    @Test
    void move_scoreLimitReachedPlayer2Wins() throws Exception
    {
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/ScoreLimit.egc", observer);
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(1, 1), new EscapeCoordinate(2, 2)));
        assertTrue(gm.move(new EscapeCoordinate(1, 0), new EscapeCoordinate(2, 1)));

        assertTrue(gm.move(new EscapeCoordinate(2, 2), new EscapeCoordinate(3, 3)));
        assertTrue(gm.move(new EscapeCoordinate(2, 1), new EscapeCoordinate(3, 2)));

        assertTrue(gm.move(new EscapeCoordinate(1, -1), new EscapeCoordinate(2, 0)));
        assertTrue(gm.move(new EscapeCoordinate(3, 2), new EscapeCoordinate(5, 5)));

        assertEquals(observer.getMessage(), "Player 2 wins");
        assertNull(observer.getError());
    }

    @Test
    void move_scoreLimitAndTurnLimitScoreLimitReached() throws Exception
    {
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/ScoreLimitAndTurnLimit.egc", observer);
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(1, 1), new EscapeCoordinate(5, 5)));
        assertTrue(gm.move(new EscapeCoordinate(1, 0), new EscapeCoordinate(2, 1)));

        assertTrue(gm.move(new EscapeCoordinate(1, -1), new EscapeCoordinate(5, 5)));

        assertEquals(observer.getMessage(), "Player 1 wins");
        assertNull(observer.getError());

    }


    @Test
    void move_scoreLimitAndTurnLimitReachedTurnLimitReached() throws Exception
    {
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/ScoreLimitAndTurnLimit.egc", observer);
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(1, 1), new EscapeCoordinate(2, 2)));
        assertTrue(gm.move(new EscapeCoordinate(1, 0), new EscapeCoordinate(2, 1)));

        assertTrue(gm.move(new EscapeCoordinate(2, 2), new EscapeCoordinate(3, 3)));
        assertTrue(gm.move(new EscapeCoordinate(2, 1), new EscapeCoordinate(3, 2)));

        assertTrue(gm.move(new EscapeCoordinate(1, -1), new EscapeCoordinate(2, 0)));
        assertTrue(gm.move(new EscapeCoordinate(3, 2), new EscapeCoordinate(5, 5)));

        assertEquals(observer.getMessage(), "Player 2 wins");
        assertNull(observer.getError());

    }

    @Test
    void move_scoreLimitAndTurnLimitReachedBothReached() throws Exception
    {
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/ScoreLimitAndTurnLimit.egc", observer);
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(1, 1), new EscapeCoordinate(2, 2)));
        assertTrue(gm.move(new EscapeCoordinate(1, 0), new EscapeCoordinate(5, 5)));

        assertTrue(gm.move(new EscapeCoordinate(2, 2), new EscapeCoordinate(3, 3)));
        assertTrue(gm.move(new EscapeCoordinate(1, -2), new EscapeCoordinate(3, 2)));

        assertTrue(gm.move(new EscapeCoordinate(1, -1), new EscapeCoordinate(2, 0)));
        assertTrue(gm.move(new EscapeCoordinate(3, 2), new EscapeCoordinate(5, 5)));

        assertEquals(observer.getMessage(), "Player 2 wins");
        assertNull(observer.getError());

    }

    @Test
    void move_pieceIsNotRemovedCorrectly() throws Exception
    {
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/NoRemove.egc", observer);

        assertTrue(gm.move(new EscapeCoordinate(6, 6), new EscapeCoordinate(7, 5)));
        assertFalse(gm.move(new EscapeCoordinate(5, 5), new EscapeCoordinate(7, 5)));

    }

    @Test
    void move_pieceIsRemovedCorrectly() throws Exception
    {
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/Remove.egc", observer);

        assertTrue(gm.move(new EscapeCoordinate(6, 6), new EscapeCoordinate(7, 5)));
        assertTrue(gm.move(new EscapeCoordinate(5, 5), new EscapeCoordinate(7, 5)));

        EscapeGamePiece dog = makePiece(
                Player.PLAYER2,
                EscapePiece.PieceName.DOG,
                EscapePiece.MovementPattern.OMNI,
                new PieceAttribute[]{
                        new PieceAttribute(
                                EscapePiece.PieceAttributeID.DISTANCE, 5)}
        );

        assertEquals(gm.getPieceAt(new EscapeCoordinate(7, 5)), dog);
        assertNull(gm.getPieceAt(new EscapeCoordinate(5, 5)));

    }

    @Test
    void move_samePlayerPieceIsNotRemoved() throws Exception
    {
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/Remove.egc", observer);

        assertFalse(gm.move(new EscapeCoordinate(4, 4), new EscapeCoordinate(6, 6)));

        EscapeGamePiece dog = makePiece(
                Player.PLAYER1,
                EscapePiece.PieceName.DOG,
                EscapePiece.MovementPattern.OMNI,
                new PieceAttribute[]{
                        new PieceAttribute(
                                EscapePiece.PieceAttributeID.DISTANCE, 5)}
        );

        assertEquals(gm.getPieceAt(new EscapeCoordinate(6, 6)), dog);
        assertEquals(gm.getPieceAt(new EscapeCoordinate(4, 4)), dog);

    }

    @Test
    void move_Player2RunsOutOfPieces() throws Exception
    {
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/Remove.egc", observer);

        assertTrue(gm.move(new EscapeCoordinate(4, 4), new EscapeCoordinate(5, 5)));

        assertEquals(observer.getMessage(), "Player 1 wins");

    }

    @Test
    void move_Player1RunsOutOfPieces() throws Exception
    {
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/Remove.egc", observer);

        assertTrue(gm.move(new EscapeCoordinate(6, 6), new EscapeCoordinate(5, 10)));
        assertTrue(gm.move(new EscapeCoordinate(5, 5), new EscapeCoordinate(4, 4)));

        assertTrue(gm.move(new EscapeCoordinate(5, 10), new EscapeCoordinate(5, 12)));

        GameManager mygm = (GameManager) gm;

        assertEquals(mygm.getPlayerScore(Player.PLAYER1), 2);

        assertEquals(observer.getMessage(), "Player 2 wins");

    }




    // ---- observer tests


    @Test
    void addingObserver() throws Exception
    {
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/ScoreLimitAndTurnLimit.egc", observer);
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(1, 1), new EscapeCoordinate(2, 2)));
        assertEquals(observer.getMessage(), Player.PLAYER1 + " move was successful");
    }

    @Test
    void addingMultipleObservers() throws Exception
    {
        EscapeGameObsever observer1 = new EscapeGameObsever();
        EscapeGameObsever observer2 = new EscapeGameObsever();
        EscapeGameObsever observer3 = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/ScoreLimitAndTurnLimit.egc", observer1);
        gm.addObserver(observer2);
        gm.addObserver(observer3);

        assertTrue(gm.move(new EscapeCoordinate(1, 1), new EscapeCoordinate(2, 2)));
        assertEquals(observer1.getMessage(), Player.PLAYER1 + " move was successful");
        assertEquals(observer2.getMessage(), Player.PLAYER1 + " move was successful");
        assertEquals(observer3.getMessage(), Player.PLAYER1 + " move was successful");
    }

    @Test
    void removeObserver() throws Exception
    {
        EscapeGameObsever observer = new EscapeGameObsever();

        EscapeGameManager<EscapeCoordinate> gm = makeGameManager("Escape/config/egc/ScoreLimitAndTurnLimit.egc", observer);
        GameManager myGm = (GameManager) gm;

        assertTrue(gm.move(new EscapeCoordinate(1, 1), new EscapeCoordinate(2, 2)));
        assertTrue(gm.move(new EscapeCoordinate(1, 0), new EscapeCoordinate(5, 5)));

        assertTrue(gm.move(new EscapeCoordinate(2, 2), new EscapeCoordinate(3, 3)));
        gm.removeObserver(observer);
        assertTrue(gm.move(new EscapeCoordinate(1, -2), new EscapeCoordinate(3, 2)));

        assertTrue(gm.move(new EscapeCoordinate(1, -1), new EscapeCoordinate(2, 0)));
        assertTrue(gm.move(new EscapeCoordinate(3, 2), new EscapeCoordinate(5, 5)));

        // observer does not receive the Player 2 wins notification
        assertEquals(observer.getMessage(), Player.PLAYER1 + " move was successful");
        assertNull(observer.getError());
    }








}
