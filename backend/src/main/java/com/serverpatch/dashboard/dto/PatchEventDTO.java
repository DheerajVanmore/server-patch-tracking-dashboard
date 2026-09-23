package com.serverpatch.dashboard.dto;

import java.time.LocalDateTime;

public class PatchEventDTO {
    private Long id;
    private Long serverId;
    private Long patchId;
    private String status;
    private LocalDateTime eventDate;
    private String failureReason;
    private String remarks;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getServerId() { return serverId; }
    public void setServerId(Long serverId) { this.serverId = serverId; }
    public Long getPatchId() { return patchId; }
    public void setPatchId(Long patchId) { this.patchId = patchId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getEventDate() { return eventDate; }
    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
