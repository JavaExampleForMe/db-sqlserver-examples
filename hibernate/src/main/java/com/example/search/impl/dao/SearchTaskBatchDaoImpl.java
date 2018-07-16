package com.example.search.impl.dao;

import com.example.search.impl.api.BatchQueryDefinition;
import com.example.search.impl.api.SearchBatchStatus;
import com.example.search.impl.api.SearchBatchStatusEnum;
import com.example.search.impl.api.SearchTaskBatch;
import com.example.search.impl.model.SearchTaskBatchEntity;
import com.example.search.impl.api.QueryParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Slf4j
@EnableTransactionManagement
public class SearchTaskBatchDaoImpl implements SearchTaskBatchDao {

    @PersistenceContext(unitName = "search")
    private EntityManager entityManager;

    @PersistenceContext(unitName = "search2")
    private EntityManager entityManager2;

    private ObjectMapper objectMapper;

    public SearchTaskBatchDaoImpl(/*EntityManager entityManager, */ObjectMapper objectMapper) {
       // this.entityManager = entityManager;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional("searchTransactionManager")
    public int save(SearchTaskBatch searchTaskBatch) {
        final SearchTaskBatchEntity searchTaskBatchEntity = mapSearchTaskBatchToSearchTaskBatchEntity(searchTaskBatch);
        entityManager.merge(searchTaskBatchEntity);
        log.info("saved searchTaskBatch entry: {}", searchTaskBatchEntity.getBatchId());
        return searchTaskBatchEntity.getBatchId();
    }

    @Override
    @Transactional(value="searchTransactionManager" ,readOnly = true)
    public Set<SearchTaskBatch> getBatchesToExecute(int searchTaskId, SearchBatchStatusEnum batchStatusId, int numberOfBatches) {
        List<SearchTaskBatchEntity> searchTaskBarchListEntities = entityManager.createQuery("FROM SearchTaskBatchEntity t " +
                "WHERE batchStatusId = :batchStatusId AND searchTaskId = :searchTaskId order by batchId")
                .setParameter("batchStatusId", batchStatusId.getCode())
                .setParameter("searchTaskId", searchTaskId)
                .getResultList();
        Set searchTaskBatches = new HashSet<SearchTaskBatch>();
        for (SearchTaskBatchEntity searchTaskBatchEntity : searchTaskBarchListEntities) {
            SearchTaskBatch searchTaskBatch = mapSearchTaskBatchEntityToSearchTaskBatch(searchTaskBatchEntity);
            searchTaskBatches.add(searchTaskBatch);
        }
        return searchTaskBatches;
    }

    @Override
    @Transactional("searchTransactionManager")
    public int executeBatchAndUpdateStatus(SearchTaskBatch batch) {
        final int affectedRows = executeBatch(batch);
        batch.setBatchResultsNumRows(affectedRows);
        batch.setBatchStatus(new SearchBatchStatus(SearchBatchStatusEnum.DONE));
        save(batch);
        return affectedRows;
    }

    private int executeBatch(SearchTaskBatch batch) {
        Query query = getPrepareQuery(batch);
        return query.executeUpdate();
    }

    private Query getPrepareQuery(SearchTaskBatch batch) {
        BatchQueryDefinition batchQueryDefinition = batch.getBatchQueryDefinition();
        String stringQuery = batchQueryDefinition.getQuery()
                .replace("#SourceRequestId#", Integer.toString(batch.getCallerId()))
                .replace("#SearchTaskId#", Integer.toString(batch.getSearchTaskId()))
                .replace("#BatchId#", Integer.toString(batch.getBatchId()));
        Query query = entityManager.createNativeQuery(stringQuery);
        for (QueryParam param : batchQueryDefinition.getParams()) {
            switch (param.getType()) {
                case Date:
                    LocalDateTime dateParam;
                    dateParam = LocalDateTime.parse(param.getValue());
                    Date out = Date.from(dateParam.atZone(ZoneId.systemDefault()).toInstant());
                    query.setParameter(param.getName(), out, TemporalType.TIMESTAMP);
                    break;
                case String:
                    query.setParameter(param.getName(), param.getValue());
                    break;
            }
        }
        return query;
    }

    @SneakyThrows
    private SearchTaskBatchEntity mapSearchTaskBatchToSearchTaskBatchEntity(SearchTaskBatch searchTaskBatch) {
        SearchTaskBatchEntity searchTaskBatchEntity = new SearchTaskBatchEntity();
        searchTaskBatchEntity.setBatchId(searchTaskBatch.getBatchId());
        searchTaskBatchEntity.setSearchTaskId(searchTaskBatch.getSearchTaskId());
        searchTaskBatchEntity.setSourceRequestId(searchTaskBatch.getCallerId());
        searchTaskBatchEntity.setBatchStatusId(searchTaskBatch.getBatchStatus().getCode());
        searchTaskBatchEntity.setBatchResultsNumRows(searchTaskBatch.getBatchResultsNumRows());
        String batchDefinition = objectMapper.writeValueAsString(searchTaskBatch.getBatchQueryDefinition());
        searchTaskBatchEntity.setBatchQueryDefinition(batchDefinition);
        return searchTaskBatchEntity;
    }

    @SneakyThrows
    private SearchTaskBatch mapSearchTaskBatchEntityToSearchTaskBatch(SearchTaskBatchEntity searchTaskBatchEntity) {
        BatchQueryDefinition batchQueryDefinition = objectMapper.readValue(searchTaskBatchEntity.getBatchQueryDefinition(), BatchQueryDefinition.class);

        SearchTaskBatch searchTaskBatch = SearchTaskBatch.builder().
                callerId(searchTaskBatchEntity.getSourceRequestId()).
                batchId(searchTaskBatchEntity.getBatchId()).
                searchTaskId(searchTaskBatchEntity.getSearchTaskId()).
                batchStatus(new SearchBatchStatus(searchTaskBatchEntity.getBatchStatusId())).
                batchResultsNumRows(searchTaskBatchEntity.getBatchResultsNumRows()).
                batchQueryDefinition(batchQueryDefinition).
                build();
        return searchTaskBatch;
    }

}
