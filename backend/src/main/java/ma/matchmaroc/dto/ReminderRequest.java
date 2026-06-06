package ma.matchmaroc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReminderRequest {
    @NotNull
    private Long matchId;
    @Email
    private String email;
    private String deviceId;
}
