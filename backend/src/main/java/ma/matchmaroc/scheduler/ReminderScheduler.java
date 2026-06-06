package ma.matchmaroc.scheduler;

import ma.matchmaroc.entity.Match;
import ma.matchmaroc.entity.Reminder;
import ma.matchmaroc.repository.MatchRepository;
import ma.matchmaroc.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Component
public class ReminderScheduler {

    private static final ZoneId MAROC = ZoneId.of("Africa/Casablanca");

    @Autowired private ReminderRepository reminderRepo;
    @Autowired private MatchRepository matchRepo;
    @Autowired(required = false) private JavaMailSender mailSender;

    @Scheduled(fixedDelay = 300_000) // every 5 minutes
    public void sendReminders() {
        if (mailSender == null) return;
        Instant now = Instant.now();
        List<Match> matches = matchRepo.findUpcoming(now);

        for (Match m : matches) {
            long mins = Duration.between(now, m.getDateTimeUtc()).toMinutes();
            if (mins >= 55 && mins <= 75) sendForMatch(m, "1h");
            if (mins >= 1400 && mins <= 1450) sendForMatch(m, "24h");
        }
    }

    private void sendForMatch(Match m, String type) {
        List<Reminder> reminders = reminderRepo.findByMatchId(m.getId());
        String matchTime = m.getDateTimeUtc().atZone(MAROC)
            .format(DateTimeFormatter.ofPattern("dd MMMM yyyy 'à' HH'h'mm", Locale.FRENCH));

        for (Reminder r : reminders) {
            if (r.getEmail() == null) continue;
            boolean alreadySent = type.equals("1h") ? r.isSent1h() : r.isSent24h();
            if (!alreadySent) {
                sendEmail(r.getEmail(), m, matchTime, type);
                if (type.equals("1h")) r.setSent1h(true);
                else r.setSent24h(true);
                reminderRepo.save(r);
            }
        }
    }

    private void sendEmail(String to, Match m, String matchTime, String type) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject("🇲🇦 " + m.getTeamA() + " vs " + m.getTeamB() + " — dans " + type + " !");
            msg.setText(
                "Yallah ! Le match commence dans " + type + " !\n\n" +
                m.getTeamA() + " vs " + m.getTeamB() + "\n" +
                matchTime + "\n" +
                m.getStadium() + ", " + m.getCity() + "\n\n" +
                "TV : " + m.getTvChannel() + "\n\n" +
                "Allez les Lions de l'Atlas ! 🇲🇦🦁\n" +
                "— Match Maroc 2026"
            );
            mailSender.send(msg);
        } catch (Exception e) {
            System.err.println("Email failed to " + to + ": " + e.getMessage());
        }
    }
}
