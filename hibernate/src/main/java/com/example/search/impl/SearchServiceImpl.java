package com.example.search.impl;

import com.example.search.api.SearchResult;
import com.example.search.api.SearchService;
import com.example.search.api.SearchToken;
import com.example.search.impl.api.*;
import com.example.search.impl.dao.SearchResultDao;
import com.example.search.impl.dao.SearchTaskBatchDao;
import com.example.search.impl.dao.SearchTaskDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
@Slf4j
public class SearchServiceImpl implements SearchService {

    private final String query = "INSERT INTO SearchResults  select #SourceRequestId#, #SearchTaskId#, #BatchId# ,[iInteractionID] ,[dtContactGMTStartTime]  ,[iInitiatorUserID] ,[iArchiveID], GETDATE() CreationDate,GETDATE() ModifyDate FROM [dbo].vwProduceResults rh " +
            "INNER JOIN [fn_intlist_to_tbl] (:userList) gl ON rh.[iInitiatorUserID]=gl.number ";
    private final String queryWhereClause = " WHERE rh.[dtContactGMTStartTime] >= :startPeriodDate " +
            "AND rh.[dtContactGMTStartTime] <= :stopPeriodDate ";

    private SearchTaskDao taskDao;
    private SearchTaskBatchDao batchDao;
    private SearchResultDao searchResultDao;

    public SearchServiceImpl(SearchTaskDao taskDao, SearchTaskBatchDao batchDao, SearchResultDao searchResultDao ) {
        this.taskDao = taskDao;
        this.batchDao = batchDao;
        this.searchResultDao = searchResultDao;
    }


    @Override
    @Transactional("searchTransactionManager")
    public SearchToken prepareSearch(SearchRequest request) {
        log.info("PrepareSearch request for '{}'", request.getSearchFilter().toString());
        SearchTask searchTask = SearchTask.builder ().
                callerId(request.getCallerId()).
                searchTaskStatus(new SearchTaskStatus(SearchTaskStatusEnum.SCHEDULED)).
                searchTaskProgress(0).
                searchFilter(request.getSearchFilter()).build();

        searchTask.setSearchTaskId(taskDao.save(searchTask));

        LocalDateTime currFromDateTime = request.getSearchFilter().getFromDate();
        // Loop over the months and create the batches
        while (currFromDateTime.isBefore(request.getSearchFilter().getToDate())) {
            LocalDateTime firstDayNextMonth = getFirstDayNextMonth(currFromDateTime);
            LocalDateTime currToDateTime = getCurrToDateTime(request.getSearchFilter().getToDate(), firstDayNextMonth);
            saveTaskBatch(searchTask , currFromDateTime, currToDateTime);
            currFromDateTime = firstDayNextMonth;
        }

        SearchToken response = new SearchToken();
        response.setSearchTaskId(searchTask.getSearchTaskId());

        return response;
    }

    @Override
    public int executeTask(SearchToken searchTaskId) {
        int totalNumberRowsAffected = 0 ;
        int numberExecutedBatches = 0 ;
        log.info("Execute Search taskId '{}' ", searchTaskId);
        Set<SearchTaskBatch> batches = batchDao.getBatchesToExecute(searchTaskId.getSearchTaskId(), SearchBatchStatusEnum.SCHEDULED, 2);
        int totalBatches = batches.size();
        taskDao.updateStatus(searchTaskId, SearchTaskStatusEnum.RUNNING,0);
        for (SearchTaskBatch batch : batches) {
            startExecuteSearchBatch(batch);
            final int numberRowsAffected = batchDao.executeBatchAndUpdateStatus(batch);
            totalNumberRowsAffected += numberRowsAffected;
            numberExecutedBatches++;
            taskDao.updateStatus(searchTaskId, SearchTaskStatusEnum.RUNNING,numberExecutedBatches*100/totalBatches);
        }
        taskDao.updateStatus(searchTaskId, SearchTaskStatusEnum.COMPLETED,100);
        return totalNumberRowsAffected;
    }

    @Override
    public List<SearchResult> getSearchResults(int callerId) {
        return searchResultDao.getResults(callerId);
    }

    @Override
    public int getProgress(int callerId){
        return 1;
    }


    @Override
    public List<SearchResult> head(int callerId, int size){
        return Arrays.asList();
    }

    private LocalDateTime getCurrToDateTime(LocalDateTime requestedToDateTime, LocalDateTime firstDayNextMonth) {
        LocalDateTime currToDateTime;
        if (firstDayNextMonth.isBefore(requestedToDateTime)){
            currToDateTime = firstDayNextMonth;
        }
        else{
            currToDateTime = requestedToDateTime;
        }
        return currToDateTime;
    }

    private LocalDateTime getFirstDayNextMonth(LocalDateTime currFromDateTime) {
        return currFromDateTime.plusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    private void saveTaskBatch(SearchTask searchTask, LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        // calculate from and to date
        BatchQueryDefinition batchQueryDefinition = getBatchQueryDefinition(searchTask, fromDateTime, toDateTime);
        SearchTaskBatch searchTaskBatch = SearchTaskBatch.builder ().
                callerId(searchTask.getCallerId()).
                searchTaskId(searchTask.getSearchTaskId()).
                batchStatus(new SearchBatchStatus(SearchBatchStatusEnum.SCHEDULED)).
                batchQueryDefinition(batchQueryDefinition).
                build();
        log.info("Saving searchTaskBatch '{}'", searchTaskBatch.toString());
        batchDao.save(searchTaskBatch);
    }

    private BatchQueryDefinition getBatchQueryDefinition(SearchTask searchTask, LocalDateTime fromDate, LocalDateTime toDate) {
        String userIds = intArrayToString(searchTask.getSearchFilter().getUserIds());
        QueryParam userListParam = new QueryParam("userList", userIds, QueryParamType.String);
        QueryParam stopPeriodParam = new QueryParam("stopPeriodDate", toDate.toString(),QueryParamType.Date);
        QueryParam startPeriodParam = new QueryParam("startPeriodDate", fromDate.toString(),QueryParamType.Date);
        QueryParam[] queryParams = {userListParam, startPeriodParam, stopPeriodParam};
        String fullQuery = query;
        BatchQueryDefinition batchQueryDefinition = new BatchQueryDefinition();
        batchQueryDefinition.setParams(queryParams);
        batchQueryDefinition.setQuery(fullQuery + queryWhereClause);
        return batchQueryDefinition;
    }

    private QueryParam[] addParam(QueryParam[] queryParams, QueryParam mediaTypeIdParam) {
        QueryParam[] tempArray = new QueryParam[ queryParams.length + 1];
        System.arraycopy(queryParams, 0, tempArray, 0, queryParams.length);
        tempArray[queryParams.length] = mediaTypeIdParam;
        return tempArray;
    }

    private String intArrayToString(int[] userIds) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < userIds.length; i++) {
            strBuilder.append(userIds[i]);
            strBuilder.append(",");
        }
        String s = strBuilder.toString();
        return s.substring(0,s.length()-1);
    }

    private void startExecuteSearchBatch(SearchTaskBatch batch) {
        batch.setBatchStatus(new SearchBatchStatus(SearchBatchStatusEnum.RUNNING));
        batchDao.save(batch);
    }
}