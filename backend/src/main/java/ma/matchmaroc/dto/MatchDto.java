package ma.matchmaroc.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class MatchDto {
    private Long id;
    private String teamA;
    private String teamB;
    private String flagA;
    private String flagB;
    private Instant dateTimeUtc;
    private String dateTimeMaroc;
    private String dateFormatted;
    private String timeFormatted;
    private String stadium;
    private String city;
    private String competition;
    private String tvChannel;
    private String groupStage;
    private boolean played;
    private Integer scoreA;
    private Integer scoreB;
    private long secondsUntilMatch;
}
