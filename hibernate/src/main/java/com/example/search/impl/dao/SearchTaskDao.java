package com.example.search.impl.dao;

import com.example.search.api.SearchToken;
import com.example.search.impl.api.SearchTask;
import com.example.search.impl.api.SearchTaskStatusEnum;

public interface SearchTaskDao {
    int save(SearchTask searchTask);
    SearchTask save(SearchToken searchTaskId);
    int getStatus(int callerId);
    void updateStatus(SearchToken searchTaskId, SearchTaskStatusEnum SearchTaskStatus, int progress);
}
