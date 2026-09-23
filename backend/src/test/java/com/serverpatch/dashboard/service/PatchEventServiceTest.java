package com.serverpatch.dashboard.service;

import com.serverpatch.dashboard.dto.PatchDTO;
import com.serverpatch.dashboard.dto.PatchEventDTO;
import com.serverpatch.dashboard.dto.ServerDTO;
import com.serverpatch.dashboard.entity.Patch;
import com.serverpatch.dashboard.entity.PatchEvent;
import com.serverpatch.dashboard.entity.Server;
import com.serverpatch.dashboard.repository.PatchEventRepository;
import com.serverpatch.dashboard.repository.PatchRepository;
import com.serverpatch.dashboard.repository.ServerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PatchEventServiceTest {

    @Autowired
    private PatchEventService patchEventService;
    @Autowired
    private ServerService serverService;
    @Autowired
    private PatchService patchService;
    
    @Autowired
    private PatchEventRepository patchEventRepository;
    @Autowired
    private ServerRepository serverRepository;
    @Autowired
    private PatchRepository patchRepository;

    private Long serverId;
    private Long patchId;

    @BeforeEach
    void setUp() {
        patchEventRepository.deleteAll();
        serverRepository.deleteAll();
        patchRepository.deleteAll();

        ServerDTO sDto = new ServerDTO();
        sDto.setHostname("srv1");
        sDto.setIpAddress("1.1.1.1");
        sDto.setOs("Linux");
        sDto.setEnvironment("DEV");
        sDto.setOwnerTeam("TeamA");
        Server s = serverService.createServer(sDto);
        serverId = s.getId();

        PatchDTO pDto = new PatchDTO();
        pDto.setPatchName("P1");
        pDto.setSeverity("HIGH");
        Patch p = patchService.createPatch(pDto);
        patchId = p.getId();
    }

    @Test
    void testCreateEvent() {
        PatchEventDTO eDto = new PatchEventDTO();
        eDto.setServerId(serverId);
        eDto.setPatchId(patchId);
        eDto.setStatus("PENDING");
        
        PatchEvent event = patchEventService.createPatchEvent(eDto);
        assertNotNull(event.getId());
        assertEquals("PENDING", event.getStatus());
    }

    @Test
    void testValidStateTransition() {
        PatchEventDTO eDto = new PatchEventDTO();
        eDto.setServerId(serverId);
        eDto.setPatchId(patchId);
        eDto.setStatus("PENDING");
        PatchEvent event = patchEventService.createPatchEvent(eDto);

        PatchEventDTO updateDto = new PatchEventDTO();
        updateDto.setStatus("IN_PROGRESS");
        PatchEvent updated = patchEventService.updatePatchEvent(event.getId(), updateDto);
        assertEquals("IN_PROGRESS", updated.getStatus());
        
        updateDto.setStatus("PATCHED");
        updated = patchEventService.updatePatchEvent(event.getId(), updateDto);
        assertEquals("PATCHED", updated.getStatus());
    }

    @Test
    void testInvalidStateTransitionRejected() {
        PatchEventDTO eDto = new PatchEventDTO();
        eDto.setServerId(serverId);
        eDto.setPatchId(patchId);
        eDto.setStatus("PENDING");
        PatchEvent event = patchEventService.createPatchEvent(eDto);

        PatchEventDTO updateDto = new PatchEventDTO();
        updateDto.setStatus("PATCHED"); // PENDING -> PATCHED is invalid
        
        assertThrows(IllegalArgumentException.class, () -> {
            patchEventService.updatePatchEvent(event.getId(), updateDto);
        });
    }

    @Test
    void testFailureReasonRequiredForFailed() {
        PatchEventDTO eDto = new PatchEventDTO();
        eDto.setServerId(serverId);
        eDto.setPatchId(patchId);
        eDto.setStatus("PENDING");
        PatchEvent event = patchEventService.createPatchEvent(eDto);

        PatchEventDTO updateDto = new PatchEventDTO();
        updateDto.setStatus("IN_PROGRESS");
        patchEventService.updatePatchEvent(event.getId(), updateDto);

        PatchEventDTO failDto = new PatchEventDTO();
        failDto.setStatus("FAILED");
        // No failure reason set
        assertThrows(IllegalArgumentException.class, () -> {
            patchEventService.updatePatchEvent(event.getId(), failDto);
        });
        
        failDto.setFailureReason("Disk full");
        PatchEvent failed = patchEventService.updatePatchEvent(event.getId(), failDto);
        assertEquals("FAILED", failed.getStatus());
    }
}
