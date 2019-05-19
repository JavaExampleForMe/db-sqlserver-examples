package com.example.search.impl.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SearchTaskBatch {
    private int batchId;
    private int searchTaskId;
    private int callerId;
    private SearchBatchStatus batchStatus;
    private int batchResultsNumRows;
    private BatchQueryDefinition batchQueryDefinition;
}
