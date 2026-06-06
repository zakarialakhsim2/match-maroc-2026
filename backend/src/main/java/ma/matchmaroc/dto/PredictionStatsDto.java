package ma.matchmaroc.dto;

import lombok.Data;

@Data
public class PredictionStatsDto {
    private Long matchId;
    private long totalVotes;
    private long winCount;
    private long drawCount;
    private long loseCount;
    private double winPct;
    private double drawPct;
    private double losePct;
    private double avgScoreA;
    private double avgScoreB;
}
