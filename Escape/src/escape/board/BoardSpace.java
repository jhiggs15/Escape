package escape.board;

import escape.piece.EscapeGamePiece;
import escape.required.EscapePiece;
import escape.required.LocationType;
import escape.required.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BoardSpace
{
    private List<EscapeGamePiece> pieces = new ArrayList<>();
    private LocationType type = LocationType.CLEAR;

    public BoardSpace(EscapeGamePiece piece, LocationType type)
    {
        if(type != null) this.type = type;
        addPiece(piece);
    }

    public boolean spaceIsEmpty()
    {
        return pieces.size() == 0;
    }

    public boolean isAccessible(EscapeGamePiece piece)
    {
        if(piece.hasAttribute(EscapePiece.PieceAttributeID.UNBLOCK) ||
                        piece.hasAttribute(EscapePiece.PieceAttributeID.FLY))
            return spaceIsEmpty();

        return type != LocationType.BLOCK && spaceIsEmpty();
    }


    public EscapeGamePiece removePiece()
    {
        return pieces.remove(0);
    }
//    public EscapeGamePiece removePiece(EscapeGamePiece piece){ return null; }
    public void addPiece(EscapeGamePiece piece)
    {
        if(piece != null) pieces.add(piece);
    }

    public EscapeGamePiece getPiece(){
        if(!pieces.isEmpty()) return pieces.get(0);
        return null;
    }
    public LocationType getType() { return type; }


    @Override
    public boolean equals(Object otherBoardSpace) {
        if(otherBoardSpace.getClass() != this.getClass()) return false;
        BoardSpace otherBP = (BoardSpace) otherBoardSpace;
        return otherBP.pieces.equals(pieces) && type == otherBP.getType();
    }
}
