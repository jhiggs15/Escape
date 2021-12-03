package escape.board;

import escape.EscapeGameBuilder;
import escape.piece.EscapeGamePiece;
import escape.movement.MoveManager;
import escape.required.EscapePiece;
import escape.required.LocationType;
import escape.required.Player;
import escape.util.PieceAttribute;
import escape.util.PieceTypeDescriptor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.*;

public class BoardTest
{
    // ----- get piece
    @Test
    void getPieceThatExists() throws Exception {
        EscapeGameBuilder egb = new EscapeGameBuilder("Escape/config/egc/NoPieces.egc");
        Board board = new Board(egb.getGameInitializer());
        EscapeGamePiece snail = makePiece(
                Player.PLAYER1,
                EscapePiece.PieceName.SNAIL,
                EscapePiece.MovementPattern.OMNI,
                new PieceAttribute[]{
                        new PieceAttribute(
                                EscapePiece.PieceAttributeID.DISTANCE, 1)},
                board);
        board.createBoardSpace(makeCoordinate(1,1),null, snail );
        assertNotNull(board.getPieceAt(makeCoordinate(1, 1)));
    }

    @Test
    void getPieceThatDNEHasSpace() throws Exception {
        EscapeGameBuilder egb = new EscapeGameBuilder("Escape/config/egc/NoPieces.egc");
        Board board = new Board(egb.getGameInitializer());
        EscapeGamePiece snail = makePiece(
                Player.PLAYER1,
                EscapePiece.PieceName.SNAIL,
                EscapePiece.MovementPattern.OMNI,
                new PieceAttribute[]{
                        new PieceAttribute(
                                EscapePiece.PieceAttributeID.DISTANCE, 1)},
                board);
        board.createBoardSpace(makeCoordinate(1,1),null, null );
        assertNull(board.getPieceAt(makeCoordinate(1, 1)));
    }

    @Test
    void getPieceThatDNEHasNoSpace() throws Exception {
        EscapeGameBuilder egb = new EscapeGameBuilder("Escape/config/egc/NoPieces.egc");
        Board board = new Board(egb.getGameInitializer());
        EscapeGamePiece snail = makePiece(
                Player.PLAYER1,
                EscapePiece.PieceName.SNAIL,
                EscapePiece.MovementPattern.OMNI,
                new PieceAttribute[]{
                        new PieceAttribute(
                                EscapePiece.PieceAttributeID.DISTANCE, 1)},
                board);
        assertNull(board.getPieceAt(makeCoordinate(1, 1)));
    }

    // ----- Add piece to the board
    @Test
    void addNewPieceToNewCoordinate() throws Exception {
        EscapeGameBuilder egb = new EscapeGameBuilder("Escape/config/egc/NoPieces.egc");
        Board board = new Board(egb.getGameInitializer());
        EscapeGamePiece snail = makePiece(
                Player.PLAYER1,
                EscapePiece.PieceName.SNAIL,
                EscapePiece.MovementPattern.OMNI,
                new PieceAttribute[]{
                        new PieceAttribute(
                                EscapePiece.PieceAttributeID.DISTANCE, 1)},
                board);
        board.createBoardSpace(makeCoordinate(1,1),null, snail );
        assertTrue(board.getPieceAt(makeCoordinate(1, 1)).equals(snail));
    }

    @Test
    void addNewPieceToPiecelessBoardSpace() throws Exception {
        EscapeGameBuilder egb = new EscapeGameBuilder("Escape/config/egc/NoPieces.egc");
        Board board = new Board(egb.getGameInitializer());
        EscapeGamePiece snail = makePiece(
                Player.PLAYER1,
                EscapePiece.PieceName.SNAIL,
                EscapePiece.MovementPattern.OMNI,
                new PieceAttribute[]{
                        new PieceAttribute(
                                EscapePiece.PieceAttributeID.DISTANCE, 1)},
                board);
        board.createBoardSpace(makeCoordinate(1,1),null, null);
        assertEquals(board.getPieceAt(makeCoordinate(1, 1)), null);
        board.createBoardSpace(makeCoordinate(1, 1), null, snail);
        assertEquals(board.getPieceAt(makeCoordinate(1, 1)), snail);
    }


    @Test
    void createEmptySpace() throws Exception {
        EscapeGameBuilder egb = new EscapeGameBuilder("Escape/config/egc/NoPieces.egc");
        Board board = new Board(egb.getGameInitializer());

        board.createBoardSpace(makeCoordinate(1,1),null, null);
        assertEquals(board.getPieceAt(makeCoordinate(1, 1)), null);
    }


    //  ----- Pieces Created
    @Test
    void test1Pieces() throws Exception {
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
    void bothPlayersSamePiece() throws Exception {
        EscapeGameBuilder egb = new EscapeGameBuilder("Escape/config/egc/PlayersHaveSamePiece.egc");
        Board board = new Board(egb.getGameInitializer());
        ArrayList<EscapeGamePiece> pieces = new ArrayList<>();
        pieces.add(makePiece(
                Player.PLAYER1,
                EscapePiece.PieceName.DOG,
                EscapePiece.MovementPattern.OMNI,
                new PieceAttribute[]{
                        new PieceAttribute(
                                EscapePiece.PieceAttributeID.DISTANCE, 5)},
                board));

        pieces.add(makePiece(
                Player.PLAYER2,
                EscapePiece.PieceName.DOG,
                EscapePiece.MovementPattern.OMNI,
                new PieceAttribute[]{
                        new PieceAttribute(
                                EscapePiece.PieceAttributeID.DISTANCE, 5)},
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
                        attributes));
    }


    //  ----- Board Spaces Created
    @Test
    void test1BoardSpaces() throws Exception {
        EscapeGameBuilder egb = new EscapeGameBuilder("Escape/config/egc/test1.egc");
        Board board = new Board(egb.getGameInitializer());

        EscapeGamePiece snail = makePiece(
            Player.PLAYER1,
            EscapePiece.PieceName.SNAIL,
            EscapePiece.MovementPattern.OMNI,
            new PieceAttribute[]{
                    new PieceAttribute(
                            EscapePiece.PieceAttributeID.DISTANCE, 1)},
            board);

        EscapeGamePiece horse = makePiece(
                Player.PLAYER2,
                EscapePiece.PieceName.HORSE,
                EscapePiece.MovementPattern.OMNI,
                new PieceAttribute[]{
                        new PieceAttribute(
                                EscapePiece.PieceAttributeID.DISTANCE, 7),
                        new PieceAttribute(
                                EscapePiece.PieceAttributeID.JUMP)
                },
                board);


        ArrayList<Space> boardSpaces = new ArrayList<>();
        boardSpaces.add(
                new Space(
                        makeCoordinate(4, 4),
                        makeBoardSpace(snail, LocationType.CLEAR)
                )
        );
        boardSpaces.add(
                new Space(
                        makeCoordinate(10, 12),
                        makeBoardSpace(horse, LocationType.CLEAR)
                )
        );

        boardSpaces.add(
                new Space(
                        makeCoordinate(5, 12),
                        makeBoardSpace(null, LocationType.EXIT)
                )
        );

        assertFalse(board.getBoard().isEmpty());

        for(Space boardSpace : boardSpaces)
        {
            assertEquals(board.getBoard().get(boardSpace.getCoordinate()), boardSpace.getBoardSpace());
        }
    }


    public static BoardSpace makeBoardSpace(EscapeGamePiece piece, LocationType type){
        return new BoardSpace(piece, type);
    }

    public static EscapeCoordinate makeCoordinate(int x, int y){
        return new EscapeCoordinate(x, y);
    }


    class Space
    {
        private EscapeCoordinate coordinate;
        private BoardSpace boardSpace;

        public EscapeCoordinate getCoordinate() {
            return coordinate;
        }

        public BoardSpace getBoardSpace() {
            return boardSpace;
        }

        public Space(EscapeCoordinate coordinate, BoardSpace boardSpace) {
            this.coordinate = coordinate;
            this.boardSpace = boardSpace;
        }





    }


}
