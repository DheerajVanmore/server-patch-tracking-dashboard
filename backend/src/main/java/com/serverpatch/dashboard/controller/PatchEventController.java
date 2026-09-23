package com.serverpatch.dashboard.controller;

import com.serverpatch.dashboard.dto.PatchEventDTO;
import com.serverpatch.dashboard.entity.PatchEvent;
import com.serverpatch.dashboard.service.PatchEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patch-events")
public class PatchEventController {

    @Autowired
    private PatchEventService patchEventService;

    @GetMapping
    public ResponseEntity<List<PatchEvent>> getPatchEvents(
            @RequestParam(required = false) Long serverId,
            @RequestParam(required = false) String status) {
        
        if (serverId != null) {
            return ResponseEntity.ok(patchEventService.getEventsByServerId(serverId));
        } else if (status != null && !status.isEmpty()) {
            return ResponseEntity.ok(patchEventService.getEventsByStatus(status));
        }
        return ResponseEntity.ok(patchEventService.getAllPatchEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatchEvent> getPatchEventById(@PathVariable Long id) {
        return ResponseEntity.ok(patchEventService.getPatchEventById(id));
    }

    @PostMapping
    public ResponseEntity<PatchEvent> createPatchEvent(@RequestBody PatchEventDTO dto) {
        return ResponseEntity.ok(patchEventService.createPatchEvent(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatchEvent> updatePatchEvent(@PathVariable Long id, @RequestBody PatchEventDTO dto) {
        return ResponseEntity.ok(patchEventService.updatePatchEvent(id, dto));
    }
}
