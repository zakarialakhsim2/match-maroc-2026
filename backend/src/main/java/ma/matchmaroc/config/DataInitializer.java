package ma.matchmaroc.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.matchmaroc.entity.Match;
import ma.matchmaroc.repository.MatchRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Configuration
public class DataInitializer {

    private static final String MATCHES_URL =
            "https://raw.githubusercontent.com/rezarahiminia/worldcup2026/main/football.matches.json";
    private static final String TEAMS_URL =
            "https://raw.githubusercontent.com/rezarahiminia/worldcup2026/main/football.teams.json";
    private static final String STADIUMS_URL =
            "https://raw.githubusercontent.com/rezarahiminia/worldcup2026/main/football.stadiums.json";

    private static final String MOROCCO_TEAM_ID = "10";
    private static final DateTimeFormatter LOCAL_DATE_FMT =
            DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
    // The API dates are US Eastern time (UTC-4 during summer)
    private static final ZoneId MATCH_ZONE = ZoneId.of("America/New_York");

    @Bean
    CommandLineRunner initData(MatchRepository repo) {
        return args -> {
            if (repo.count() > 0) return;

            try {
                HttpClient client = HttpClient.newHttpClient();
                ObjectMapper mapper = new ObjectMapper();

                // Load all three datasets
                JsonNode matchesJson = mapper.readTree(fetch(client, MATCHES_URL));
                JsonNode teamsJson   = mapper.readTree(fetch(client, TEAMS_URL));
                JsonNode stadiumsJson= mapper.readTree(fetch(client, STADIUMS_URL));

                // Build lookup maps
                Map<String, JsonNode> teams   = new HashMap<>();
                Map<String, JsonNode> stadiums = new HashMap<>();
                teamsJson.forEach(t   -> teams.put(t.get("id").asText(), t));
                stadiumsJson.forEach(s -> stadiums.put(s.get("id").asText(), s));

                List<Match> moroccoMatches = new ArrayList<>();

                for (JsonNode m : matchesJson) {
                    String homeId = m.get("home_team_id").asText();
                    String awayId = m.get("away_team_id").asText();

                    if (!homeId.equals(MOROCCO_TEAM_ID) && !awayId.equals(MOROCCO_TEAM_ID)) continue;

                    JsonNode homeTeam = teams.get(homeId);
                    JsonNode awayTeam = teams.get(awayId);
                    JsonNode stadium  = stadiums.get(m.get("stadium_id").asText());

                    // Parse local date → UTC Instant
                    String localDateStr = m.get("local_date").asText(); // "06/13/2026 18:00"
                    LocalDateTime ldt = LocalDateTime.parse(localDateStr, LOCAL_DATE_FMT);
                    Instant kickoff = ldt.atZone(MATCH_ZONE).toInstant();

                    boolean finished = "TRUE".equalsIgnoreCase(m.get("finished").asText());

                    Match match = new Match();
                    match.setTeamA(homeTeam.get("name_en").asText());
                    match.setTeamB(awayTeam.get("name_en").asText());
                    // Use ISO country code for flag (matches existing flagcdn.com pattern)
                    match.setFlagA(homeTeam.get("iso2").asText().toLowerCase());
                    match.setFlagB(awayTeam.get("iso2").asText().toLowerCase());
                    match.setDateTimeUtc(kickoff);
                    match.setStadium(stadium != null ? stadium.get("name_en").asText() : "TBD");
                    match.setCity(stadium != null ? stadium.get("city_en").asText() : "TBD");
                    match.setCompetition("Coupe du Monde FIFA 2026");
                    match.setGroupStage("Groupe " + m.get("group").asText());
                    match.setTvChannel("2M · Arryadia · beIN Sports");
                    match.setPlayed(finished);
                    if (finished) {
                        match.setScoreA(m.get("home_score").asInt());
                        match.setScoreB(m.get("away_score").asInt());
                    }

                    moroccoMatches.add(match);
                }

                repo.saveAll(moroccoMatches);
                System.out.println("✅ Loaded " + moroccoMatches.size() + " Morocco matches from worldcup2026 API");

            } catch (Exception e) {
                System.err.println("⚠️  Could not load from API, falling back to hardcoded data: " + e.getMessage());
                fallback(repo);
            }
        };
    }

    private String fetch(HttpClient client, String url) throws Exception {
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).build();
        return client.send(req, HttpResponse.BodyHandlers.ofString()).body();
    }

    private void fallback(MatchRepository repo) {
        // Correct data from the API (Group C, real opponents)
        repo.saveAll(List.of(
                createMatch("Brazil",   "Morocco",  "br", "ma", "2026-06-13T22:00:00Z",
                        "MetLife Stadium", "New York/New Jersey", "Groupe C"),
                createMatch("Scotland", "Morocco",  "gb-sct", "ma", "2026-06-19T22:00:00Z",
                        "Gillette Stadium", "Boston", "Groupe C"),
                createMatch("Morocco",  "Haiti",    "ma", "ht", "2026-06-24T22:00:00Z",
                        "Mercedes-Benz Stadium", "Atlanta", "Groupe C")
        ));
    }

    private Match createMatch(String teamA, String teamB, String flagA, String flagB,
                              String dateUtc, String stadium, String city, String group) {
        Match m = new Match();
        m.setTeamA(teamA); m.setTeamB(teamB);
        m.setFlagA(flagA); m.setFlagB(flagB);
        m.setDateTimeUtc(Instant.parse(dateUtc));
        m.setStadium(stadium); m.setCity(city);
        m.setCompetition("Coupe du Monde FIFA 2026");
        m.setGroupStage(group);
        m.setTvChannel("2M · Arryadia · beIN Sports");
        return m;
    }
}