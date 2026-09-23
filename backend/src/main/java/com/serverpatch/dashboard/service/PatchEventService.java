package com.serverpatch.dashboard.service;

import com.serverpatch.dashboard.dto.PatchEventDTO;
import com.serverpatch.dashboard.entity.Patch;
import com.serverpatch.dashboard.entity.PatchEvent;
import com.serverpatch.dashboard.entity.Server;
import com.serverpatch.dashboard.exception.ResourceNotFoundException;
import com.serverpatch.dashboard.repository.PatchEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PatchEventService {
    @Autowired
    private PatchEventRepository patchEventRepository;
    @Autowired
    private ServerService serverService;
    @Autowired
    private PatchService patchService;

    public List<PatchEvent> getAllPatchEvents() {
        return patchEventRepository.findAll();
    }

    public PatchEvent getPatchEventById(Long id) {
        return patchEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PatchEvent not found with id " + id));
    }

    public List<PatchEvent> getEventsByServerId(Long serverId) {
        return patchEventRepository.findByServerId(serverId);
    }

    public List<PatchEvent> getEventsByStatus(String status) {
        return patchEventRepository.findByStatus(status);
    }

    public PatchEvent createPatchEvent(PatchEventDTO dto) {
        PatchEvent event = new PatchEvent();
        Server server = serverService.getServerById(dto.getServerId());
        Patch patch = patchService.getPatchById(dto.getPatchId());
        
        event.setServer(server);
        event.setPatch(patch);
        event.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDING");
        event.setEventDate(dto.getEventDate() != null ? dto.getEventDate() : LocalDateTime.now());
        event.setFailureReason(dto.getFailureReason());
        event.setRemarks(dto.getRemarks());
        
        validateState(event.getStatus(), event.getFailureReason());
        return patchEventRepository.save(event);
    }

    public PatchEvent updatePatchEvent(Long id, PatchEventDTO dto) {
        PatchEvent event = getPatchEventById(id);
        String oldStatus = event.getStatus();
        String newStatus = dto.getStatus();

        if (newStatus != null && !newStatus.equals(oldStatus)) {
            validateTransition(oldStatus, newStatus);
            event.setStatus(newStatus);
        }
        
        if (dto.getEventDate() != null) event.setEventDate(dto.getEventDate());
        if (dto.getFailureReason() != null) event.setFailureReason(dto.getFailureReason());
        if (dto.getRemarks() != null) event.setRemarks(dto.getRemarks());

        validateState(event.getStatus(), event.getFailureReason());
        return patchEventRepository.save(event);
    }

    private void validateTransition(String oldStatus, String newStatus) {
        boolean isValid = false;
        if ("PENDING".equals(oldStatus)) {
            if ("IN_PROGRESS".equals(newStatus) || "EXEMPTED".equals(newStatus)) isValid = true;
        } else if ("IN_PROGRESS".equals(oldStatus)) {
            if ("PATCHED".equals(newStatus) || "FAILED".equals(newStatus)) isValid = true;
        }

        if (!isValid) {
            throw new IllegalArgumentException("Invalid state transition from " + oldStatus + " to " + newStatus);
        }
    }

    private void validateState(String status, String failureReason) {
        if ("FAILED".equals(status) && (failureReason == null || failureReason.trim().isEmpty())) {
            throw new IllegalArgumentException("failure_reason is required when status is FAILED");
        }
    }
}
