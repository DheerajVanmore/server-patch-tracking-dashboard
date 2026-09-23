package com.serverpatch.dashboard.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class ServerDTO {
    private Long id;
    @NotBlank
    private String hostname;
    @NotBlank
    private String ipAddress;
    @NotBlank
    private String os;
    @NotBlank
    private String environment;
    @NotBlank
    private String ownerTeam;
    private String status;
    private LocalDateTime lastChecked;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getHostname() { return hostname; }
    public void setHostname(String hostname) { this.hostname = hostname; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getOs() { return os; }
    public void setOs(String os) { this.os = os; }
    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }
    public String getOwnerTeam() { return ownerTeam; }
    public void setOwnerTeam(String ownerTeam) { this.ownerTeam = ownerTeam; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getLastChecked() { return lastChecked; }
    public void setLastChecked(LocalDateTime lastChecked) { this.lastChecked = lastChecked; }
}
