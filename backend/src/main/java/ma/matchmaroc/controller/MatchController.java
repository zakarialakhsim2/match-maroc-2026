package ma.matchmaroc.controller;

import ma.matchmaroc.dto.MatchDto;
import ma.matchmaroc.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @GetMapping("/upcoming")
    public ResponseEntity<MatchDto> getUpcoming() {
        return ResponseEntity.ok(matchService.getNextMatch());
    }

    @GetMapping("/morocco")
    public ResponseEntity<List<MatchDto>> getMoroccoMatches() {
        return ResponseEntity.ok(matchService.getAllMoroccoMatches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchDto> getById(@PathVariable Long id) {
        return matchService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
