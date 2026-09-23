package com.serverpatch.dashboard.dto;

import java.time.LocalDateTime;

public class AlertDTO {
    private String alertType;
    private String severity;
    private String serverHostname;
    private String patchName;
    private String message;
    private LocalDateTime date;

    public AlertDTO() {}

    public AlertDTO(String alertType, String severity, String serverHostname, String patchName, String message, LocalDateTime date) {
        this.alertType = alertType;
        this.severity = severity;
        this.serverHostname = serverHostname;
        this.patchName = patchName;
        this.message = message;
        this.date = date;
    }

    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getServerHostname() { return serverHostname; }
    public void setServerHostname(String serverHostname) { this.serverHostname = serverHostname; }
    public String getPatchName() { return patchName; }
    public void setPatchName(String patchName) { this.patchName = patchName; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
