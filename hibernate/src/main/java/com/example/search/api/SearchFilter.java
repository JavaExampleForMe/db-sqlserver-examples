package com.example.search.api;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
public class SearchFilter {
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private int[] userIds;
    private int[] mediaTypeIds;
}
