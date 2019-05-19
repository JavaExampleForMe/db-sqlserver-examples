package com.example;

import java.io.IOException;
import com.example.search.api.SearchService;
import com.example.search.impl.SearchConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@Import({SearchConfig.class})
public class App {
    public static void main(String[] args) throws IOException {
        final ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
        final SearchService searchService = context.getBean(SearchService.class);
        searchService.getSearchResults(3);
    }


}
