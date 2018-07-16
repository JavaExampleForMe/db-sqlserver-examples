package com.example.search.impl.api;

import com.example.search.api.SearchFilter;
import lombok.*;

@Builder
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class SearchTask {
    private int searchTaskId;
    private int callerId;
    private SearchTaskStatus searchTaskStatus;
    private int searchTaskProgress;
    private SearchFilter searchFilter;
}
