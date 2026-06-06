package ma.matchmaroc.repository;

import ma.matchmaroc.entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    List<Prediction> findByMatchId(Long matchId);
    Optional<Prediction> findByMatchIdAndDeviceId(Long matchId, String deviceId);
    long countByMatchId(Long matchId);
}
