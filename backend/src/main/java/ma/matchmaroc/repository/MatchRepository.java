package ma.matchmaroc.repository;

import ma.matchmaroc.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    @Query("SELECT m FROM Match m WHERE (m.teamA = 'Morocco' OR m.teamB = 'Morocco') AND m.dateTimeUtc > :now ORDER BY m.dateTimeUtc ASC")
    List<Match> findNextMoroccoMatch(Instant now);

    @Query("SELECT m FROM Match m WHERE m.teamA = 'Morocco' OR m.teamB = 'Morocco' ORDER BY m.dateTimeUtc ASC")
    List<Match> findAllMoroccoMatches();

    @Query("SELECT m FROM Match m WHERE m.dateTimeUtc > :now ORDER BY m.dateTimeUtc ASC")
    List<Match> findUpcoming(Instant now);
}
