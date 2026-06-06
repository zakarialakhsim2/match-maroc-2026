package ma.matchmaroc.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Entity
@Table(name = "predictions")
@Data
public class Prediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long matchId;

    @Column(nullable = false)
    private Integer scoreTeamA;

    @Column(nullable = false)
    private Integer scoreTeamB;

    private String deviceId;
    private Instant createdAt = Instant.now();
}
