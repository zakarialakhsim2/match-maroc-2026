package ma.matchmaroc.service;

import ma.matchmaroc.dto.PredictionRequest;
import ma.matchmaroc.dto.PredictionStatsDto;
import ma.matchmaroc.entity.Prediction;
import ma.matchmaroc.repository.PredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PredictionService {

    @Autowired
    private PredictionRepository predictionRepository;

    public Prediction save(PredictionRequest req) {
        // Update existing vote from same device
        predictionRepository.findByMatchIdAndDeviceId(req.getMatchId(), req.getDeviceId())
            .ifPresent(p -> predictionRepository.deleteById(p.getId()));

        Prediction p = new Prediction();
        p.setMatchId(req.getMatchId());
        p.setScoreTeamA(req.getScoreTeamA());
        p.setScoreTeamB(req.getScoreTeamB());
        p.setDeviceId(req.getDeviceId());
        return predictionRepository.save(p);
    }

    public PredictionStatsDto getStats(Long matchId) {
        List<Prediction> preds = predictionRepository.findByMatchId(matchId);
        PredictionStatsDto stats = new PredictionStatsDto();
        stats.setMatchId(matchId);
        stats.setTotalVotes(preds.size());

        if (preds.isEmpty()) {
            stats.setWinPct(0); stats.setDrawPct(0); stats.setLosePct(0);
            return stats;
        }

        long wins = preds.stream().filter(p -> p.getScoreTeamA() > p.getScoreTeamB()).count();
        long draws = preds.stream().filter(p -> p.getScoreTeamA().equals(p.getScoreTeamB())).count();
        long loses = preds.size() - wins - draws;

        double avgA = preds.stream().mapToInt(Prediction::getScoreTeamA).average().orElse(0);
        double avgB = preds.stream().mapToInt(Prediction::getScoreTeamB).average().orElse(0);

        stats.setWinCount(wins);
        stats.setDrawCount(draws);
        stats.setLoseCount(loses);
        double total = preds.size();
        stats.setWinPct(Math.round((wins / total) * 100.0));
        stats.setDrawPct(Math.round((draws / total) * 100.0));
        stats.setLosePct(Math.round((loses / total) * 100.0));
        stats.setAvgScoreA(Math.round(avgA * 10.0) / 10.0);
        stats.setAvgScoreB(Math.round(avgB * 10.0) / 10.0);
        return stats;
    }
}
