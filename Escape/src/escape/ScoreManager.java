package escape;

import escape.piece.EscapeGamePiece;
import escape.required.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreManager
{
    private int player1Score = 0;
    private int player2Score = 0;

    List<EscapeGamePiece> player1ScoredPieces = new ArrayList<>();
    List<EscapeGamePiece> player2ScoredPieces = new ArrayList<>();

    public void addScore(Score score)
    {
        int playerScore = score.getPlayerScore();
        List<EscapeGamePiece> gamePieces = score.getPlayerScoredGamePieces();

        if(isPlayer1(score.getPlayer()))
        {
            player1Score += playerScore;
            player1ScoredPieces.addAll(gamePieces);
        }
        else
        {
            player2Score += playerScore;
            player2ScoredPieces.addAll(gamePieces);
        }
    }

    private boolean isPlayer1(Player player)
    {
        return player.equals(Player.PLAYER1);
    }

}
