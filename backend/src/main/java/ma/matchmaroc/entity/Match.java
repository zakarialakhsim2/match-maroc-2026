package ma.matchmaroc.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Entity
@Table(name = "matches")
@Data
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String teamA;

    @Column(nullable = false)
    private String teamB;

    private String flagA;
    private String flagB;

    @Column(nullable = false)
    private Instant dateTimeUtc;

    private String stadium;
    private String city;
    private String competition;
    private String tvChannel;
    private String groupStage;
    private boolean played = false;
    private Integer scoreA;
    private Integer scoreB;
}
