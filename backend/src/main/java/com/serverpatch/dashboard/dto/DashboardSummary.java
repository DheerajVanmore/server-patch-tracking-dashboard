package com.serverpatch.dashboard.dto;

import java.util.List;

public class DashboardSummary {
    private long totalServers;
    private long patched;
    private long pending;
    private long failed;
    private long exempted;
    private long inProgress;
    private double compliancePercentage;
    private List<Object> recentEvents;

    public long getTotalServers() { return totalServers; }
    public void setTotalServers(long totalServers) { this.totalServers = totalServers; }
    public long getPatched() { return patched; }
    public void setPatched(long patched) { this.patched = patched; }
    public long getPending() { return pending; }
    public void setPending(long pending) { this.pending = pending; }
    public long getFailed() { return failed; }
    public void setFailed(long failed) { this.failed = failed; }
    public long getExempted() { return exempted; }
    public void setExempted(long exempted) { this.exempted = exempted; }
    public long getInProgress() { return inProgress; }
    public void setInProgress(long inProgress) { this.inProgress = inProgress; }
    public double getCompliancePercentage() { return compliancePercentage; }
    public void setCompliancePercentage(double compliancePercentage) { this.compliancePercentage = compliancePercentage; }
    public List<Object> getRecentEvents() { return recentEvents; }
    public void setRecentEvents(List<Object> recentEvents) { this.recentEvents = recentEvents; }
}
