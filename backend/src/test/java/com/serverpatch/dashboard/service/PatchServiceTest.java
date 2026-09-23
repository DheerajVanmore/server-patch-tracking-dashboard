package com.serverpatch.dashboard.service;

import com.serverpatch.dashboard.dto.PatchDTO;
import com.serverpatch.dashboard.entity.Patch;
import com.serverpatch.dashboard.repository.PatchEventRepository;
import com.serverpatch.dashboard.repository.PatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PatchServiceTest {

    @Autowired
    private PatchService patchService;

    @Autowired
    private PatchRepository patchRepository;

    @Autowired
    private PatchEventRepository patchEventRepository;

    @BeforeEach
    void setUp() {
        patchEventRepository.deleteAll();
        patchRepository.deleteAll();
    }

    @Test
    void testCreatePatch() {
        PatchDTO dto = new PatchDTO();
        dto.setPatchName("Security Update 1");
        dto.setPatchIdentifier("KB123456");
        dto.setVersion("1.0");
        dto.setSeverity("CRITICAL");
        dto.setReleaseDate(LocalDate.now());

        Patch patch = patchService.createPatch(dto);
        assertNotNull(patch.getId());
        assertEquals("KB123456", patch.getPatchIdentifier());
    }

    @Test
    void testFilterBySeverity() {
        PatchDTO dto = new PatchDTO();
        dto.setPatchName("Minor Fix");
        dto.setSeverity("LOW");
        patchService.createPatch(dto);

        PatchDTO dto2 = new PatchDTO();
        dto2.setPatchName("Major Fix");
        dto2.setSeverity("CRITICAL");
        patchService.createPatch(dto2);

        List<Patch> criticalPatches = patchService.filterBySeverity("CRITICAL");
        assertEquals(1, criticalPatches.size());
        assertEquals("Major Fix", criticalPatches.get(0).getPatchName());
    }
}
