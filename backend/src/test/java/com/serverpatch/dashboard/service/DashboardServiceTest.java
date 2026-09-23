package com.serverpatch.dashboard.service;

import com.serverpatch.dashboard.dto.DashboardSummary;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class DashboardServiceTest {

    @Autowired private DashboardService dashboardService;
    @Autowired private ServerService serverService;
    @Autowired private PatchService patchService;
    @Autowired private PatchEventService patchEventService;
    
    @Autowired private ServerRepository serverRepository;
    @Autowired private PatchRepository patchRepository;
    @Autowired private PatchEventRepository patchEventRepository;

    @BeforeEach
    void setUp() {
        patchEventRepository.deleteAll();
        serverRepository.deleteAll();
        patchRepository.deleteAll();
    }

    @Test
    void testZeroEvents() {
        DashboardSummary summary = dashboardService.getSummary();
        assertEquals(0, summary.getTotalServers());
        assertEquals(0.0, summary.getCompliancePercentage());
        assertTrue(summary.getRecentEvents().isEmpty());
    }

    @Test
    void testSummaryCalculationAndCompliance() {
        ServerDTO sDto = new ServerDTO();
        sDto.setHostname("s1"); sDto.setIpAddress("1.1.1.1");
        sDto.setOs("Linux"); sDto.setEnvironment("DEV"); sDto.setOwnerTeam("T1");
        Long sId = serverService.createServer(sDto).getId();

        PatchDTO pDto = new PatchDTO();
        pDto.setPatchName("P1");
        Long pId = patchService.createPatch(pDto).getId();

        PatchEventDTO e1 = new PatchEventDTO();
        e1.setServerId(sId); e1.setPatchId(pId); e1.setStatus("PATCHED");
        patchEventService.createPatchEvent(e1);

        PatchEventDTO e2 = new PatchEventDTO();
        e2.setServerId(sId); e2.setPatchId(pId); e2.setStatus("PENDING");
        patchEventService.createPatchEvent(e2);

        DashboardSummary summary = dashboardService.getSummary();
        assertEquals(1, summary.getTotalServers());
        assertEquals(1, summary.getPatched());
        assertEquals(1, summary.getPending());
        assertEquals(50.0, summary.getCompliancePercentage());
        assertEquals(2, summary.getRecentEvents().size());
    }
}
