package com.example.search.impl.dao;

import com.example.search.api.SearchToken;
import com.example.search.impl.api.SearchTask;
import com.example.search.impl.api.SearchTaskStatus;
import com.example.search.impl.api.SearchTaskStatusEnum;
import com.example.search.impl.exceptions.MutiSearchException;
import com.example.search.api.SearchFilter;
import com.example.search.impl.exceptions.SourceRequestNotFoundException;
import com.example.search.impl.model.SearchTaskEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@EnableTransactionManagement
@Slf4j
public class SearchTaskDaoImpl implements SearchTaskDao {

    private ObjectMapper objectMapper;

    private SearchTaskBatchDao batchDao;

    @PersistenceContext(unitName = "search")
    private EntityManager entityManager;
    @PersistenceContext(unitName = "search2")
    private EntityManager entityManager2;

    public SearchTaskDaoImpl(/*EntityManager entityManager, */ObjectMapper objectMapper, SearchTaskBatchDao batchDao) {
        /*this.entityManager = entityManager;*/
        this.objectMapper = objectMapper;
        this.batchDao = batchDao;
    }

    @Override
    @Transactional("searchTransactionManager")
    public int save(SearchTask searchTask) {
        SearchTaskEntity searchTaskEntity = getSearchTaskEntity(searchTask.getCallerId());
        if (searchTaskEntity != null ){
            if (searchTaskEntity.getSearchTaskStatusId() == SearchTaskStatusEnum.RUNNING.getCode()) {
                throw new MutiSearchException("can't execute SearchTask while seach is executed");
            }
            entityManager.remove(searchTaskEntity);
            entityManager.flush();
        }
        searchTaskEntity = mapSearchTaskToSearchTaskEntity(searchTask);
        entityManager.persist(searchTaskEntity);

        // log.info("saved prepareSearch request entry: {}", searchTaskEntity.getSearchTaskId());
        return searchTaskEntity.getSearchTaskId();
    }

    @Override
    public SearchTask save(SearchToken searchTaskId) {
        SearchTaskEntity searchTaskEntity = getSearchTaskEntity(searchTaskId);
        return mapSearchTaskEntityToSearchTask(searchTaskEntity);
    }

    private SearchTaskEntity getSearchTaskEntity(SearchToken searchTaskId) {
        log.info("Searching for TaskId {}",searchTaskId.getSearchTaskId());
        SearchTaskEntity searchTaskEntity = entityManager.find(SearchTaskEntity.class, searchTaskId.getSearchTaskId());
        return searchTaskEntity;
    }

    @Override
    @Transactional("searchTransactionManager")
    public void updateStatus(SearchToken searchTaskId, SearchTaskStatusEnum SearchTaskStatus, int progress) {
        SearchTaskEntity searchTaskEntity = getSearchTaskEntity(searchTaskId);
        searchTaskEntity.setSearchTaskProgress(progress);
        searchTaskEntity.setSearchTaskStatusId(SearchTaskStatus.getCode());
        entityManager.merge(searchTaskEntity);
    }


    @Override
    @Transactional(value ="searchTransactionManager", readOnly = true)
    public int getStatus(int callerId) {
        SearchTask searchTask = findSearchTask(callerId);
        return searchTask.getSearchTaskStatus().getCode();
    }


    private SearchTask findSearchTask(int callerId) {
        SearchTaskEntity searchTaskEntity = getSearchTaskEntity(callerId);
        if (searchTaskEntity == null){
            throw new SourceRequestNotFoundException(callerId);
        }
        SearchTask searchTask = mapSearchTaskEntityToSearchTask(searchTaskEntity);
        return searchTask;
    }

    private SearchTaskEntity getSearchTaskEntity(int callerId) {
        try {
            return (SearchTaskEntity) entityManager.createQuery("FROM SearchTaskEntity t " +
                    "WHERE sourceRequestTypeId = :sourceRequestTypeId AND sourceRequestId = :sourceRequestId")
                     .setParameter("sourceRequestId", callerId)
                    .getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }

    @SneakyThrows
    private SearchTaskEntity mapSearchTaskToSearchTaskEntity(SearchTask searchTask) {
        SearchTaskEntity searchTaskEntity = new SearchTaskEntity();
        String searchTaskDefinition = objectMapper.writeValueAsString(searchTask.getSearchFilter());
        searchTaskEntity.setSearchTaskDefinition(searchTaskDefinition);
        searchTaskEntity.setSourceRequestId(searchTask.getCallerId());
        searchTaskEntity.setSearchTaskProgress(searchTask.getSearchTaskProgress());
        searchTaskEntity.setSearchTaskStatusId(searchTask.getSearchTaskStatus().getCode());
        return searchTaskEntity;
    }

    @SneakyThrows
    private SearchTask mapSearchTaskEntityToSearchTask(SearchTaskEntity searchTaskEntity) {
        SearchFilter searchFilter = objectMapper.readValue(searchTaskEntity.getSearchTaskDefinition(), SearchFilter.class);
        SearchTask searchTask = SearchTask.builder().
                searchTaskId(searchTaskEntity.getSearchTaskId()).
                callerId(searchTaskEntity.getSourceRequestId()).
                searchTaskStatus(new SearchTaskStatus(searchTaskEntity.getSearchTaskStatusId())).
                searchTaskProgress(searchTaskEntity.getSearchTaskProgress()).
                searchFilter(searchFilter).build();
        return searchTask;
    }


}
