package ma.matchmaroc.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Entity
@Table(name = "reminders")
@Data
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long matchId;

    private String email;
    private String deviceId;
    private boolean sent24h = false;
    private boolean sent1h = false;
    private Instant registeredAt = Instant.now();
}
