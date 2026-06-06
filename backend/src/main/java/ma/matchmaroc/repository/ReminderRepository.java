package ma.matchmaroc.repository;

import ma.matchmaroc.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByMatchId(Long matchId);
    Optional<Reminder> findByMatchIdAndDeviceId(Long matchId, String deviceId);
    List<Reminder> findBySent24hFalseOrSent1hFalse();
}
