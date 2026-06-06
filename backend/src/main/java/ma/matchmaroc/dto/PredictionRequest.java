package ma.matchmaroc.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PredictionRequest {
    @NotNull
    private Long matchId;
    @NotNull @Min(0) @Max(20)
    private Integer scoreTeamA;
    @NotNull @Min(0) @Max(20)
    private Integer scoreTeamB;
    private String deviceId;
}
