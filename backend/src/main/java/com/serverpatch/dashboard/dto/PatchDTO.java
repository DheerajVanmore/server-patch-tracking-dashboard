package com.serverpatch.dashboard.dto;

import java.time.LocalDate;

public class PatchDTO {
    private Long id;
    private String patchName;
    private String patchIdentifier;
    private String version;
    private LocalDate releaseDate;
    private String severity;
    private String description;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPatchName() { return patchName; }
    public void setPatchName(String patchName) { this.patchName = patchName; }
    public String getPatchIdentifier() { return patchIdentifier; }
    public void setPatchIdentifier(String patchIdentifier) { this.patchIdentifier = patchIdentifier; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
