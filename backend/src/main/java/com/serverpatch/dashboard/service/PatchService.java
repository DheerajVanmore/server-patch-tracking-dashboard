package com.serverpatch.dashboard.service;

import com.serverpatch.dashboard.dto.PatchDTO;
import com.serverpatch.dashboard.entity.Patch;
import com.serverpatch.dashboard.exception.ResourceNotFoundException;
import com.serverpatch.dashboard.repository.PatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatchService {
    @Autowired
    private PatchRepository patchRepository;

    public List<Patch> getAllPatches() {
        return patchRepository.findAll();
    }

    public Patch getPatchById(Long id) {
        return patchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patch not found with id " + id));
    }

    public Patch createPatch(PatchDTO dto) {
        Patch patch = new Patch();
        mapDtoToEntity(dto, patch);
        return patchRepository.save(patch);
    }

    public Patch updatePatch(Long id, PatchDTO dto) {
        Patch patch = getPatchById(id);
        mapDtoToEntity(dto, patch);
        return patchRepository.save(patch);
    }

    public void deletePatch(Long id) {
        Patch patch = getPatchById(id);
        patchRepository.delete(patch);
    }

    public List<Patch> searchPatches(String query) {
        return patchRepository.findByPatchNameContainingIgnoreCaseOrPatchIdentifierContainingIgnoreCase(query, query);
    }

    public List<Patch> filterBySeverity(String severity) {
        return patchRepository.findBySeverity(severity);
    }

    private void mapDtoToEntity(PatchDTO dto, Patch patch) {
        patch.setPatchName(dto.getPatchName());
        patch.setPatchIdentifier(dto.getPatchIdentifier());
        patch.setVersion(dto.getVersion());
        patch.setReleaseDate(dto.getReleaseDate());
        patch.setSeverity(dto.getSeverity());
        patch.setDescription(dto.getDescription());
    }
}
