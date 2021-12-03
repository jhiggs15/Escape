package escape;

import escape.piece.EscapeGamePiece;
import escape.required.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreManager
{
    private Score player1Score = new Score(Player.PLAYER1);
    private Score player2Score = new Score(Player.PLAYER2);


    public void addScore(Score score)
    {
        if(isPlayer1(score.getPlayer()))
            player1Score.combineScores(score);
        else
            player2Score.combineScores(score);
    }

    public int getPlayerScore(Player player)
    {
        if(isPlayer1(player))
            return player1Score.getPlayerScore();
        else
            return player2Score.getPlayerScore();
    }

    private boolean isPlayer1(Player player)
    {
        return player.equals(Player.PLAYER1);
    }

}
