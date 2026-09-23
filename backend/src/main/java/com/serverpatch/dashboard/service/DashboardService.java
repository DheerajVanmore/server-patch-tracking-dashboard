package com.serverpatch.dashboard.service;

import com.serverpatch.dashboard.dto.DashboardSummary;
import com.serverpatch.dashboard.entity.PatchEvent;
import com.serverpatch.dashboard.repository.PatchEventRepository;
import com.serverpatch.dashboard.repository.ServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {
    @Autowired
    private ServerRepository serverRepository;
    @Autowired
    private PatchEventRepository patchEventRepository;

    public DashboardSummary getSummary() {
        DashboardSummary summary = new DashboardSummary();
        summary.setTotalServers(serverRepository.count());
        
        long patched = patchEventRepository.countByStatus("PATCHED");
        long pending = patchEventRepository.countByStatus("PENDING");
        long failed = patchEventRepository.countByStatus("FAILED");
        long exempted = patchEventRepository.countByStatus("EXEMPTED");
        long inProgress = patchEventRepository.countByStatus("IN_PROGRESS");
        
        summary.setPatched(patched);
        summary.setPending(pending);
        summary.setFailed(failed);
        summary.setExempted(exempted);
        summary.setInProgress(inProgress);
        
        long totalEvents = patched + pending + failed + exempted + inProgress;
        double compliance = totalEvents > 0 ? ((double) patched / totalEvents) * 100 : 0.0;
        summary.setCompliancePercentage(compliance);
        
        List<PatchEvent> recent = patchEventRepository.findTop10ByOrderByEventDateDesc();
        summary.setRecentEvents(new java.util.ArrayList<>(recent));
        
        return summary;
    }
}
