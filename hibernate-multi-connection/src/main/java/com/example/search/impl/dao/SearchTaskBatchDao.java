package com.example.search.impl.dao;

import com.example.search.impl.api.SearchBatchStatusEnum;
import com.example.search.impl.api.SearchTaskBatch;

import java.util.Set;

public interface SearchTaskBatchDao {
    int save(SearchTaskBatch searchTaskBatch);
    int executeBatchAndUpdateStatus(SearchTaskBatch batch);
    Set<SearchTaskBatch> getBatchesToExecute(int searchTaskId, SearchBatchStatusEnum batchStatusId, int numberOfBatches);
}
