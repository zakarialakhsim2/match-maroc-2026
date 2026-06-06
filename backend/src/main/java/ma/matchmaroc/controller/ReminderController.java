package ma.matchmaroc.controller;

import jakarta.validation.Valid;
import ma.matchmaroc.dto.ReminderRequest;
import ma.matchmaroc.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    @Autowired
    private ReminderService reminderService;

    @PostMapping
    public ResponseEntity<String> register(@Valid @RequestBody ReminderRequest req) {
        reminderService.register(req);
        return ResponseEntity.ok("Rappel enregistré avec succès");
    }
}
