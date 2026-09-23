package com.serverpatch.dashboard.repository;

import com.serverpatch.dashboard.entity.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServerRepository extends JpaRepository<Server, Long> {
    List<Server> findByHostnameContainingIgnoreCaseOrIpAddressContainingIgnoreCase(String hostname, String ip);
    List<Server> findByEnvironment(String env);
    List<Server> findByStatus(String status);
    List<Server> findByEnvironmentAndStatus(String env, String status);
}
