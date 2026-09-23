package com.serverpatch.dashboard.service;

import com.serverpatch.dashboard.dto.ServerDTO;
import com.serverpatch.dashboard.entity.Server;
import com.serverpatch.dashboard.repository.PatchEventRepository;
import com.serverpatch.dashboard.repository.ServerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ServerServiceTest {

    @Autowired
    private ServerService serverService;

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private PatchEventRepository patchEventRepository;

    @BeforeEach
    void setUp() {
        patchEventRepository.deleteAll();
        serverRepository.deleteAll();
    }

    @Test
    void testCreateServer() {
        ServerDTO dto = new ServerDTO();
        dto.setHostname("srv-web-01");
        dto.setIpAddress("192.168.1.10");
        dto.setOs("Ubuntu 22.04");
        dto.setEnvironment("PROD");
        dto.setOwnerTeam("DevOps");
        
        Server server = serverService.createServer(dto);
        assertNotNull(server.getId());
        assertEquals("srv-web-01", server.getHostname());
        assertEquals("ACTIVE", server.getStatus());
    }

    @Test
    void testGetServerById() {
        ServerDTO dto = new ServerDTO();
        dto.setHostname("srv-web-02");
        dto.setIpAddress("192.168.1.11");
        dto.setOs("Ubuntu");
        dto.setEnvironment("UAT");
        dto.setOwnerTeam("QA");
        Server saved = serverService.createServer(dto);

        Server found = serverService.getServerById(saved.getId());
        assertEquals(saved.getId(), found.getId());
    }

    @Test
    void testSearchServers() {
        ServerDTO dto = new ServerDTO();
        dto.setHostname("db-server-main");
        dto.setIpAddress("10.0.0.5");
        dto.setOs("RHEL");
        dto.setEnvironment("PROD");
        dto.setOwnerTeam("DBA");
        serverService.createServer(dto);

        List<Server> found = serverService.searchServers("db-server");
        assertFalse(found.isEmpty());
        assertEquals("db-server-main", found.get(0).getHostname());
    }
}
