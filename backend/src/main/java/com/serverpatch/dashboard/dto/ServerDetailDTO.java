package com.serverpatch.dashboard.dto;

import java.util.List;

public class ServerDetailDTO extends ServerDTO {
    private List<PatchEventInfo> patchHistory;

    public List<PatchEventInfo> getPatchHistory() {
        return patchHistory;
    }

    public void setPatchHistory(List<PatchEventInfo> patchHistory) {
        this.patchHistory = patchHistory;
    }
}
