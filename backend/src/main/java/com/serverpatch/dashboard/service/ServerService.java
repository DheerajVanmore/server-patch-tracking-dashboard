package com.serverpatch.dashboard.service;

import com.serverpatch.dashboard.dto.ServerDTO;
import com.serverpatch.dashboard.entity.Server;
import com.serverpatch.dashboard.exception.ResourceNotFoundException;
import com.serverpatch.dashboard.repository.ServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerService {
    @Autowired
    private ServerRepository serverRepository;

    public List<Server> getAllServers() {
        return serverRepository.findAll();
    }

    public Server getServerById(Long id) {
        return serverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Server not found with id " + id));
    }

    public Server createServer(ServerDTO dto) {
        Server server = new Server();
        mapDtoToEntity(dto, server);
        return serverRepository.save(server);
    }

    public Server updateServer(Long id, ServerDTO dto) {
        Server server = getServerById(id);
        mapDtoToEntity(dto, server);
        return serverRepository.save(server);
    }

    public void deleteServer(Long id) {
        Server server = getServerById(id);
        serverRepository.delete(server);
    }

    public List<Server> searchServers(String query) {
        return serverRepository.findByHostnameContainingIgnoreCaseOrIpAddressContainingIgnoreCase(query, query);
    }

    public List<Server> filterServers(String environment, String status) {
        if (environment != null && status != null) {
            return serverRepository.findByEnvironmentAndStatus(environment, status);
        } else if (environment != null) {
            return serverRepository.findByEnvironment(environment);
        } else if (status != null) {
            return serverRepository.findByStatus(status);
        }
        return getAllServers();
    }

    private void mapDtoToEntity(ServerDTO dto, Server server) {
        server.setHostname(dto.getHostname());
        server.setIpAddress(dto.getIpAddress());
        server.setOs(dto.getOs());
        server.setEnvironment(dto.getEnvironment());
        server.setOwnerTeam(dto.getOwnerTeam());
        if (dto.getStatus() != null) {
            server.setStatus(dto.getStatus());
        }
        if (dto.getLastChecked() != null) {
            server.setLastChecked(dto.getLastChecked());
        }
    }
}
