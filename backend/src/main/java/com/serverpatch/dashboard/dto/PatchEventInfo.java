package com.serverpatch.dashboard.dto;

import java.time.LocalDateTime;

public class PatchEventInfo {
    private String patchName;
    private String patchIdentifier;
    private String severity;
    private String status;
    private LocalDateTime eventDate;
    private String failureReason;
    private String remarks;

    public String getPatchName() { return patchName; }
    public void setPatchName(String patchName) { this.patchName = patchName; }
    public String getPatchIdentifier() { return patchIdentifier; }
    public void setPatchIdentifier(String patchIdentifier) { this.patchIdentifier = patchIdentifier; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getEventDate() { return eventDate; }
    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
