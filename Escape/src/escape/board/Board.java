package escape.board;

import escape.piece.EscapeGamePiece;
import escape.required.Coordinate;
import escape.required.EscapePiece;
import escape.required.LocationType;
import escape.util.EscapeGameInitializer;
import escape.util.LocationInitializer;
import escape.util.PieceTypeDescriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Board {

    private HashMap<EscapeCoordinate, BoardSpace> board = new HashMap<>();
    private Coordinate.CoordinateType boardType;
    private BoundsChecker boundsChecker;
    private List<EscapeGamePiece> piecesOnTheBoard = new ArrayList<>();

    public Board(EscapeGameInitializer gameInitializer)
    {
        this.boundsChecker = new BoundsChecker(gameInitializer.getxMax(), gameInitializer.getyMax());
        this.boardType = gameInitializer.getCoordinateType();

        HashMap<EscapePiece.PieceName, EscapeGamePiece> piecesWithoutOwner = new HashMap();

        for(PieceTypeDescriptor pieceTypeDescriptor : gameInitializer.getPieceTypes())
            piecesWithoutOwner.put(pieceTypeDescriptor.getPieceName(), new EscapeGamePiece(pieceTypeDescriptor, this));


        // create pieces
        // create locations with pieces
        for(LocationInitializer location : gameInitializer.getLocationInitializers())
        {
            // gets the piece at the location and null otherwise
            EscapeGamePiece pieceAtCurrentLocation = piecesWithoutOwner.get(location.pieceName);
            // if a piece exists in the current location create a new piece with a player attached to it
            if(pieceAtCurrentLocation != null)
            {
                pieceAtCurrentLocation = pieceAtCurrentLocation.makeCopy(location.player);
                piecesOnTheBoard.add(pieceAtCurrentLocation);
            }

            // creates a board space on the board, or adds to it depending on the context
            createBoardSpace(location.x, location.y, location.locationType, pieceAtCurrentLocation);


        }

    }



    public Coordinate.CoordinateType getBoardType() {
        return boardType;
    }

    public HashMap<EscapeCoordinate, BoardSpace> getBoard() {
        return board;
    }

    public List<EscapeGamePiece> getPiecesOnTheBoard()
    {
        return piecesOnTheBoard;
    }

    private void createBoardSpace(int x, int y, LocationType locationType, EscapeGamePiece piece)
    {
        // create coordinate
        EscapeCoordinate coordinate = new EscapeCoordinate(x, y);
        if(!board.containsKey(coordinate)) // if the board doesnt already contain the coordinate add it as a new space
            board.put(
                    new EscapeCoordinate(x, y),
                    new BoardSpace(piece, locationType)
            );
        else  // otherwise, add the piece to this spot on the board
            board.get(coordinate).addPiece(piece);
    }
    private BoardSpace createPiece()
    {
        return null;
    }


}
