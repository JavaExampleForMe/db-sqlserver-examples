package com.example.search.impl.dao;

import com.example.search.api.SearchResult;

import java.util.List;

public interface SearchResultDao {
    public List<SearchResult> getResults(int callerId);
}
