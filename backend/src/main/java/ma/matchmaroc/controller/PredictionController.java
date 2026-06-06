package ma.matchmaroc.controller;

import jakarta.validation.Valid;
import ma.matchmaroc.dto.PredictionRequest;
import ma.matchmaroc.dto.PredictionStatsDto;
import ma.matchmaroc.entity.Prediction;
import ma.matchmaroc.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/predictions")
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    @PostMapping
    public ResponseEntity<Prediction> vote(@Valid @RequestBody PredictionRequest req) {
        return ResponseEntity.ok(predictionService.save(req));
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<PredictionStatsDto> getStats(@PathVariable Long matchId) {
        return ResponseEntity.ok(predictionService.getStats(matchId));
    }
}
