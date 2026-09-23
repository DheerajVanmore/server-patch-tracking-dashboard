package com.serverpatch.dashboard.service;

import com.serverpatch.dashboard.dto.AlertDTO;
import com.serverpatch.dashboard.dto.PatchDTO;
import com.serverpatch.dashboard.dto.PatchEventDTO;
import com.serverpatch.dashboard.dto.ServerDTO;
import com.serverpatch.dashboard.entity.PatchEvent;
import com.serverpatch.dashboard.repository.PatchEventRepository;
import com.serverpatch.dashboard.repository.PatchRepository;
import com.serverpatch.dashboard.repository.ServerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AlertServiceTest {

    @Autowired private AlertService alertService;
    @Autowired private ServerService serverService;
    @Autowired private PatchService patchService;
    @Autowired private PatchEventService patchEventService;
    @Autowired private PatchEventRepository patchEventRepository;
    @Autowired private ServerRepository serverRepository;
    @Autowired private PatchRepository patchRepository;

    private Long serverId;

    @BeforeEach
    void setUp() {
        patchEventRepository.deleteAll();
        patchRepository.deleteAll();
        serverRepository.deleteAll();

        ServerDTO s = new ServerDTO();
        s.setHostname("alerts-server"); s.setIpAddress("1.2.3.4");
        s.setOs("Linux"); s.setEnvironment("PROD"); s.setOwnerTeam("A");
        serverId = serverService.createServer(s).getId();
    }

    @Test
    void testAlertsForFailedEvents() {
        PatchDTO p = new PatchDTO(); p.setPatchName("p1"); p.setSeverity("LOW");
        Long pId = patchService.createPatch(p).getId();

        PatchEventDTO e = new PatchEventDTO();
        e.setServerId(serverId); e.setPatchId(pId); e.setStatus("FAILED"); e.setFailureReason("Error");
        patchEventService.createPatchEvent(e);

        List<AlertDTO> alerts = alertService.getAlerts();
        assertEquals(1, alerts.size());
        assertEquals("Failure", alerts.get(0).getAlertType());
    }

    @Test
    void testAlertsGeneratedForCriticalPending() {
        PatchDTO p = new PatchDTO(); p.setPatchName("p_crit"); p.setSeverity("CRITICAL");
        Long pId = patchService.createPatch(p).getId();

        PatchEventDTO e = new PatchEventDTO();
        e.setServerId(serverId); e.setPatchId(pId); e.setStatus("PENDING");
        PatchEvent event = patchEventService.createPatchEvent(e);
        
        // Simulate overdue
        event.setEventDate(LocalDateTime.now().minusDays(10));
        patchEventRepository.save(event);

        List<AlertDTO> alerts = alertService.getAlerts();
        assertEquals(1, alerts.size());
        assertEquals("Overdue", alerts.get(0).getAlertType());
        assertEquals("CRITICAL", alerts.get(0).getSeverity());
    }
}
