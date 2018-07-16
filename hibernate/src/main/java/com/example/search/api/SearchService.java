package com.example.search.api;


import com.example.search.impl.api.SearchRequest;

import java.util.List;

public interface SearchService {

    SearchToken prepareSearch(SearchRequest request);

    int executeTask(SearchToken searchTaskId);

    int getProgress(int callerId);

    List<SearchResult> getSearchResults(int callerId);

    List<SearchResult> head(int callerId, int size);

}
