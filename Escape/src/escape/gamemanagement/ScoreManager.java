package escape.gamemanagement;

import escape.piece.EscapeGamePiece;
import escape.required.Player;

import java.util.List;
import java.util.stream.Collectors;

public class ScoreManager
{
    private Score player1Score = new Score(Player.PLAYER1);
    private Score player2Score = new Score(Player.PLAYER2);
    private List<EscapeGamePiece> player1PiecesOnTheBoard;
    private List<EscapeGamePiece> player2PiecesOnTheBoard;


    public void addScore(Score score)
    {
        if(isPlayer1(score.getPlayer()))
            player1Score.combineScores(score);
        else
            player2Score.combineScores(score);
    }

    public void updatePiecesOnBoard(List<EscapeGamePiece> gamePieces)
    {
        player1PiecesOnTheBoard = gamePieces.stream().filter(piece -> piece.getPlayer().equals(Player.PLAYER1)).collect(Collectors.toList());
        player2PiecesOnTheBoard = gamePieces.stream().filter(piece -> piece.getPlayer().equals(Player.PLAYER2)).collect(Collectors.toList());
    }

    public boolean hasPlayersLeft(Player player)
    {
        if(isPlayer1(player))
            return player1PiecesOnTheBoard.size() != 0;
        else
            return player2PiecesOnTheBoard.size() != 0;
    }

    public int getPlayerScore(Player player)
    {
        if(isPlayer1(player))
            return player1Score.getPlayerScore();
        else
            return player2Score.getPlayerScore();
    }

//    public int getTotalPossibleScore(Player player)
//    {
//        if(isPlayer1(player))
//            return player1Score.getPlayerScore() + sumPiecesOnBoard(player1PiecesOnTheBoard);
//        else
//            return player2Score.getPlayerScore() + sumPiecesOnBoard(player1PiecesOnTheBoard);
//    }

//    private int sumPiecesOnBoard(List<EscapeGamePiece> pieces)
//    {
//        int sum = 0;
//        for(EscapeGamePiece piece : pieces)
//            sum += piece.getValue();
//        return sum;
//    }


    private boolean isPlayer1(Player player)
    {
        return player.equals(Player.PLAYER1);
    }

}
