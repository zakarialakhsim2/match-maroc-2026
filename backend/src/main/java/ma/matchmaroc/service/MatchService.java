package ma.matchmaroc.service;

import ma.matchmaroc.dto.MatchDto;
import ma.matchmaroc.entity.Match;
import ma.matchmaroc.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private static final ZoneId MAROC_ZONE = ZoneId.of("Africa/Casablanca");
    private static final DateTimeFormatter DATE_FMT =
        DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH);
    private static final DateTimeFormatter TIME_FMT =
        DateTimeFormatter.ofPattern("HH'h'mm");

    @Autowired
    private MatchRepository matchRepository;

    public MatchDto getNextMatch() {
        List<Match> matches = matchRepository.findNextMoroccoMatch(Instant.now());
        if (matches.isEmpty()) throw new RuntimeException("Aucun match à venir");
        return toDto(matches.get(0));
    }

    public List<MatchDto> getAllMoroccoMatches() {
        return matchRepository.findAllMoroccoMatches()
            .stream().map(this::toDto).collect(Collectors.toList());
    }

    public Optional<MatchDto> findById(Long id) {
        return matchRepository.findById(id).map(this::toDto);
    }

    private MatchDto toDto(Match m) {
        MatchDto dto = new MatchDto();
        dto.setId(m.getId());
        dto.setTeamA(m.getTeamA());
        dto.setTeamB(m.getTeamB());
        dto.setFlagA(m.getFlagA());
        dto.setFlagB(m.getFlagB());
        dto.setDateTimeUtc(m.getDateTimeUtc());
        dto.setStadium(m.getStadium());
        dto.setCity(m.getCity());
        dto.setCompetition(m.getCompetition());
        dto.setTvChannel(m.getTvChannel());
        dto.setGroupStage(m.getGroupStage());
        dto.setPlayed(m.isPlayed());
        dto.setScoreA(m.getScoreA());
        dto.setScoreB(m.getScoreB());

        ZonedDateTime marocTime = m.getDateTimeUtc().atZone(MAROC_ZONE);
        dto.setDateFormatted(marocTime.format(DATE_FMT));
        dto.setTimeFormatted(marocTime.format(TIME_FMT));
        dto.setDateTimeMaroc(marocTime.format(DATE_FMT) + " à " + marocTime.format(TIME_FMT));

        long seconds = Duration.between(Instant.now(), m.getDateTimeUtc()).toSeconds();
        dto.setSecondsUntilMatch(Math.max(0, seconds));

        return dto;
    }
}
