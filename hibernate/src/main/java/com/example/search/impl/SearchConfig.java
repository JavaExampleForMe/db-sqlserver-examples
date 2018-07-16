package com.example.search.impl;

import com.example.search.api.SearchService;
import com.example.search.impl.api.LocalDateTimeUserType;
import com.example.search.impl.dao.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@Import({SearchConfigInternal.class,SearchConfigInternal2.class})
public class SearchConfig {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


    @Bean
    SearchTaskDao searchTaskDao() {
        return new SearchTaskDaoImpl(/*searchEntityManager(), */objectMapper(), searchTaskBatchDao());
    }

    @Bean
    SearchTaskBatchDao searchTaskBatchDao() {
        return new SearchTaskBatchDaoImpl(/*searchEntityManager(),*/ objectMapper());
    }

    @Bean
    SearchResultDao searchResultDao() {
        return new SearchResultDaoImpl(/*searchEntityManager()*/);
    }

    @Bean
    SearchService searchService() {
        return new SearchServiceImpl(searchTaskDao(), searchTaskBatchDao(), searchResultDao());
    }

     @Bean
     LocalDateTimeUserType getLocalDateTimeUserType() {
        return new LocalDateTimeUserType();
    }

}
