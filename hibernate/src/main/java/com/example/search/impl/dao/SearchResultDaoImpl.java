package com.example.search.impl.dao;

import com.example.search.api.SearchResult;
import com.example.search.impl.model.SearchResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Repository
@EnableTransactionManagement
public class SearchResultDaoImpl implements SearchResultDao {

    @PersistenceContext(unitName = "search")
    private EntityManager entityManager;

    @PersistenceContext(unitName = "search2")
    private EntityManager entityManager2;

    @Override
    public List<SearchResult> getResults(int callerId) {
        log.info("logging");
        List<SearchResultEntity> searchResultEntities = entityManager.createQuery("FROM SearchResultEntity t " +
                "WHERE sourceRequestId = :callerId ")
                .setParameter("callerId", callerId)
                .getResultList();

        return getInteractionInfos(searchResultEntities);
    }

    private List<SearchResult> getInteractionInfos(List<SearchResultEntity> searchResultEntities) {
        List<SearchResult> list = new ArrayList<>();

        for (SearchResultEntity searchResultEntity : searchResultEntities)
            list.add(mapSearchResultEntityToInteractionInfo(searchResultEntity));
        return list;
    }

    private SearchResult mapSearchResultEntityToInteractionInfo(SearchResultEntity searchResultEntity) {

        return new SearchResult(
                searchResultEntity.getInteractionID(),
                searchResultEntity.getSearchResultIdEntity().getArchiveId(),
                searchResultEntity.getSearchResultIdEntity().getContactGMTStartTime(),
                searchResultEntity.getUserId());
    }
}
