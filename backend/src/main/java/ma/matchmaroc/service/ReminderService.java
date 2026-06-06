package ma.matchmaroc.service;

import ma.matchmaroc.dto.ReminderRequest;
import ma.matchmaroc.entity.Reminder;
import ma.matchmaroc.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReminderService {

    @Autowired
    private ReminderRepository reminderRepository;

    public void register(ReminderRequest req) {
        if (req.getDeviceId() != null) {
            reminderRepository.findByMatchIdAndDeviceId(req.getMatchId(), req.getDeviceId())
                .ifPresent(r -> reminderRepository.deleteById(r.getId()));
        }
        Reminder r = new Reminder();
        r.setMatchId(req.getMatchId());
        r.setEmail(req.getEmail());
        r.setDeviceId(req.getDeviceId());
        reminderRepository.save(r);
    }
}
