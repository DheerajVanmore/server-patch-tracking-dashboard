package com.serverpatch.dashboard.controller;

import com.serverpatch.dashboard.dto.PatchDTO;
import com.serverpatch.dashboard.entity.Patch;
import com.serverpatch.dashboard.service.PatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patches")
public class PatchController {

    @Autowired
    private PatchService patchService;

    @GetMapping
    public ResponseEntity<List<Patch>> getPatches(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String severity) {
        
        if (search != null && !search.isEmpty()) {
            return ResponseEntity.ok(patchService.searchPatches(search));
        } else if (severity != null && !severity.isEmpty()) {
            return ResponseEntity.ok(patchService.filterBySeverity(severity));
        }
        return ResponseEntity.ok(patchService.getAllPatches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patch> getPatchById(@PathVariable Long id) {
        return ResponseEntity.ok(patchService.getPatchById(id));
    }

    @PostMapping
    public ResponseEntity<Patch> createPatch(@RequestBody PatchDTO dto) {
        return ResponseEntity.ok(patchService.createPatch(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patch> updatePatch(@PathVariable Long id, @RequestBody PatchDTO dto) {
        return ResponseEntity.ok(patchService.updatePatch(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatch(@PathVariable Long id) {
        patchService.deletePatch(id);
        return ResponseEntity.noContent().build();
    }
}
