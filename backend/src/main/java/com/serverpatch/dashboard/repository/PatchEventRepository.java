package com.serverpatch.dashboard.repository;

import com.serverpatch.dashboard.entity.PatchEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PatchEventRepository extends JpaRepository<PatchEvent, Long> {
    List<PatchEvent> findByServerId(Long serverId);
    List<PatchEvent> findByPatchId(Long patchId);
    List<PatchEvent> findByStatus(String status);
    long countByStatus(String status);
    List<PatchEvent> findByServerIdOrderByEventDateDesc(Long serverId);
    List<PatchEvent> findTop10ByOrderByEventDateDesc();
}
