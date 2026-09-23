package com.serverpatch.dashboard.controller;

import com.serverpatch.dashboard.dto.PatchEventInfo;
import com.serverpatch.dashboard.dto.ServerDTO;
import com.serverpatch.dashboard.dto.ServerDetailDTO;
import com.serverpatch.dashboard.entity.PatchEvent;
import com.serverpatch.dashboard.entity.Server;
import com.serverpatch.dashboard.service.PatchEventService;
import com.serverpatch.dashboard.service.ServerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/servers")
public class ServerController {

    @Autowired
    private ServerService serverService;

    @Autowired
    private PatchEventService patchEventService;

    @GetMapping
    public ResponseEntity<List<Server>> getServers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String environment,
            @RequestParam(required = false) String status) {
        
        if (search != null && !search.isEmpty()) {
            return ResponseEntity.ok(serverService.searchServers(search));
        } else if (environment != null || status != null) {
            return ResponseEntity.ok(serverService.filterServers(environment, status));
        }
        return ResponseEntity.ok(serverService.getAllServers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServerDetailDTO> getServerById(@PathVariable Long id) {
        Server server = serverService.getServerById(id);
        List<PatchEvent> events = patchEventService.getEventsByServerId(id);

        ServerDetailDTO detail = new ServerDetailDTO();
        detail.setId(server.getId());
        detail.setHostname(server.getHostname());
        detail.setIpAddress(server.getIpAddress());
        detail.setOs(server.getOs());
        detail.setEnvironment(server.getEnvironment());
        detail.setOwnerTeam(server.getOwnerTeam());
        detail.setStatus(server.getStatus());
        detail.setLastChecked(server.getLastChecked());

        List<PatchEventInfo> history = events.stream().map(e -> {
            PatchEventInfo info = new PatchEventInfo();
            info.setPatchName(e.getPatch().getPatchName());
            info.setPatchIdentifier(e.getPatch().getPatchIdentifier());
            info.setSeverity(e.getPatch().getSeverity());
            info.setStatus(e.getStatus());
            info.setEventDate(e.getEventDate());
            info.setFailureReason(e.getFailureReason());
            info.setRemarks(e.getRemarks());
            return info;
        }).collect(Collectors.toList());

        detail.setPatchHistory(history);
        return ResponseEntity.ok(detail);
    }

    @PostMapping
    public ResponseEntity<Server> createServer(@Valid @RequestBody ServerDTO dto) {
        return ResponseEntity.ok(serverService.createServer(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Server> updateServer(@PathVariable Long id, @Valid @RequestBody ServerDTO dto) {
        return ResponseEntity.ok(serverService.updateServer(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServer(@PathVariable Long id) {
        serverService.deleteServer(id);
        return ResponseEntity.noContent().build();
    }
}
