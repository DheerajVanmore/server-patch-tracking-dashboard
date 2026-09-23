package com.serverpatch.dashboard.repository;

import com.serverpatch.dashboard.entity.Patch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PatchRepository extends JpaRepository<Patch, Long> {
    List<Patch> findByPatchNameContainingIgnoreCaseOrPatchIdentifierContainingIgnoreCase(String name, String id);
    List<Patch> findBySeverity(String severity);
}
