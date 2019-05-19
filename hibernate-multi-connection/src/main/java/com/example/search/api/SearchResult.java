package com.example.search.api;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SearchResult {
    private long interactionID;
    private long archiveID;
    private LocalDateTime contactGMTStartTime;
    private int userId;

    public SearchResult(long interactionID, long archiveID, LocalDateTime contactGMTStartTime, int userId) {
        this.interactionID = interactionID;
        this.archiveID = archiveID;
        this.contactGMTStartTime = contactGMTStartTime;
        this.userId = userId;
    }
}
