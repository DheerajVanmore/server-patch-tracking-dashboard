package com.serverpatch.dashboard.service;

import com.serverpatch.dashboard.dto.AlertDTO;
import com.serverpatch.dashboard.entity.PatchEvent;
import com.serverpatch.dashboard.repository.PatchEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlertService {
    @Autowired
    private PatchEventRepository patchEventRepository;

    public List<AlertDTO> getAlerts() {
        List<AlertDTO> alerts = new ArrayList<>();
        List<PatchEvent> allEvents = patchEventRepository.findAll();

        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        for (PatchEvent event : allEvents) {
            String severity = event.getPatch().getSeverity();
            boolean isCriticalOrHigh = "CRITICAL".equalsIgnoreCase(severity) || "HIGH".equalsIgnoreCase(severity);

            if ("FAILED".equalsIgnoreCase(event.getStatus())) {
                alerts.add(new AlertDTO("Failure", "HIGH", event.getServer().getHostname(), 
                    event.getPatch().getPatchName(), "Patch installation failed: " + event.getFailureReason(), event.getEventDate()));
            } else if ("PENDING".equalsIgnoreCase(event.getStatus()) && isCriticalOrHigh) {
                if (event.getEventDate() != null && event.getEventDate().isBefore(sevenDaysAgo)) {
                    alerts.add(new AlertDTO("Overdue", "CRITICAL", event.getServer().getHostname(), 
                        event.getPatch().getPatchName(), "Critical/High patch pending for over 7 days", event.getEventDate()));
                } else {
                    alerts.add(new AlertDTO("Urgent", "HIGH", event.getServer().getHostname(), 
                        event.getPatch().getPatchName(), "Critical/High patch is pending", event.getEventDate()));
                }
            }
        }
        
        alerts.sort((a, b) -> {
            if (a.getDate() == null || b.getDate() == null) return 0;
            return b.getDate().compareTo(a.getDate());
        });
        
        return alerts;
    }
}
